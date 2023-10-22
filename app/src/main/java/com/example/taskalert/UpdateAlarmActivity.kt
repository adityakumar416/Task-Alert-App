package com.example.taskalert

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UpdateAlarmActivity : AppCompatActivity() {
    private lateinit var btnSetAlarm: Button
    private lateinit var timePicker: TimePicker
    private lateinit var amTextView: TextView
    private lateinit var pmTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_alarm)


        timePicker = findViewById(R.id.timePicker)
        btnSetAlarm = findViewById(R.id.buttonAlarm)
        amTextView = findViewById(R.id.amTextView)
        pmTextView = findViewById(R.id.pmTextView)

        val alarmId = intent.getStringExtra("ALARM_ID")
        val hour = intent.getIntExtra("hour",0)
        val minute = intent.getIntExtra("minute",0)
        val am = intent.getBooleanExtra("am",true)


        timePicker.hour = hour
        timePicker.minute = minute

        if (am==true) {
            amTextView.text = "AM"
            pmTextView.text = "" // Clear PM text
        } else {
            amTextView.text = "" // Clear AM text
            pmTextView.text = "PM"
        }
        btnSetAlarm.setOnClickListener {



            val updatedAlarm = Alarm(
                id = alarmId!!,
                hour = timePicker.hour,
                minute = timePicker.minute,
                isAM = timePicker.hour < 12
            )

            val reference = FirebaseDatabase.getInstance().reference.child("alarms")
            reference.child(alarmId.toString()).setValue(updatedAlarm).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,AlarmActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                    finish()
                } else {
                    Toast.makeText(this, "failed to update " + task.exception, Toast.LENGTH_SHORT).show()
                }
            }

        }


    }

}