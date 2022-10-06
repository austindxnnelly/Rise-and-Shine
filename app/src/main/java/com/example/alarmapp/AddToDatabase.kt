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

        var question2 = QuestionDatabase(
            1,
            "What is 9 times 9?",
            "72",
            "81",
            "90",
            "99",
            2
        )

        que.add(question1)
        que.add(question2)
        return que
    }
}