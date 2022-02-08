package edu.us.ischool.zarkoc.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView


class QuizScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_screen)

        val curQuestion = QuizApp.getNextQuestion()

        val buttonSubmit = findViewById<Button>(R.id.buttonSubmit)
        val questionTitle = findViewById<TextView>(R.id.questionName)
        val answerGroup = findViewById<RadioGroup>(R.id.answerGroup)

        questionTitle.text = curQuestion.question

        val answer1 = findViewById<RadioButton>(R.id.answer1)
        val answer2 = findViewById<RadioButton>(R.id.answer2)
        val answer3 = findViewById<RadioButton>(R.id.answer3)
        val answer4 = findViewById<RadioButton>(R.id.answer4)
        answer1.text = curQuestion.answers.get(0)
        answer2.text = curQuestion.answers.get(1)
        answer3.text = curQuestion.answers.get(2)
        answer4.text = curQuestion.answers.get(3)

        buttonSubmit.setOnClickListener {
            when (curQuestion.correctAnswer) {
                0 -> if (answer1.isChecked) {QuizApp.updateScore()}
                1 -> if (answer2.isChecked) {QuizApp.updateScore()}
                2 -> if (answer3.isChecked) {QuizApp.updateScore()}
                3 -> if (answer4.isChecked) {QuizApp.updateScore()}
            }

            QuizApp.lastAnswer = when(answerGroup.checkedRadioButtonId) {
                R.id.answer1 -> curQuestion.answers.get(0)
                R.id.answer2 -> curQuestion.answers.get(1)
                R.id.answer3 -> curQuestion.answers.get(2)
                R.id.answer4 -> curQuestion.answers.get(3)
                else -> ""
            }
            QuizApp.lastCorrectAnswer = curQuestion.answers.get(curQuestion.correctAnswer)

            var intentSubmit = Intent(this, AnswerScreen::class.java)
            startActivity(intentSubmit)
        }
    }
}