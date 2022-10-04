package com.example.alarmapp

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


/**
 * Class which is invoked when alarm rung
 * Class which is invoked when phone is rebooted
 */
class myBroadcastReceiver : BroadcastReceiver(){
    /**
     * this is called when an alarm is triggered it calls AlarmService
     * @param context, The Context in which the receiver is running.
     * @param intent, The Intent being received.
     */
    override fun onReceive(context: Context, intent: Intent) {
        //what happens when alarm is triggered
        //opening AlarmActivity
        val testing = false
        if(testing) {
            val i = Intent(context, AlarmActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                PendingIntent.getActivity(context, 0, i, FLAG_MUTABLE)
            }else{
                PendingIntent.getActivity(context, 0, i, 0)
            }

            val builder = NotificationCompat.Builder(context, "alarmApp")
                .setSmallIcon(R.mipmap.icon_white_foreground)
                //.setContentTitle(R.string.app_name.toString())
                .setContentText("ALARM IS RINGING") //change to unique name for each alarm
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(123, builder.build())
        }else{
            Toast.makeText(context, "Alarm Received", Toast.LENGTH_SHORT).show()
            startAlarmService(context, intent)
        }

        // after the reboot has been completed turn alarms back on
        if(intent.action.equals("android.intent.action.BOOT_COMPLETED")){
            //TODO: set alarm
            val ab = 2
        }
    }

    /**
     * Starts the AlarmService class
     *
     * @param context required context
     * @param intent required intent
     */
    fun startAlarmService(context: Context, intent: Intent){
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra("NAME", intent.getStringExtra("NAME"))

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}