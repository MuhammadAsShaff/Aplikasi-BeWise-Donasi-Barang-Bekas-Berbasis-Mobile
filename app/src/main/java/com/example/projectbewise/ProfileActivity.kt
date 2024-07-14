package com.example.projectbewise

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class   ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_voucher -> {
                    // start VoucherActivity
                    true
                }
                R.id.navigation_donation -> {
                    // start DonationActivity
                    true
                }
                R.id.navigation_history -> {
                    // start HistoryActivity
                    true
                }
                R.id.navigation_profile -> {
                    // start ProfileActivity or refresh
                    true
                }
                else -> false
            }
        }
    }
}
