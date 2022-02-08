package edu.us.ischool.zarkoc.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class AnswerScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_screen2)

        val buttonNext = findViewById<Button>(R.id.buttonNext)
        if (QuizApp.quizOver()) {
            buttonNext.text = "Finish"
        }

        val answerGivenText = findViewById<TextView>(R.id.answerGiven)
        val answerCorrectText = findViewById<TextView>(R.id.answerCorrect)
        val answerTrack = findViewById<TextView>(R.id.answerTrack)

        answerTrack.text = "You have " + QuizApp.curScore + " out of " + QuizApp.curQuestionNum + " correct"
        answerCorrectText.text = "Correct: " + QuizApp.lastCorrectAnswer
        answerGivenText.text = "Given: " + QuizApp.lastAnswer

        buttonNext.setOnClickListener {
            if (QuizApp.quizOver()) {
                QuizApp.reset()
                
                var intentFinish = Intent(this, MainActivity::class.java)
                startActivity(intentFinish)
            } else {
                var intentNext = Intent(this, QuizScreen::class.java)
                startActivity(intentNext)
            }
        }
    }
}