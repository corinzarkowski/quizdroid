package edu.us.ischool.zarkoc.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TopicOverview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic_overview)

        val quizTitle = findViewById<TextView>(R.id.quizTitle)
        val quizDesc = findViewById<TextView>(R.id.quizDesc)
        val quizTopic = QuizApp.getTopicInfo()

        quizTitle.text = quizTopic.title
        quizDesc.text = quizTopic.descriptionShort

        val buttonStart = findViewById<Button>(R.id.buttonStart)
        buttonStart.setOnClickListener {
            val intentStart = Intent(this, QuizScreen::class.java)
            startActivity(intentStart)
        }
    }
}