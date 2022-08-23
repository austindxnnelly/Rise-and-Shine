package com.example.alarmapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Class which allows app to access reboot status
 */
class myBroadcastReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        // after the reboot has been completed turn alarms back on
        if(intent.action.equals("android.intent.action.BOOT_COMPLETED")){
            //TODO: set alarm
            val ab = 2
        }
    }
}