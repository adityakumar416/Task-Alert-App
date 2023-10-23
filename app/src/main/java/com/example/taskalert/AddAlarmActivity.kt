package com.example.taskalert

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import java.util.*

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var btnSetAlarm: Button
    private lateinit var timePicker: TimePicker
    private lateinit var amTextView: TextView
    private lateinit var pmTextView: TextView
    private var alarmId: String? = null

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alarm)

        timePicker = findViewById(R.id.timePicker)
        btnSetAlarm = findViewById(R.id.buttonAlarm)
        amTextView = findViewById(R.id.amTextView)
        pmTextView = findViewById(R.id.pmTextView)

        alarmId = intent.getStringExtra("ALARM_ID")

        btnSetAlarm.setOnClickListener {
            val calendar = calculateAlarmTime()
                // Create new alarm logic here
                setAlarm(calendar.timeInMillis)
            }
    }



    private fun calculateAlarmTime(): Calendar {
        val calendar = Calendar.getInstance()
        if (Build.VERSION.SDK_INT >= 23) {
            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                timePicker.hour,
                timePicker.minute,
                0
            )
        } else {
            calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                timePicker.currentHour,
                timePicker.currentMinute,
                0
            )
        }
        val amPm = if (timePicker.currentHour >= 12) "PM" else "AM"

        return calendar
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun setAlarm(timeInMillis: Long) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("alarms")
        val alarmId = databaseReference.push().key
        val isAM = timePicker.currentHour < 12

        val alarm = Alarm(
            id = alarmId.toString(),
            hour = timePicker.currentHour,
            minute = timePicker.currentMinute,
           /* isAM = if (timePicker.currentHour >= 12) "PM" else "AM"*/
        )
        alarmId?.let {
            databaseReference.child(it).setValue(alarm)
                .addOnSuccessListener {
                    // Alarm saved successfully
                    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(this, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)

                    AlarmNotification().showNotification(this, "Alarm Set", "Alarm set for ${timePicker.currentHour}:${timePicker.currentMinute}")

                    Toast.makeText(this, "Alarm set successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    // Error occurred while saving the alarm
                    Toast.makeText(this, "Failed to set alarm!", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
