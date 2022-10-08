package com.example.alarmapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.my_row.view.*
import kotlin.properties.Delegates

/**
 * Custom adapter for the alarm database. The adapter acts as a bridge between the
 * database and UI. It converts data from the database to a UI component.
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

    private lateinit var parent: ViewGroup
    private  lateinit var holder: MyViewHolder
    private var switch_states = ArrayList<Boolean>()
    private var database_states = ArrayList<Int>()
    private var count = 0

    /**
    * Creates a new view holder when there is no existing view holders
    * @param parent, The recyclerView group
    * @param viewType, Set of views
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.my_row, parent, false)
        this.parent = parent
        return MyViewHolder(view)
    }

    /**
    * Sets the correct time of the alarm.
    * Uses 24 hour format in order to do so.
    * @param holder, item view and metadata about its place within the RecyclerView
    * @param position, The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        when (context?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
            Configuration.UI_MODE_NIGHT_YES -> {
                val backgroundcolor = context?.let { ContextCompat.getColor(it, R.color.background) }
                val textcolor = context?.let { ContextCompat.getColor(it, R.color.night_name) }
                if (backgroundcolor != null) {
                    holder.itemView.row.setBackgroundColor(backgroundcolor)
                }
                if (textcolor != null) {
                    holder.itemView.alarm_time.setTextColor(textcolor)
                }
            }
        }
        addStatesToArray()
        val state = database_states[position] == 1
        holder.switch.isChecked = state

        val timeString : String
        val isSystem24Hour = DateFormat.is24HourFormat(context)
        val hour = alarm_hours[position]
        val minute = alarm_minutes[position]
        val minuteString = if(minute < 10) "0$minute" else "$minute"

        if(!isSystem24Hour){
            val meridiemIndicator = if(hour > 11) "PM" else "AM"
            val hourString = if(hour == 0) "12" else if(hour > 12) hour - 12 else "$hour"
            timeString = "$hourString:$minuteString $meridiemIndicator"
        }else{
            val hourString = if(hour < 10) "0$hour" else "$hour"
            timeString = "$hourString:$minuteString"
        }

        if(alarm_names[position] != ""){
            holder.alarmTime.text = alarm_names[position]
        }else{
            holder.alarmTime.text = timeString
        }

        this.holder = holder
        switch_states.add(holder.switch.isChecked)
        holder.switch.setOnClickListener {
            switch_states[position] = holder.switch.isChecked
            var s = 0
            if(holder.switch.isChecked){
                s = 1
            }
            val db = AlarmDatabase(context, "AlarmDatabase", null, 1)
            db.updateDatabase(alarm_ids.get(position).toString(), alarm_names.get(position), alarm_hours.get(position), alarm_minutes.get(position), s)
            addStatesToArray()
            database_states.clear()
        }
    }

    /**
     * Function that adds the switch states stored in the database into an array.
     */
    fun addStatesToArray() {
        val db = AlarmDatabase(context, "AlarmDatabase", null, 1)
        val cursor = db.readAllData()
        if(cursor?.count != 0){
            if (cursor != null) {
                while(cursor.moveToNext()){
                    database_states.add(cursor.getInt(4))
                }
            }
        }
    }

    /**
    * Gets the count of the items in the database
    * @return alarm names.size, the amount of alarms in a users app.
     */
    override fun getItemCount(): Int {
        return alarm_names.size
    }

    /**
     * returns the holder.
     */
    fun getHolder(): MyViewHolder {
        return holder
    }

    /**
     * returns the switch states array.
     */
    fun switch_states(): ArrayList<Boolean> {
        return switch_states
    }

    /**
     * MyViewHolder is a custom class which describes an item view and metadata
     * about its place within the RecyclerView
     */
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmName: TextView = itemView.findViewById(R.id.alarm_name)
        val alarmTime: TextView = itemView.findViewById(R.id.alarm_time)
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        val switch: Switch = itemView.findViewById(R.id.alarm_switch)
    }

}