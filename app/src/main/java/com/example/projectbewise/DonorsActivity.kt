package com.example.projectbewise

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class DonorsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donors)

        // Handle back button
        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        // Set click listener for each donor card
        val donorListContainer: ViewGroup = findViewById(R.id.donor_list_container)
        for (i in 0 until donorListContainer.childCount) {
            val child = donorListContainer.getChildAt(i)
            child.setOnClickListener {
                val intent = Intent(this, ConfirmationDetailActivity::class.java)
                // Pass data if needed
                startActivity(intent)
            }
        }
    }
}
