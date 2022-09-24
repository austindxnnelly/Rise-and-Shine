package com.example.alarmapp

import org.junit.Assert.*
import org.junit.Test

class EditDialogTest{
    @Test
    fun checkHourToMilliSecond(){
        val result = EditDialog(
            "0",
            "test",
            0,
            0,
            "00:00",
            true).hourToMilliSecond(10)
        assertEquals(result, 10*3600000)
    }
}