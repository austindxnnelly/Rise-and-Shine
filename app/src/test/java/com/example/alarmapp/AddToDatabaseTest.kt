package com.example.alarmapp

import org.junit.Assert.*
import org.junit.Test

/**
 * A class that is used to test the AddToDatabase class
 */
class AddToDatabaseTest {
    /**
     * This function makes sure that the question is not null
     */
    @Test
    fun testQuestion(){
        val result = AddToDatabase.getQuestion()
        assertNotNull(result)
    }
}