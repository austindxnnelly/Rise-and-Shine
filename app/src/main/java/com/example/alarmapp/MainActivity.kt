package com.example.alarmapp

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.Context
import android.content.Intent
import android.icu.text.Normalizer.NO
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.alarmapp.databinding.ActivityMainBinding
import java.util.*
import android.icu.util.Calendar
import android.icu.util.Calendar.*
import android.util.Log

/**
 * Main class, used for our notification channel, allowing
 * the OS to recognise when an alarm pops, and give the
 * user a notification, as well as acting as the main base
 * for all of our controls. Where you add an alarm etc
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        createNotificationChannel()
    }

    /**
     * Function to create notification channel
     * which allows alarms to sound
     */
    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name : CharSequence = "alarmRingingChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("Rise&Shine",name,importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Function to set alarm
     * displays a short message
     * @param hour the hour when alarm should go off
     * @param minute the minute when alarm should go off
     */
    fun setAlarm(hour : Int, minute: Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(HOUR_OF_DAY, hour)
                set(MINUTE, minute)
            }

            alarmMgr = this.getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, myBroadcastReceiver::class.java)

            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_MUTABLE)
            alarmMgr.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
            Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Function to cancel the alarm
     * displays a short message
     */
    fun cancelAlarm(){
        alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, myBroadcastReceiver::class.java)

        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, FLAG_MUTABLE)
        alarmMgr.cancel(alarmIntent)

        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_LONG).show()
    }

    /**
     * @return
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /**
     * @return
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}