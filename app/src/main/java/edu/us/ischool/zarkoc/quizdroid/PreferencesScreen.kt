package edu.us.ischool.zarkoc.quizdroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextWatcher
import android.widget.EditText
import android.text.Editable
import android.widget.Button


class PreferencesScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_screen)

        val inputMessage : EditText = findViewById<EditText>(R.id.inputURL)
        val inputInterval : EditText = findViewById<EditText>(R.id.inputTime)
        val buttonStart : Button = findViewById<Button>(R.id.buttonDownload)

        buttonStart.setOnClickListener {
            try {
                QuizApp.startNewDownload(inputMessage.text.toString(), inputInterval.text.toString().toInt(), this@PreferencesScreen)
            } catch (e : Error) {
                val downloadFailedAlert = DownloadFailedDialogFragment()
                downloadFailedAlert.show(supportFragmentManager, "download_failed")
            }
        }
    }
}