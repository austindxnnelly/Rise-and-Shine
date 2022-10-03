package com.example.alarmapp

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import android.os.Vibrator
import androidx.core.app.NotificationCompat


class AlarmService : Service() {
    var mediaPlayer: MediaPlayer? = null
    var vibrator: Vibrator? = null

    override fun onCreate() {
        super.onCreate()

        mediaPlayer = MediaPlayer.create(this, R.raw.alarm)
        mediaPlayer!!.isLooping = true

        vibrator = (getSystemService(VIBRATOR_SERVICE) as Vibrator)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationIntent = Intent(this, AlarmActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val alarmName = intent!!.getStringExtra("NAME")

        val notification: Notification = NotificationCompat.Builder(this, "alarmApp")
            .setContentTitle(alarmName)
            .setContentText("RINGING")
            .setSmallIcon(R.mipmap.icon_white_foreground)
            .setContentIntent(pendingIntent)
            .build()

        mediaPlayer!!.start()

        val pattern = longArrayOf(0, 100, 1000)
        vibrator!!.vibrate(pattern, 0)

        startForeground(1, notification)

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

}