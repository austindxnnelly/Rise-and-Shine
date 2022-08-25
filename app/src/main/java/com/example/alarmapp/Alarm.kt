package com.example.alarmapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.icu.util.Calendar
import android.os.Build
import androidx.core.content.ContextCompat.getSystemService
import android.content.Intent
import android.widget.Toast

/**
 *
 */
class Alarm (
    private val name : String,
    private val hour: Int,
    private val minute: Int,
    private val day : Int,
    ){

    private lateinit var alarmMgr: AlarmManager
    private lateinit var alarmIntent: PendingIntent
    private lateinit var calendar : Calendar
    var timeInMillis : Long? = null


    fun setTime(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                //set(Calendar.DAY_OF_WEEK, day!!)
                set(Calendar.HOUR_OF_DAY, hour)
                set(Calendar.MINUTE, minute)
            }
        } else {
            TODO("VERSION.SDK_INT < N")
        }
    }
    /**
     *
     */
    fun setAlarm(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, myBroadcastReceiver::class.java)

            alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
            alarmMgr!!.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent
            )
            Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show()

/*
            //S = 31
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmMgr?.canScheduleExactAlarms() == true) {
                    alarmMgr?.setExact(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        alarmIntent
                    )
                } else {
                    alarmMgr?.set(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        alarmIntent
                    )
                }
            } else {
                alarmMgr?.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmIntent
                )
            }*/
            timeInMillis = calendar.timeInMillis
        }
    }

    fun cancelAlarm(){
        alarmMgr = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, myBroadcastReceiver::class.java)

        alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmMgr.cancel(alarmIntent)

        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_LONG).show()
    }

    fun createMessage() : String{
        val info = AlarmManager.AlarmClockInfo(timeInMillis!!, alarmIntent)
        val trigTime = info.triggerTime
        val showInt = info.showIntent

        val message = "Alarm created successfully at: $trigTime, with info: $showInt"
        return message
        /*val mySnackBar = Snackbar.make(
            findViewById(R.id.recyclerView),
            message,
            Snackbar.LENGTH_LONG
        )
        mySnackBar.show()*/
    }
}