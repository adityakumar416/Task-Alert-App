package com.example.taskalert

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.*

class AlarmActivity : AppCompatActivity() {

    private lateinit var ringtone: Ringtone


    @SuppressLint("ScheduleExactAlarm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val hourPicker: NumberPicker = findViewById(R.id.hourPicker)
        val minutePicker: NumberPicker = findViewById(R.id.minutePicker)

        // Set the range for hourPicker
        hourPicker.minValue = 0
        hourPicker.maxValue = 23
        // Set wrapSelectorWheel property
        hourPicker.wrapSelectorWheel = true

        // Set the range for minutePicker
        minutePicker.minValue = 0
        minutePicker.maxValue = 59
        // Set wrapSelectorWheel property
        minutePicker.wrapSelectorWheel = true

        val setAlarmButton: Button = findViewById(R.id.setAlarmButton)
        setAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hourPicker.value)
            calendar.set(Calendar.MINUTE, minutePicker.value)

            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(this, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE)


            val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            ringtone = RingtoneManager.getRingtone(this, ringtoneUri)

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            Toast.makeText(this, "Alarm set successfully!", Toast.LENGTH_SHORT).show()
            AlarmNotification().showNotification(this, "Alarm Set", "Alarm set for ${hourPicker.value}:${minutePicker.value}")




        }
    }

 
}