package com.example.alarmapp

/**
 * Question database class which contains an id, the question, each option and the solution.
 */
data class QuestionDatabase(
var id:Int,
var question:String,

var option_one:String,
var option_two:String,
var option_three:String,
var option_four:String,
var correct_ans:Int
)