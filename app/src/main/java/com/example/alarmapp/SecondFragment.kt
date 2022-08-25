package com.example.alarmapp

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.example.alarmapp.databinding.FragmentSecondBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var hour: Int? = null
    private var minute: Int? = null

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

    private fun openTimePicker(){
        //True if in military time, false if using 12 hours
        val isSystem24Hour = is24HourFormat(requireContext())
        val clockFormat =  if(isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val rightNow = Calendar.getInstance()
        val currentHour = if(isSystem24Hour) rightNow.get(Calendar.HOUR_OF_DAY) else rightNow.get(Calendar.HOUR)
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
            val meridiemIndicator = if(hour > 11) "PM" else "AM"
            val minuteString = if(minute < 10) "0$minute" else "$minute"
            val hourString = if(hour == 0) "12" else if(hour > 12) hour - 12 else "$hour"
            binding.setTime.hint = "$hourString:$minuteString $meridiemIndicator"
            this.hour = hour
            this.minute = minute
        }
    }

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
            //new
            val alarm = Alarm(name!!, hour!!, minute!!)
            alarm.setAlarm()
//            val message = alarm.createMessage()
            /*val message = "Alarm $name created! At $hour:$minute"
            val mySnackBar = Snackbar.make(
                requireActivity().findViewById(R.id.recyclerView),
                message,
                Snackbar.LENGTH_LONG
            )
            mySnackBar.show()*/
            //new end

            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}