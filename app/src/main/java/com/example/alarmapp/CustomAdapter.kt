package com.example.alarmapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CustomAdapter(
    private var context: Context?,
    private var alarm_names: ArrayList<String>,
    private var alarm_hours: ArrayList<Int>,
    private var alarm_minutes: ArrayList<Int>
) : RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.alarmName.text = alarm_names[position]
    }

    override fun getItemCount(): Int {
        return alarm_names.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val alarmName: TextView = itemView.findViewById(R.id.alarm_name)
    }

}