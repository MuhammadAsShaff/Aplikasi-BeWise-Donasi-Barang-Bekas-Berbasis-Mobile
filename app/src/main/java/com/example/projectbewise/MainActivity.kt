package com.example.projectbewise

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
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
