package edu.us.ischool.zarkoc.quizdroid

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            override var quizList: ArrayList<Topic> = arrayListOf<Topic>(
                Topic("Math", "This is a quick quiz about basic math!", "PLACEHOLDER", arrayListOf<Quiz>(
                    Quiz("2 + 2", arrayListOf<String>("5", "4", "72", "89"), 1),
                    Quiz("3 + 3", arrayListOf<String>("6", "4", "9812", "448"), 0),
                    Quiz("4!", arrayListOf<String>("24", "42", "442", "22222224"), 0),
                    Quiz("447 + 1", arrayListOf<String>("999", "9999", "2", "448"), 3)
                )),
                Topic("Physics", "This is a quick quiz about basic physics!", "PLACEHOLDER", arrayListOf<Quiz>(
                    Quiz("Isaac ______", arrayListOf<String>("Newton", "Dewton", "Pooton", "Gooton"), 0),
                    Quiz("9.8 ___", arrayListOf<String>("answers/s", "m/s", "seventy four", "luftballoons"), 1),
                    Quiz("Is water wet?", arrayListOf<String>("yes", "no", "maybe", "so"), 2)
                )),
                Topic("Marvel", "This is a quick quiz about Marvel superheroes!", "PLACEHOLDER", arrayListOf<Quiz>(
                    Quiz("Spiderman villain", arrayListOf<String>("Green Gobbler", "Kratos", "Kratom", "Mysterio"), 3),
                    Quiz("Infinity stone count", arrayListOf<String>("5", "6", "seventy four", "7"), 1),
                    Quiz("Is Kanye in the MCU?", arrayListOf<String>("ooo idk", "possibly?", "yes", "yes but not kanye west"), 2)
                )),
            )
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
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("TESTING", "QuizApp Loaded")
    }
}