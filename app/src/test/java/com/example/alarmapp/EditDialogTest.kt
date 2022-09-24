package com.example.alarmapp

import android.view.View
import kotlinx.android.synthetic.main.edit_dialog.*
import kotlinx.android.synthetic.main.my_row.*
import org.junit.Assert.*
import org.junit.Test

/**
 * This class is used to test the edit dialog class.
 */
class EditDialogTest{
    /**
     * This test makes sure that the hourToMilliSecond function is working correctly.
     */
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

    /**
     * This test makes sure that the minuteToMilliSecond function is working correctly.
     */
    @Test
    fun checkMinuteToMilliSecond(){
        val result = EditDialog(
            "0",
            "test",
            0,
            0,
            "00:00",
            true).minuteToMilliSecond(10)
        assertEquals(result, 10*60000)
    }

    /**
     * This test makes sure that the secondToMilliSecond function is working correctly.
     */
    @Test
    fun checkSecondToMilliSecond(){
        val result = EditDialog(
            "0",
            "test",
            0,
            0,
            "00:00",
            true).secondToMilliSecond(10)
        assertEquals(result, 10*1000)
    }
}