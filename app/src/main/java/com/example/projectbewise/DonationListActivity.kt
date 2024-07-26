package com.example.projectbewise

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DonationListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Implementasi logika untuk menampilkan daftar donasi
    }
}
