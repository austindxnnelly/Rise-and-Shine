package com.example.alarmapp

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * Acts as a foundation for the alarm database.
 * Everytime you click confirm on an alarm, it adds that alarm to the database.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class CustomAdapter(
    private var context: Context?,
    private var alarm_ids: ArrayList<Int>,
    private var alarm_names: ArrayList<String>,
    private var alarm_hours: ArrayList<Int>,
    private var alarm_minutes: ArrayList<Int>
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {


    /**
    * Unsure here
    * @param
    * @param
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }

    /**
    * Sets the correct time of the alarm.
    * Uses 24 hour format in order to do so.
    * @param
    * @param
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        /*if(alarm_names[position] == ""){
            holder.alarmName.text = "A" + alarm_ids[position]
        }else{
            holder.alarmName.text = alarm_names[position]
        }*/
        holder.alarmName.text = ""

        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val hour = alarm_hours[position]
        val minute = alarm_minutes[position]
        val minuteString = if(minute < 10) "0$minute" else "$minute"

        if(!isSystem24Hour){
            val meridiemIndicator = if(hour > 11) "PM" else "AM"
            val hourString = if(hour == 0) "12" else if(hour > 12) hour - 12 else "$hour"
            val timeString = "$hourString:$minuteString $meridiemIndicator"
            holder.alarmTime.text = timeString
        }else{
            val hourString = if(hour < 10) "0$hour" else "$hour"
            val timeString = "$hourString:$minuteString"
            holder.alarmTime.text = timeString
        }
    }

    /**
    * Gets the count of the items in the database
    * @return alarm names.size, the amount of alarms in a users app.
     */
    override fun getItemCount(): Int {
        return alarm_names.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmName: TextView = itemView.findViewById(R.id.alarm_name)
        val alarmTime: TextView = itemView.findViewById(R.id.alarm_time)
    }

}