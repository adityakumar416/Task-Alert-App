package com.example.taskalert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class StopAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Stop the alarm ringtone
        val alarmReceiver = AlarmReceiver()
        alarmReceiver.stopRingtone()
    }
}
