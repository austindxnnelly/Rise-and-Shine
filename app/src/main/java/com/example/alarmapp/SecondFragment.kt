package com.example.alarmapp

import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.alarmapp.databinding.FragmentSecondBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 * This part is used when adding a new alarm using the + button
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var hour: Int? = null
    private var minute: Int? = null
    private var alarm_ids = ArrayList<Int>()

    /**
     * Called when View is created. The view will later be terminated .
     * @param inflater, converts XML to a View
     * @param container, parent view that the Fragment's UI should be attached to.
     * @param savedInstanceState, reconstructed by previous state
     *
     * @return The View for the Fragment's UI, or null.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        binding.setTime.setOnClickListener{
            openTimePicker()
        }

        return binding.root
    }

    /**
    * The screen that pops up when you go to select your preferred alarm time.
     */
    private fun openTimePicker(){
        //True if in military time, false if using 12 hours
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat =  if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val rightNow = Calendar.getInstance()
        val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)

        val timePicker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(currentHour)
            .setMinute(currentMinute)
            .setTitleText("Select Time")
            .build()

        timePicker.show(childFragmentManager, "TAG")
        timePicker.addOnPositiveButtonClickListener{
            //Range 0-23
            val hour = timePicker.hour
            //Range 0-59
            val minute = timePicker.minute

            val minuteString = if(minute < 10) "0$minute" else "$minute"
            if(!isSystem24Hour){
                val meridiemIndicator = if(hour > 11) "PM" else "AM"
                val hourString = if(hour == 0) "12" else if(hour > 12) hour - 12 else "$hour"
                binding.setTime.hint = "$hourString:$minuteString $meridiemIndicator"
            }else{
                val hourString = if(hour < 10) "0$hour" else "$hour"
                binding.setTime.hint = "$hourString:$minuteString"
            }

            this.hour = hour
            this.minute = minute
        }
    }

    /**
     * Called immediately after onCreateView.
     * @param view, The View returned by onCreateView
     * @param savedInstanceState, re-constructed from a previous saved state
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            val time = binding.setTime.hint.toString()

            val bundle = Bundle()
            bundle.putString("key", time)

            val db = AlarmDatabase(context, "AlarmDatabase", null, 1)

            if(hour == null){
                val rightNow = Calendar.getInstance()
                val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
                val currentMinute = rightNow.get(Calendar.MINUTE)
                hour = currentHour
                minute = currentMinute
            }

            val name: String = binding.addAlarmName.text.toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                db.addAlarm(name, hour!!, minute!!, 1)
            }

            val cursor = db.readAllData()
            if(cursor?.count != 0){
                if (cursor != null) {
                    while(cursor.moveToNext()){
                        alarm_ids.add(cursor.getInt(0))
                    }
                }
            }

            val id = alarm_ids[alarm_ids.size-1]
            (activity as MainActivity).setAlarm(hour!!, minute!!, id, name)

            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
        }
    }

    /**
     * Function that is called when View needs to be destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}