package com.example.projectbewise

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar_include)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Retrieve full name from intent and set it to TextView
        val fullName = intent.getStringExtra("fullName")
        val userNameTextView = findViewById<TextView>(R.id.user_name)
        userNameTextView.text = fullName
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                // Handle home action
                return true
            }
            R.id.navigation_voucher -> {
                // Handle voucher action
                return true
            }
            R.id.navigation_donation -> {
                // Handle donation action
                return true
            }
            R.id.navigation_history -> {
                // Handle history action
                return true
            }
            R.id.navigation_profile -> {
                // Handle profile action
                return true
            }
        }
        return false
    }
}
