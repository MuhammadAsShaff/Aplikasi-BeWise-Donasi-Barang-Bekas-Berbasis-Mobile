package com.example.projectbewise

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DonationListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        // Menghubungkan ImageView dengan ID back_button
        val backButton: ImageView = findViewById(R.id.back_button)

        // Menambahkan OnClickListener ke backButton
        backButton.setOnClickListener {
            onBackPressed()
        }
    }
}
