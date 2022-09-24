package com.example.alarmapp

import org.junit.Assert.*
import org.junit.Test

class MainActivityTest{
    @Test
    fun testMainActivity(){
        val mainActivity = MainActivity()
        mainActivity.createNotificationChannel()
        mainActivity.setAlarm(0, 0, 0)
        mainActivity.cancelAlarm(0)
    }
}