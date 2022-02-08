package edu.us.ischool.zarkoc.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView

const val ANSWERGIVEN = "edu.us.ischool.zarkoc.quizdroid.answerGiven"

class QuizScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_screen)

        val questions = intent.getStringArrayListExtra(QUESTIONLIST)
        var answersCorrect = intent.getIntExtra(ANSWERSCORRECT, 0)
        val answers = intent.getStringArrayListExtra(ANSWERLIST)
        var questionNum = intent.getIntExtra(QUESTIONNUM, 0)

        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        val questionTitle = findViewById<TextView>(R.id.questionName)
        val answerGroup = findViewById<RadioGroup>(R.id.answerGroup)

        if (questions != null) {
            questionTitle.text = questions.get(questionNum)
        }

        val answer1 = findViewById<RadioButton>(R.id.answer1)
        val answer2 = findViewById<RadioButton>(R.id.answer2)
        val answer3 = findViewById<RadioButton>(R.id.answer3)
        val answer4 = findViewById<RadioButton>(R.id.answer4)
        if (answers != null) {
            answer1.text = answers.get(0 + (questionNum * 5))
            answer2.text = answers.get(1 + (questionNum * 5))
            answer3.text = answers.get(2 + (questionNum * 5))
            answer4.text = answers.get(3 + (questionNum * 5))
        }

        buttonSubmit.setOnClickListener {
            if (answers != null) {
                val correctAnswer = answers.get((questionNum * 5) + 4)

                when (correctAnswer) {
                    "1" -> if (answer1.isChecked) {answersCorrect += 1}
                    "2" -> if (answer2.isChecked) {answersCorrect += 1}
                    "3" -> if (answer3.isChecked) {answersCorrect += 1}
                    "4" -> if (answer4.isChecked) {answersCorrect += 1}
                }
            }

            questionNum += 1

            var checkedAnswer : String = when (answerGroup.getCheckedRadioButtonId()) {
                R.id.answer1 -> "1"
                R.id.answer2 -> "2"
                R.id.answer3 -> "3"
                R.id.answer4 -> "4"
                else -> "-1"
            }

            var intentSubmit = Intent(this, AnswerScreen::class.java).apply {
                putExtra(QUESTIONLIST, questions)
                putExtra(QUESTIONNUM, questionNum)
                putExtra(ANSWERLIST, answers)
                putExtra(ANSWERSCORRECT, answersCorrect)
                putExtra(ANSWERGIVEN, checkedAnswer)
            }
            startActivity(intentSubmit)
        }
    }
}