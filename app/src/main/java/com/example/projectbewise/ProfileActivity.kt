package com.example.projectbewise

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var profileImageView: ImageView
    private lateinit var profileNameTextView: TextView
    private lateinit var profileEmailTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        profileImageView = findViewById(R.id.profile_image)
        profileNameTextView = findViewById(R.id.profile_name)
        profileEmailTextView = findViewById(R.id.profile_email)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.navigation_bar_include)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    true
                }
                R.id.navigation_pengiriman -> {
                    startActivity(Intent(this, DonorsActivity::class.java))
                    true
                }
                R.id.navigation_donation -> {
                    startActivity(Intent(this, DonationActivity::class.java))
                    true
                }
                R.id.navigation_history -> {
                    startActivity(Intent(this, HistoryActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    // Stay on the current activity
                    true
                }
                else -> false
            }
        }

        // Handle Edit Profile
        findViewById<android.widget.LinearLayout>(R.id.edit_profile).setOnClickListener {
            startActivity(Intent(this, ProfileEditActivity::class.java))
        }

        // Handle About Us
        findViewById<android.widget.LinearLayout>(R.id.about_us).setOnClickListener {
            // Start About Us Activity (Replace AboutUsActivity with the actual activity class)
            startActivity(Intent(this, AboutActivity::class.java))
        }

        // Handle FAQ
        findViewById<android.widget.LinearLayout>(R.id.faq).setOnClickListener {
            // Start FAQ Activity (Replace FAQActivity with the actual activity class)
            startActivity(Intent(this, FAQActivity::class.java))
        }

        // Handle Logout
        findViewById<android.widget.LinearLayout>(R.id.logout).setOnClickListener {
            auth.signOut()
            // Start Login Activity (Replace LoginActivity with the actual activity class)
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        loadUserProfile()
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile() // Refresh profile data when returning to this activity
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val fullName = document.getString("fullName")
                        val email = document.getString("email")
                        val profileImageUrl = document.getString("profileImageUrl")

                        profileNameTextView.text = fullName
                        profileEmailTextView.text = email

                        if (!profileImageUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(profileImageUrl)
                                .circleCrop() // Menampilkan gambar bulat
                                .into(profileImageView)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
