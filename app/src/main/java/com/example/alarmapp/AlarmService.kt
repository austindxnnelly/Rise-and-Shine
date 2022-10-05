package com.example.alarmapp

import android.app.Notification
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import androidx.core.app.NotificationCompat
import java.lang.reflect.Executable

/**
 * Alarm service class which is called by myBroadcastReceiver
 * It is responsible for alarm sound and vibration as well as creating notification
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class AlarmService : Service() {
    var mediaPlayer: MediaPlayer? = null
    var vibrator: Vibrator? = null
    var vibratorManager : VibratorManager? = null

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mediaPlayer!!.isLooping = true


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            vibratorManager = getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibrator = vibratorManager!!.defaultVibrator
        }else{
            @Suppress("Deprecated")
            vibrator = (getSystemService(VIBRATOR_SERVICE) as Vibrator)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, AlarmActivity::class.java)
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(this, 0, notificationIntent, FLAG_MUTABLE)
        }else{
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }
        val alarmName = intent!!.getStringExtra("NAME")

        val notification: Notification = NotificationCompat.Builder(this, "alarmApp")
            .setContentTitle(alarmName)
            .setContentText("RINGING")
            .setSmallIcon(R.mipmap.icon_white_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()

        mediaPlayer!!.start()
        //how long to wait, how long to vibrate for, how long to wait for
        val pattern = longArrayOf(0, 500, 200)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val effect = VibrationEffect.createWaveform(pattern,0)
            vibrator!!.vibrate(effect)
        } else {
            @Suppress("Deprecated")
            vibrator!!.vibrate(pattern, 0)
        }
        startForeground(1, notification)
        flipSwitch(intent)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer!!.stop()
        vibrator!!.cancel()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    fun flipSwitch(intent: Intent?){
        val name = intent!!.getStringExtra("NAME")
        val hour = intent!!.getIntExtra("HOUR", -1)
        val minute = intent!!.getIntExtra("MINUTE", -1)
        val id = intent!!.getStringExtra("ID")
        val db = AlarmDatabase(this, "AlarmDatabase", null, 1)

        if (id != null && name != null) {
            db.updateDatabase(id, name, hour, minute, 0)
        }else{
            print("ID not valid - id: $id")
//            throw Exception("ID not valid - id: $id - name valid? $name")
        }
    }

}