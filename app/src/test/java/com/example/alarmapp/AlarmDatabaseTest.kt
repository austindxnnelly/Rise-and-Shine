package com.example.alarmapp

import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test

class AlarmDatabaseTest{
    @Test
    fun testDatabase(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val db = AlarmDatabase(appContext, "TestDatabase", null, 1)
        db.addAlarm("test", 0, 0, 0)
        val cursor = db.readAllData()
        val id = cursor?.getInt(0)
        db.updateDatabase(id.toString(), "test_change", 1, 1, 1)
        db.deleteOneRow(id.toString())
    }
}