package edu.us.ischool.zarkoc.quizdroid

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

const val TOPIC = "edu.us.ischool.zarkoc.quizdroid.topic"
const val DESCRIPTION = "edu.us.ischool.zarkoc.quizdroid.description"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonMath = findViewById<Button>(R.id.quizMath)
        buttonMath.setOnClickListener {
            val intentMath = Intent(this, TopicOverview::class.java).apply {
                putExtra(TOPIC, "Math")
                putExtra(DESCRIPTION, "This is a quick quiz about basic math!")
            }
            startActivity(intentMath)
        }
        val buttonPhysics = findViewById<Button>(R.id.quizPhysics)
        buttonPhysics.setOnClickListener {
            val intentPhysics = Intent(this, TopicOverview::class.java).apply {
                putExtra(TOPIC, "Physics")
                putExtra(DESCRIPTION, "This is a quick quiz about basic physics!")
            }
            startActivity(intentPhysics)
        }
        val buttonMarvel = findViewById<Button>(R.id.quizMarvel)
        buttonMarvel.setOnClickListener {
            val intentMarvel = Intent(this, TopicOverview::class.java).apply {
                putExtra(TOPIC, "Marvel")
                putExtra(DESCRIPTION, "This is a quick quiz about Marvel superheroes!")
            }
            startActivity(intentMarvel)
        }
    }
}

class QuizApp: Application() {
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

    var curQuiz : Int = -1
    var curQuestionNum : Int = 0

    class RepositoryImp : TopicRepository {
        override var quizList: ArrayList<Topic> = arrayListOf<Topic>(
            Topic("Math", "This is a quick quiz about basic math!", "PLACEHOLDER", arrayListOf<Quiz>(
                Quiz("2 + 2", arrayListOf<String>(), 0),
                Quiz("3 + 3", arrayListOf<String>(), 0),
                Quiz("4!", arrayListOf<String>(), 0),
                Quiz("447 + 1", arrayListOf<String>(), 0)
            )),
            Topic("Physics", "This is a quick quiz about basic math!", "PLACEHOLDER", arrayListOf<Quiz>(
                Quiz("", arrayListOf<String>(), 0),
                Quiz("", arrayListOf<String>(), 0),
                Quiz("", arrayListOf<String>(), 0)
            )),
            Topic("Marvel", "This is a quick quiz about basic math!", "PLACEHOLDER", arrayListOf<Quiz>(
                Quiz("", arrayListOf<String>(), 0),
                Quiz("", arrayListOf<String>(), 0),
                Quiz("", arrayListOf<String>(), 0)
            )),
        )
    }

    var repository = RepositoryImp()

    override fun onCreate() {
        super.onCreate()
        Log.i("TESTING", "QuizApp Loaded")
    }

    fun setQuiz(quizNum : Int) {
        curQuiz = quizNum
    }

    fun getNextQuestion() : Quiz {
        return repository.getQuestion(curQuestionNum, curQuiz)
    }
}