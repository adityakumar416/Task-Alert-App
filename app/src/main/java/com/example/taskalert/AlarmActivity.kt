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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class AlarmActivity : AppCompatActivity(), AlarmAdapter.OnAlarmItemClickListener {

    private lateinit var alarmsRecyclerView: RecyclerView
    private val alarms: ArrayList<Alarm> = arrayListOf()
    private lateinit var adapter: AlarmAdapter

    @SuppressLint("ScheduleExactAlarm", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        val setAlarmButton: Button = findViewById(R.id.setAlarmButton)
        setAlarmButton.setOnClickListener {
            val intent = Intent(this, AddAlarmActivity::class.java)
            startActivity(intent)
        }

        alarmsRecyclerView = findViewById(R.id.alarmsRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        alarmsRecyclerView.layoutManager = layoutManager
        val onAlarmItemClickListener: AlarmAdapter.OnAlarmItemClickListener = this

        // Initialize the adapter only once
        adapter = AlarmAdapter(alarms, onAlarmItemClickListener,this)
        alarmsRecyclerView.adapter = adapter

        val databaseReference = FirebaseDatabase.getInstance().reference.child("alarms")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing alarms list
                alarms.clear()
                if (snapshot.exists()) {
                    // Populate the alarms list with data from Firebase
                    for (alarmSnapshot in snapshot.children) {
                        val alarm = alarmSnapshot.getValue(Alarm::class.java)
                        alarm?.let { alarms.add(it) }
                    }

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }


        override fun onAlarmItemClick(id: String, hour: Int, minute: Int, am: Boolean) {
        val intent = Intent(this, UpdateAlarmActivity::class.java)
        intent.putExtra("ALARM_ID", id)
        intent.putExtra("hour",hour)
        intent.putExtra("minute",minute)
        intent.putExtra("am",am)
        startActivity(intent)
    }
}


/*
private fun updateAlarmInFirebase(alarmId: String, hour: Int, minute: Int,am:Boolean, timeInMillis: Long) {
    val isAM = timePicker.currentHour < 12
    val alarm = Alarm(
        id = alarmId,
        hour =hour,
        minute = minute,
        isAM = am
    )
    val databaseReference = FirebaseDatabase.getInstance().reference.child("alarms")

    databaseReference.child(alarmId).setValue(alarm)
        .addOnSuccessListener {
            // Alarm updated successfully
            setAlarm(timeInMillis)
            Toast.makeText(this, "Alarm updated successfully!", Toast.LENGTH_SHORT).show()
            finish()
        }
        .addOnFailureListener {
            // Error occurred while updating the alarm
            Toast.makeText(this, "Failed to update alarm!", Toast.LENGTH_SHORT).show()
        }
}
*/




/*
        alarmsRecyclerView = findViewById(R.id.alarmsRecyclerView)
        val layoutManager = LinearLayoutManager(this)
        alarmsRecyclerView.layoutManager = layoutManager
        val onAlarmItemClickListener: AlarmAdapter.OnAlarmItemClickListener = this

        val databaseReference = FirebaseDatabase.getInstance().reference.child("alarms")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val alarmsList = mutableListOf<Alarm>()
                for (alarmSnapshot in snapshot.children) {
                    val alarm = alarmSnapshot.getValue(Alarm::class.java)
                    alarm?.let { alarmsList.add(it) }
                }

                val adapter = AlarmAdapter(alarmsList, onAlarmItemClickListener)
                alarmsRecyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })




        val setAlarmButton: Button = findViewById(R.id.setAlarmButton)
        setAlarmButton.setOnClickListener {

            val intent = Intent(this,AddAlarmActivity::class.java)
            startActivity(intent)

        }



    }

    override fun onAlarmItemClick(alarmId: String) {
        val intent = Intent(this, AddAlarmActivity::class.java)
        intent.putExtra("ALARM_ID", alarmId)
        startActivity(intent)
    }


}*/









/*
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




        }*/