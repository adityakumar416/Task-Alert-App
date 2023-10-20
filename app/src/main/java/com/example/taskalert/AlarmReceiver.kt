package com.example.taskalert

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import androidx.appcompat.app.AlertDialog

class AlarmReceiver : BroadcastReceiver() {

    private var ringtone: Ringtone? = null

    override fun onReceive(context: Context, intent: Intent) {
        // Play the alarm ringtone
        val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
        ringtone?.play()

        // You can perform other actions here when the alarm triggers
    }


    fun stopRingtone() {
        ringtone?.stop()
    }
}
