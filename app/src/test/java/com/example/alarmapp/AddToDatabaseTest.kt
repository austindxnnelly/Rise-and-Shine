package com.example.alarmapp

import org.junit.Assert.*
import org.junit.Test

class AddToDatabaseTest {
    @Test
    fun testQuestion(){
        val result = AddToDatabase.getQuestion()
        assertNotNull(result)
    }
}