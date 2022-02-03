package edu.us.ischool.zarkoc.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

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
    }
}