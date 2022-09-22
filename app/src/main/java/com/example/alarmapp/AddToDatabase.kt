package com.example.alarmapp

object AddToDatabase {

    fun getQuestion():ArrayList<QuestionDatabase>{
        var que:ArrayList<QuestionDatabase> = arrayListOf()

        var question1 = QuestionDatabase(
            1,
            "What is 12 times 5?",

            "20",
            "40",
            "56",
            "60",
            4
        )

        que.add(question1)
        return que
    }
}