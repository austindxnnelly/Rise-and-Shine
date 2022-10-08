package com.example.alarmapp

/**
 * Memory card class used for the memory game to turn off the alarm.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
data class MemoryCard(val identifier: Int, var isFaceUp: Boolean = false, var isMatched: Boolean = false)