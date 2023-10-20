package com.example.taskalert

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.taskalert.databinding.ActivityCreateNoteBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNoteBinding
    private lateinit var database: DatabaseReference
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNoteBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()
        val onlineUserId = mAuth!!.currentUser?.uid.toString()
        database = FirebaseDatabase.getInstance().reference.child("note").child(onlineUserId)

        binding.btnSaveNote.setOnClickListener {

            val notes: String = binding.tvNoteCr.text.toString()

            if (notes.isEmpty()) {
                binding.tvNoteCr.error = "Note Required!"
            }else{

                val title = binding.tvTitleCr.text.toString()
                val note = binding.tvNoteCr.text.toString()
                val id: String? = database.push().key

                val data = Data(title,note,id)
                if (id != null) {
                    database.child(id).setValue(data).addOnSuccessListener {
                        Toast.makeText(this, "Saved successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                        finish()
                    }.addOnFailureListener{
                        Toast.makeText(this,"failed to save",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.alarm.setOnClickListener {
            val intent = Intent(this,AlarmActivity::class.java)
            startActivity(intent)
        }

    }
}