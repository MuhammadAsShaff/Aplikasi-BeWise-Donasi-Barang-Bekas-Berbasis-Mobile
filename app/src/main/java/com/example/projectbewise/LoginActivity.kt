package com.example.projectbewise

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectbewise.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Login successful
                            val user = auth.currentUser
                            user?.let {
                                // Retrieve user data from Firestore
                                firestore.collection("users").document(user.uid).get()
                                    .addOnSuccessListener { document ->
                                        if (document != null) {
                                            val fullName = document.getString("fullName")
                                            val phone = document.getString("phone")
                                            // Use the user information as needed
                                            Toast.makeText(this, "Welcome $fullName", Toast.LENGTH_SHORT).show()
                                            // Navigate to main activity with user data
                                            val intent = Intent(this, MainActivity::class.java).apply {
                                                putExtra("fullName", fullName)
                                            }
                                            startActivity(intent)
                                            finish()
                                        } else {
                                            Toast.makeText(this, "No user data found", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            // If login fails, display a message to the user.
                            Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }

        binding.registerLink.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
