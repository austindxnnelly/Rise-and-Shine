package com.example.alarmapp

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

/**
 * Class which is invoked when alarm rung
 * Class which is invoked when phone is rebooted
 */
class myBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        //what happens when alarm is triggered
        //opening AlarmActivity
        val i = Intent(context, AlarmActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, i, FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(context, "alarmApp")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Alarm App") //change to R.appTitle (or similar)
            .setContentText("Alarm is ringing") //change to unique name for each alarm
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(123, builder.build())


        // after the reboot has been completed turn alarms back on
        if(intent.action.equals("android.intent.action.BOOT_COMPLETED")){
            //TODO: set alarm
            val ab = 2
        }
    }
}