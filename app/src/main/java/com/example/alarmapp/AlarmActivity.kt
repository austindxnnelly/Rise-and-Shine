package com.example.alarmapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_alarm.*
import android.widget.Button

/**
 * Alarm activity class, used for the alarm question to disable the alarm.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class AlarmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        val buttonClickMath = findViewById<Button>(R.id.btn_math)
        buttonClickMath.setOnClickListener {
            val intent = Intent(this, AlarmActivityMath::class.java)
            startActivity(intent)
        }
        val buttonClickMemory = findViewById<Button>(R.id.btn_mem)
        buttonClickMemory.setOnClickListener {
            val intent = Intent(this, AlarmActivityMemory::class.java)
            startActivity(intent)
        }
    }

}