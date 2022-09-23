package com.example.alarmapp

import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.Canvas
import android.graphics.Typeface
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alarmapp.databinding.FragmentFirstBinding
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlin.properties.Delegates

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 * This screen is when you open the app, and your alarms are listed.
 *
 * @author Shay Stevens, Dougal Colquhoun, Liam Iggo, Austin Donnelly
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var alarm_ids = ArrayList<Int>()
    private var alarm_names = ArrayList<String>()
    private var alarm_hours = ArrayList<Int>()
    private var alarm_minutes = ArrayList<Int>()
    private lateinit var customAdapter : CustomAdapter
    private lateinit var holder: CustomAdapter.MyViewHolder

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
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    /**
    * Called immediately after onCreateView.
    * @param view, The View returned by onCreateView
    * @param savedInstanceState, re-constructed from a previous saved state
     */
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
        if(alarm_ids.size == 0){
            storeDataInArrays()
        }

        val recyclerView = binding.recyclerView
        customAdapter = CustomAdapter(context, alarm_ids, alarm_names, alarm_hours, alarm_minutes)
        recyclerView.adapter = customAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        swipeToDelete()
    }


    /**
     * Function that is called when View needs to be destroyed
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
    * function which stores the data (being the alarms) in an array
    * for view to a user
     */
    private fun storeDataInArrays(){
        val db = AlarmDatabase(context, "AlarmDatabase", null, 1)
        val cursor = db.readAllData()
        if(cursor?.count != 0){
            if (cursor != null) {
                while(cursor.moveToNext()){
                    alarm_ids.add(cursor.getInt(0))
                    alarm_names.add(cursor.getString(1))
                    alarm_hours.add(cursor.getInt(2))
                    alarm_minutes.add(cursor.getInt(3))
                }
            }
        }else{
            binding.emptyAlarmIV.visibility = View.VISIBLE
            binding.noAlarmTV.visibility = View.VISIBLE
        }
    }

    /**
    * Function that allows alarms to be removed from the app
    * by swiping them to the side.
     */
    private fun swipeToDelete(){
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            /**
             * Called on move.
             */
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            /**
             * Function called when swiping
             */
            @SuppressLint("NotifyDataSetChanged")
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                holder = customAdapter.getHolder()
                val position = viewHolder.adapterPosition
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
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        val db = AlarmDatabase(context, "AlarmDatabase", null, 1)
                        val id = alarm_ids.get(position)
                        db.deleteOneRow(id.toString())
                        //cancelling alarm
                        (activity as MainActivity).cancelAlarm(id)

                        alarm_ids.removeAt(position)
                        alarm_names.removeAt(position)
                        alarm_hours.removeAt(position)
                        alarm_minutes.removeAt(position)
                        customAdapter.notifyItemRemoved(position)
                        if (alarm_names.size == 0) {
                            if (alarm_names.size == 0) {
                                binding.emptyAlarmIV.visibility = View.VISIBLE
                                binding.noAlarmTV.visibility = View.VISIBLE
                            }
                        }
                    }
                    ItemTouchHelper.RIGHT -> {
                        val switch_states = customAdapter.switch_states()
                        val switch_state = switch_states[position]
                        val alarmName = alarm_names.get(position)
                        val alarm_hour = alarm_hours.get(position)
                        val alarm_min = alarm_minutes.get(position)
                        val dialog = EditDialog(alarmName, alarm_hour, alarm_min, timeString, switch_state)
                        fragmentManager?.let { dialog.show(it, "Edit") }
                        customAdapter.notifyDataSetChanged()
                    }
                }
            }

            /**
             * Drawing child
             */
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val deleteColor = context?.let { ContextCompat.getColor(it, R.color.deleteColor) }
                val editColor = context?.let { ContextCompat.getColor(it, R.color.editColor) }
                val labelColor = context?.let { ContextCompat.getColor(it, R.color.labelColor) }
                val deleteIcon = R.drawable.ic_baseline_delete_24
                val editIcon = R.drawable.ic_baseline_edit_24
                if (deleteColor != null) {
                    if (labelColor != null) {
                        if (editColor != null) {
                            RecyclerViewSwipeDecorator.Builder(
                                c,
                                recyclerView,
                                viewHolder,
                                dX,
                                dY,
                                actionState,
                                isCurrentlyActive
                            )
                                .addSwipeLeftBackgroundColor(deleteColor)
                                .addSwipeRightBackgroundColor(editColor)
                                .addSwipeLeftActionIcon(deleteIcon)
                                .addSwipeLeftLabel("Delete")
                                .addSwipeRightLabel("Edit")
                                .addSwipeRightActionIcon(editIcon)
                                .setSwipeLeftLabelColor(labelColor)
                                .setSwipeRightLabelColor(labelColor)
                                .setSwipeRightLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0F)
                                .setSwipeRightLabelTypeface(Typeface.DEFAULT_BOLD)
                                .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 22.0F)
                                .setSwipeLeftLabelTypeface(Typeface.DEFAULT_BOLD)
                                .create()
                                .decorate()
                        }
                    }
                }

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }).attachToRecyclerView(binding.recyclerView)
    }
}