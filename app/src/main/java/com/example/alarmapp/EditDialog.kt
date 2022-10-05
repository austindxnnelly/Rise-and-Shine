package com.example.alarmapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.asp.fliptimerviewlibrary.CountDownClock
import com.example.alarmapp.databinding.EditDialogBinding
import java.lang.Math.abs
import java.util.*

/**
 * Edit dialog that appears when alarm is being edited.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class EditDialog(
    private val id: String,
    private val name: String,
    private val hour: Int,
    private val minute: Int,
    private val time: String,
    private val switch_state: Boolean) : DialogFragment() {

    private var _binding: EditDialogBinding? = null
    private val binding get() = _binding!!

    /**
     * Called when View is created. The view will later be terminated .
     * @param inflater, converts XML to a View
     * @param container, parent view that the Dialog's UI should be attached to.
     * @param savedInstanceState, reconstructed by previous state
     *
     * @return The View for the Dialog's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditDialogBinding.inflate(inflater, container, false)
        if(name == ""){
            binding.EditName.hint = "Alarm Name"
        }

        binding.EditName.setText(name)

        binding.EditTime.text = time

        binding.EditButton.setOnClickListener {
            val db = AlarmDatabase(context, "AlarmDatabase", null, 1)
            var s = 0
            if(switch_state){
                s = 1
            }
            db.updateDatabase(id, binding.EditName.text.toString(), hour, minute, s)
            val idInt = Integer.parseInt(id)
            (activity as MainActivity).cancelAlarm(idInt)
            (activity as MainActivity).setAlarm(hour, minute, idInt, name)
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
            dismiss()
        }

        if(switch_state) {
            binding.timerProgramCountdown.visibility = View.VISIBLE
            binding.setTime.hint = ""

            val rightNow = Calendar.getInstance()
            val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
            val currentMinute = rightNow.get(Calendar.MINUTE)
            val currentSeconds = rightNow.get(Calendar.SECOND)
            val countdown : Int

            var hourDifference = abs(currentHour - hour)
            var minuteDifference = abs(currentMinute - minute)

            if((currentHour == hour && currentMinute >= minute)){
                hourDifference = 23 - hourDifference
                minuteDifference = 60 - minuteDifference
                countdown = (hourToMilliSecond(hourDifference)
                        + minuteToMilliSecond(minuteDifference)
                        - secondToMilliSecond(currentSeconds))
            }else if((currentHour == hour && currentMinute < minute) || (currentHour < hour && hour != 0)){
                countdown = (hourToMilliSecond(hourDifference)
                        + minuteToMilliSecond(minuteDifference)
                        - secondToMilliSecond(currentSeconds))
            }else{
                hourDifference = 24 - hourDifference
                countdown = (hourToMilliSecond(hourDifference)
                        + minuteToMilliSecond(minuteDifference)
                        - secondToMilliSecond(currentSeconds))
            }


            binding.timerProgramCountdown.startCountDown(countdown.toLong())
            binding.timerProgramCountdown.setCountdownListener(object :
                CountDownClock.CountdownCallBack {

                /**
                 * Member function which is called when the countdown is about to finish
                 */
                override fun countdownAboutToFinish() {
                    //Has to be implemented
                }

                /**
                 * When the countdown has finished this function is called
                 */
                override fun countdownFinished() {
                    binding.timerProgramCountdown.visibility = View.INVISIBLE
                    binding.setTime.hint = "Alarm is not set"
                }
            })
        }else{
            binding.timerProgramCountdown.visibility = View.INVISIBLE
            binding.setTime.hint = "Alarm is not set"
        }

        return binding.root
    }

    /**
     * Function that converts hours to milliseconds
     * @param hour, The hour to be converted
     * @return hour converted to milliseconds
     */
    fun hourToMilliSecond(hour: Int): Int {
        return hour * 3600000
    }

    /**
     * Function that converts minutes to milliseconds
     * @param minute, The minute to be converted
     * @return minute converted to milliseconds
     */
    fun minuteToMilliSecond(minute: Int): Int{
        return minute * 60000
    }

    /**
     * Function that converts seconds to milliseconds
     * @param second, The second to be converted
     * @return second converted to milliseconds
     */
    fun secondToMilliSecond(second: Int): Int{
        return second * 1000
    }
}