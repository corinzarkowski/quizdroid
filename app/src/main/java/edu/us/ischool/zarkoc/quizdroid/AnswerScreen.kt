package edu.us.ischool.zarkoc.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AnswerScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer_screen2)

        val questions = intent.getStringArrayListExtra(QUESTIONLIST)
        var answersCorrect = intent.getIntExtra(ANSWERSCORRECT, 0)
        val answers = intent.getStringArrayListExtra(ANSWERLIST)
        var questionNum = intent.getIntExtra(QUESTIONNUM, 0)
        var answerGiven = intent.getStringExtra(ANSWERGIVEN)
        val buttonNext = findViewById<Button>(R.id.buttonNext)

        if (questions != null && questionNum >= questions.size) {
            buttonNext.text = "Finish"
        }

        val answerGivenText = findViewById<TextView>(R.id.answerGiven)
        val answerCorrectText = findViewById<TextView>(R.id.answerCorrect)
        val answerTrack = findViewById<TextView>(R.id.answerTrack)

        if (answers != null) {
            answerTrack.text = "You have " + answersCorrect + " out of " + questionNum + " correct"
            answerCorrectText.text = "Correct: " + answers.get(((questionNum - 1) * 5) + answers.get(((questionNum - 1) * 5) + 4).toInt() - 1)
            if (answerGiven != null) {
                answerGivenText.text = "Given: " + answers.get(((questionNum - 1) * 5) + answerGiven.toInt() - 1)
            }
        }

        buttonNext.setOnClickListener {
            if (questions != null && questionNum < questions.size) {
                var intentNext = Intent(this, QuizScreen::class.java).apply {
                    putExtra(QUESTIONLIST, questions)
                    putExtra(QUESTIONNUM, questionNum)
                    putExtra(ANSWERLIST, answers)
                    putExtra(ANSWERSCORRECT, answersCorrect)
                }
                startActivity(intentNext)
            } else {
                var intentFinish = Intent(this, MainActivity::class.java)
                startActivity(intentFinish)
            }

        }
    }
}