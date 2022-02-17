package edu.us.ischool.zarkoc.quizdroid

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import java.io.*
import androidx.core.app.ActivityCompat
import android.util.JsonReader
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class NoConnectionDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("You have no internet connection!")
                .setPositiveButton("Exit",
                    DialogInterface.OnClickListener { dialog, id -> exitProcess(-1) })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class AirplaneModeDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("You have no internet connection! Disable airplane mode?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, id ->
                        startActivity(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS))
                    })
                .setNegativeButton("Exit",
                    DialogInterface.OnClickListener { dialog, id ->
                        exitProcess(-1)
                    })
            // Create the AlertDialog object and return it
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class DownloadFailedDialogFragment : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage("Download failed!")
                .setPositiveButton("Retry",
                    DialogInterface.OnClickListener { dialog, id -> Log.i("TESTING", "retrying")})
                .setNegativeButton("Exit",
                    DialogInterface.OnClickListener { dialog, id -> exitProcess(-1) })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

class MainActivity : AppCompatActivity() {
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    var questionFile = File(applicationContext.filesDir.toString() + "/questions.json")
                    var questionReader = questionFile.reader()
                    QuizApp.parseJSON(questionReader)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val intentPrefs = Intent(this, PreferencesScreen::class.java)
            startActivity(intentPrefs)
            true
        }

        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actionbar, menu)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.prefs_bar))

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.INTERNET
            ),
            1
        )

        fun isOnline(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                        return true
                    }
                }
            }
            return false
        }

        if (!isOnline(this)) {
            if (Settings.System.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) == 0) {
                val noConnectionAlert = NoConnectionDialogFragment()
                noConnectionAlert.show(supportFragmentManager, "no_connection")
            } else {
                val airplaneModeAlert = AirplaneModeDialogFragment()
                airplaneModeAlert.show(supportFragmentManager, "no_connection_airplane")
            }
        } else {
            try {
                Toast.makeText(this, "https://raw.githubusercontent.com/corinzarkowski/quizdroid/master/app/src/main/data/questions.json", Toast.LENGTH_SHORT).show()
                QuizApp.startNewDownload("https://raw.githubusercontent.com/corinzarkowski/quizdroid/master/app/src/main/data/questions.json", 1, this)
            } catch (e : Error) {
                val downloadFailedAlert = DownloadFailedDialogFragment()
                downloadFailedAlert.show(supportFragmentManager, "download_failed")
            }
        }

        val buttonMath = findViewById<Button>(R.id.quizMath)
        buttonMath.setOnClickListener {
            val intentMath = Intent(this, TopicOverview::class.java)
            QuizApp.setTopic(0)
            startActivity(intentMath)
        }
        val buttonPhysics = findViewById<Button>(R.id.quizPhysics)
        buttonPhysics.setOnClickListener {
            val intentPhysics = Intent(this, TopicOverview::class.java)
            QuizApp.setTopic(1)
            startActivity(intentPhysics)
        }
        val buttonMarvel = findViewById<Button>(R.id.quizMarvel)
        buttonMarvel.setOnClickListener {
            val intentMarvel = Intent(this, TopicOverview::class.java)
            QuizApp.setTopic(2)
            startActivity(intentMarvel)
        }
    }
}

class QuizApp: Application() {
    companion object {
        data class Quiz(val question : String, val answers : ArrayList<String>, val correctAnswer : Int)
        data class Topic(val title : String, val descriptionShort : String, val descriptionLong : String, val questions : ArrayList<Quiz>)

        interface TopicRepository {
            var quizList : ArrayList<Topic>

            fun getQuestion(questionNum : Int, topic : Int) : Quiz {
                val curQuestions : ArrayList<Quiz> = quizList.get(topic).questions

                return curQuestions.get(questionNum)
            }

            fun getTopic(topicIndex : Int) : Topic {
                return quizList.get(topicIndex)
            }
        }

        class RepositoryImp : TopicRepository {
            override var quizList : ArrayList<Topic> = arrayListOf<Topic>()
        }

        private var repository = RepositoryImp()
        var curQuiz : Int = -1
        var curQuestionNum : Int = 0
        var curScore : Int = 0
        var lastAnswer : String = ""
        var lastCorrectAnswer : String = ""

        fun startNewDownload(inputUrl: String, inputInterval: Int, con : Context) {
            if (inputUrl.isNotEmpty() && inputInterval > 0) {
                var downloadTimer : Timer = Timer()
                val interval : Long = inputInterval.toLong() * 600000

                downloadTimer.schedule(object : TimerTask() {
                    override fun run() {
                        val apiResponse = URL("https://raw.githubusercontent.com/corinzarkowski/quizdroid/master/app/src/main/data/questions.json").readText()
                        var questionFile = File(con.filesDir.toString() + "/questions.json")
                        var questionWriter = FileWriter(questionFile)
                        if (apiResponse.isNotEmpty()) {
                            questionWriter.write(apiResponse)
                            questionWriter.close()
                        }

                    }
                }, 0, interval)
            }
        }

        fun getTopicInfo() : Topic {
            return repository.getTopic(curQuiz)
        }

        fun setTopic(quizNum : Int) {
            curQuiz = quizNum
        }

        fun getNextQuestion() : Quiz {
            curQuestionNum += 1
            return repository.getQuestion(curQuestionNum - 1, curQuiz)
        }

        fun quizOver() : Boolean {
            return curQuestionNum == repository.getTopic(curQuiz).questions.size
        }

        fun updateScore() {
            curScore += 1
        }

        fun reset() {
            curQuiz = -1
            curQuestionNum = 0
            curScore = 0
            lastAnswer = ""
            lastCorrectAnswer = ""
        }

        fun parseJSON(input : InputStreamReader) {
            var curTopics : ArrayList<Topic> = arrayListOf<Topic>()

            val questionJson = JsonReader(input)
            questionJson.beginArray()
            while (questionJson.hasNext()) {
                var curTitle : String = ""
                var curDesc : String = ""
                var curQuiz : ArrayList<Quiz> = arrayListOf<Quiz>()

                questionJson.beginObject()
                if (questionJson.nextName() == "title") {
                    curTitle = questionJson.nextString()
                }
                if (questionJson.nextName() == "desc") {
                    curDesc = questionJson.nextString()
                }
                if (questionJson.nextName() == "questions") {
                    questionJson.beginArray()
                    while (questionJson.hasNext()) {
                        var curText : String = ""
                        var curAnswer : Int = -1
                        var answers : ArrayList<String> = arrayListOf<String>()

                        questionJson.beginObject()
                        if (questionJson.nextName() == "text") {
                            curText = questionJson.nextString()
                        }
                        if (questionJson.nextName() == "answer") {
                            curAnswer = questionJson.nextString().toInt()
                        }
                        if (questionJson.nextName() == "answers") {
                            questionJson.beginArray()
                            while (questionJson.hasNext()) {
                                answers.add(questionJson.nextString())
                            }
                            questionJson.endArray()
                        }
                        curQuiz.add(Quiz(curText, answers, curAnswer))
                        questionJson.endObject()
                    }
                    questionJson.endArray()
                }
                curTopics.add(Topic(curTitle, curDesc, "", curQuiz))
                questionJson.endObject()
            }
            Log.i("TESTING", "current repo: " + curTopics.toString())
            repository.quizList = curTopics
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("TESTING", "QuizApp Loaded")
    }
}