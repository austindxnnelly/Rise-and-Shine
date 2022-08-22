package com.example.alarmapp

import android.annotation.SuppressLint
import android.database.Cursor
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alarmapp.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var alarm_names = ArrayList<String>()
    private var alarm_hours = ArrayList<Int>()
    private var alarm_minutes = ArrayList<Int>()
    private lateinit var customAdapter : CustomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAlarmButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        val bundle = this.arguments
        val inputData = bundle?.getString("key")
        //if(inputData != null){
            //binding.test.text = inputData.toString()
        //}
        storeDataInArrays()
        val recyclerView = binding.recyclerView
        customAdapter = CustomAdapter(context, alarm_names, alarm_hours, alarm_minutes)
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun storeDataInArrays(){
        val db = AlarmDatabase(context, "AlarmDatabase", null, 1)
        val cursor = db.readAllData()
        if(cursor?.count != 0){
            if (cursor != null) {
                while(cursor.moveToNext()){
                    alarm_names.add(cursor.getString(1))
                    alarm_hours.add(cursor.getInt(2))
                    alarm_minutes.add(cursor.getInt(3))
                }
            }
        }

    }
}