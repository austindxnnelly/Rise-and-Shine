package com.example.alarmapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.alarmapp.databinding.FragmentSecondBinding
import com.google.android.material.snackbar.Snackbar
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

    /**
    The screen that initially pops up when you click the + button
    @return the root of binding.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSecondBinding.inflate(inflater, container, false)

        binding.setTime.setOnClickListener{
            openTimePicker()
        }

        return binding.root

    }

    /**
    The screen that pops up when you go to select your preferred alarm time.
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
    Unsure here ...
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
                db.addAlarm(name, hour!!, minute!!)
            }

            (activity as MainActivity).setAlarm(hour!!, minute!!)

            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
        }
    }

    /**
    When the alarm is removed from the list.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}