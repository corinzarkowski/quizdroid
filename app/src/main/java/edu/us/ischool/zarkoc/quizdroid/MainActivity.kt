package edu.us.ischool.zarkoc.quizdroid

import android.Manifest
import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import java.io.*
import androidx.core.app.ActivityCompat
import android.util.JsonReader


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ),
            1
        )

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
//            override var quizList : ArrayList<Topic> = arrayListOf<Topic>(
//                Topic("Math", "This is a quick quiz about basic math!", "PLACEHOLDER", arrayListOf<Quiz>(
//                    Quiz("2 + 2", arrayListOf<String>("5", "4", "72", "89"), 1),
//                    Quiz("3 + 3", arrayListOf<String>("6", "4", "9812", "448"), 0),
//                    Quiz("4!", arrayListOf<String>("24", "42", "442", "22222224"), 0),
//                    Quiz("447 + 1", arrayListOf<String>("999", "9999", "2", "448"), 3)
//                )),
//                Topic("Physics", "This is a quick quiz about basic physics!", "PLACEHOLDER", arrayListOf<Quiz>(
//                    Quiz("Isaac ______", arrayListOf<String>("Newton", "Dewton", "Pooton", "Gooton"), 0),
//                    Quiz("9.8 ___", arrayListOf<String>("answers/s", "m/s", "seventy four", "luftballoons"), 1),
//                    Quiz("Is water wet?", arrayListOf<String>("yes", "no", "maybe", "so"), 2)
//                )),
//                Topic("Marvel", "This is a quick quiz about Marvel superheroes!", "PLACEHOLDER", arrayListOf<Quiz>(
//                    Quiz("Spiderman villain", arrayListOf<String>("Green Gobbler", "Kratos", "Kratom", "Mysterio"), 3),
//                    Quiz("Infinity stone count", arrayListOf<String>("5", "6", "seventy four", "7"), 1),
//                    Quiz("Is Kanye in the MCU?", arrayListOf<String>("ooo idk", "possibly?", "yes", "yes but not kanye west"), 2)
//                )),
//            )
            override var quizList : ArrayList<Topic> = arrayListOf<Topic>()
        }

        private var repository = RepositoryImp()
        var curQuiz : Int = -1
        var curQuestionNum : Int = 0
        var curScore : Int = 0
        var lastAnswer : String = ""
        var lastCorrectAnswer : String = ""

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