package edu.us.ischool.zarkoc.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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