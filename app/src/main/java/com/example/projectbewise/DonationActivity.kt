package com.example.projectbewise

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DonationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_donation)

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Setup any necessary logic for donation page
    }
}
