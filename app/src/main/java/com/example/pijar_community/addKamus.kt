package com.example.pijar_community

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class addKamus : AppCompatActivity() {

    private lateinit var Huruf: EditText
    private lateinit var jenis: Spinner
    private lateinit var gambar: Button
    private lateinit var kirim: Button
    private lateinit var firebase: DatabaseReference

    private var selectedImageUri: Uri? = null

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the selected image URI here
        if (uri != null) {
            selectedImageUri = uri
            // You can use the 'selectedImageUri' to work with the selected image
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_kamus)

        Huruf = findViewById(R.id.huruf)
        jenis = findViewById(R.id.jenis)
        gambar= findViewById(R.id.pilih)
        kirim = findViewById(R.id.kirim)
        firebase = FirebaseDatabase.getInstance().getReference("kamus")

        val jenisKamus = resources.getStringArray(R.array.jenis)
        val jenisAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisKamus)
        jenisAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenis.adapter = jenisAdapter

        gambar.setOnClickListener {
            // Open the gallery to select an image
            getContent.launch("image/*")
        }

        kirim.setOnClickListener {
            kirimdata()
        }

    }

    private fun kirimdata() {
        val huruf = Huruf.text.toString()
        val JenisKMS = jenis.selectedItem.toString()


        val id =firebase.push().key!!
        val data = kamus(id, huruf, JenisKMS)

        firebase.child(id).setValue(data)
            .addOnCompleteListener {
                Toast.makeText(this,"Kirim",Toast.LENGTH_SHORT).show()
            }

        if (selectedImageUri != null) {
            // Kirim URL gambar ke Firebase Realtime Database

            firebase.child(id).setValue(selectedImageUri.toString())
        }


    }
}