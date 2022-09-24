package com.example.alarmapp

/**
 * Add to database object. Used as a database for the math questions.
 */
object AddToDatabase {

    /**
     * Get question from the database.
     * @return arraylist of questions.
     */
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