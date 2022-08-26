package com.example.alarmapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/**
 * Class is used once an alarm pops. To turn off the alarm, you are taken to this page.
 * This is where the use of puzzles and challenges to turn your alarm off take place.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly.
 */
class AlarmActivity : AppCompatActivity() {

    /**
    Sets the content to be shown on the page once you go to turn off the alarm.
    @param savedInstanceState, the state of the screen that is displayed.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
    }
}