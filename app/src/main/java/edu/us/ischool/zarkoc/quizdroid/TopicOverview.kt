package edu.us.ischool.zarkoc.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

const val QUESTIONLIST = "edu.us.ischool.zarkoc.quizdroid.questionList"
const val QUESTIONNUM = "edu.us.ischool.zarkoc.quizdroid.questionNum"
const val ANSWERLIST = "edu.us.ischool.zarkoc.quizdroid.answerList"
const val ANSWERSCORRECT = "edu.us.ischool.zarkoc.quizdroid.answersCorrect"

class TopicOverview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val quizTopic = intent.getStringExtra(TOPIC)
        val quizTitle= findViewById<TextView>(R.id.quizTitle)
        val quizDescText = intent.getStringExtra(DESCRIPTION)
        val quizDesc= findViewById<TextView>(R.id.quizDesc)

        quizTitle.text = quizTopic
        quizDesc.text = quizDescText

        var questionList = arrayListOf<String>()
        var answerList = arrayListOf<String>()
        var questionNum : Int = 0

        when (quizTopic) {
            "Math" -> {questionList = arrayListOf<String>("2 + 2", "3 + 3", "4!", "447 + 1")}
            "Physics" -> {questionList = arrayListOf<String>("Isaac ______", "9.8 ___", "Is water wet?")}
            "Marvel" -> {questionList = arrayListOf<String>("Spiderman villain", "Infinity stone count", "Is Kanye in the MCU?")}
        }

        when (quizTopic) {
            "Math" -> {answerList = arrayListOf<String>(
                "5", "4", "72", "89", "2",
                "6", "4", "9812", "448", "1",
                "24", "42", "442", "22222224", "1",
                "999", "9999", "2", "448", "4"
            )}
            "Physics" -> {answerList = arrayListOf<String>(
                "Newton", "Dewton", "Pooton", "Gooton", "1",
                "answers/s", "m/s", "seventy four", "luftballoons", "2",
                "yes", "no", "maybe", "so", "3"
            )}
            "Marvel" -> {answerList = arrayListOf<String>(
                "Green Gobbler", "Kratos", "Kratom", "Mysterio", "4",
                "5", "6", "seventy four", "7", "2",
                "ooo idk", "possibly?", "yes", "yes but not kanye west", "3"
            )}
        }

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        buttonStart.setOnClickListener {
            val intentStart = Intent(this, QuizScreen::class.java).apply {
                putExtra(TOPIC, quizTopic)
                putExtra(QUESTIONLIST, questionList)
                putExtra(QUESTIONNUM, questionNum)
                putExtra(ANSWERLIST, answerList)
                putExtra(ANSWERSCORRECT, 0)
            }
            startActivity(intentStart)
        }
    }
}