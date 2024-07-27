package com.example.projectbewise

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var userNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigation_bar_include)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        userNameTextView = findViewById(R.id.user_name)

        // Load user name when activity is created
        loadUserName()
    }

    override fun onResume() {
        super.onResume()
        // Reload user name when activity resumes
        loadUserName()
    }

    private fun loadUserName() {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val fullName = document.getString("fullName")
                        userNameTextView.text = fullName ?: "User Name" // Default to "User Name" if fullName is null
                    }
                }
                .addOnFailureListener { exception ->
                    userNameTextView.text = "User Name" // Set default name on failure
                }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                // Handle home action
                return true
            }
            R.id.navigation_pengiriman -> {
                val intent = Intent(this, DonorsActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.navigation_donation -> {
                val intent = Intent(this, DonationActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.navigation_history -> {
                val intent = Intent(this, DonationListActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.navigation_profile -> {
                val intent = Intent(this, ProfileActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return false
    }
}
