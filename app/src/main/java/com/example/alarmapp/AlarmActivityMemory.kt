package com.example.alarmapp
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_alarm_memory.*

private const val TAG = "AlarmActivityMemory"

/**
 * Alarm activity class, used for the alarm memory game to disable the alarm.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class AlarmActivityMemory : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null
    private var correctAmt: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_memory)

        val images = mutableListOf(R.drawable.apple, R.drawable.orange, R.drawable.pear, R.drawable.berry)
        images.addAll(images)
        images.shuffle()

        buttons = listOf(button1, button2, button3, button4, button5,
            button6, button7, button8)

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                Log.i(TAG, "Option Selected.")
                updateModels(index)
                updateViews()
            }
        }
    }

    /**
     * This function is called to update the views.
     */
    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.facedown)
        }
    }

    /**
     * This function is called to update the models.
     */
    private fun updateModels(position: Int) {
        val card = cards[position]
        if (card.isFaceUp) {
            Toast.makeText(this, "Invalid", Toast.LENGTH_SHORT).show()
            return
        }

        if (indexOfSingleSelectedCard == null) {
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
    }

    /**
     * This function is called to restore the cards.
     */
    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    /**
     * This function is used to check for a match between the selected pair.
     * @param position1 the first card position.
     * @param position2 the second card position.
     */
    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].identifier == cards[position2].identifier) {
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
            cards[position1].isMatched = true
            cards[position2].isMatched = true
            correctAmt += 1;
            if (correctAmt == 4) {
                stopAlarm();
            }
        }
    }

    /**
     * This function is called to stop the alarm.
     */
    private fun stopAlarm() {
        val intentService = Intent(applicationContext, AlarmService::class.java)
        applicationContext.stopService(intentService)
        Toast.makeText(
            this,
            "Rise&Shine!!", Toast.LENGTH_SHORT
        ).show()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}