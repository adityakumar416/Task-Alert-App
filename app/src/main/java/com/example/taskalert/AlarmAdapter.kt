package com.example.taskalert

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AlarmAdapter(private val alarms: ArrayList<Alarm>,private val onAlarmItemClickListener: OnAlarmItemClickListener, private val context: Context) : RecyclerView.Adapter<AlarmAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val dayMode: TextView = itemView.findViewById(R.id.dayMode)
        val alarm_item: ConstraintLayout = itemView.findViewById(R.id.alarm_item)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.alarm_item, parent, false)
        return ViewHolder(view)
    }
    private fun convertTo12HourFormat(hourOfDay: Int): Int {
        return if (hourOfDay > 12) {
            hourOfDay - 12
        } else if (hourOfDay == 0) {
            12
        } else {
            hourOfDay
        }
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alarm = alarms[position]
        val timeString = "${convertTo12HourFormat(alarm.hour)}:${String.format("%02d", alarm.minute)}"
        holder.timeTextView.text = timeString
        val amPm = if (alarm.hour >= 12) "PM" else "AM"
        holder.dayMode.text = amPm

        holder.alarm_item.setOnClickListener {
            onAlarmItemClickListener.onAlarmItemClick(alarm.id,alarm.hour,alarm.minute)
        }

        holder.alarm_item.setOnLongClickListener(View.OnLongClickListener {
            showDialog(alarm)
            true
        })


    }

    private fun showDialog(alarm: Alarm) {


        MaterialAlertDialogBuilder(context)
            .setTitle("Delete Alarm")
            .setMessage("Do you want to delete this Alarm ?")
            .setNegativeButton("No", object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog?.dismiss()
                }
            })
            .setPositiveButton("Yes",object : DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    val reference = FirebaseDatabase.getInstance().reference.child("alarms")

                    Toast.makeText(context, "Alarm deleted", Toast.LENGTH_SHORT).show()

                    reference.child(alarm.id).removeValue()
                    alarms.remove(alarm)


                }

            }).show()
    }


    interface OnAlarmItemClickListener {
         fun onAlarmItemClick(id: String, hour: Int, minute: Int)

    }

    override fun getItemCount(): Int {
        return alarms.size
    }

}
