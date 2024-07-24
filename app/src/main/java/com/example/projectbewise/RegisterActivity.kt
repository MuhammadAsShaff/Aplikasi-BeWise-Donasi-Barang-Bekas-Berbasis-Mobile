package com.example.projectbewise

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectbewise.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.registerButton.setOnClickListener {
            val fullName = binding.fullName.text.toString()
            val email = binding.email.text.toString()
            val phone = binding.phone.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() && password == confirmPassword) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Registration successful
                            val user = auth.currentUser
                            val userId = user?.uid

                            // Save additional user information to Firebase Firestore
                            val userInfo = User(fullName, email, phone)
                            userId?.let {
                                firestore.collection("users").document(it).set(userInfo)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()
                                        // Navigate to login activity or other destination
                                        finish()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to save user information", Toast.LENGTH_SHORT).show()
                                    }
                            }
                        } else {
                            // If registration fails, display a message to the user.
                            Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill out all fields correctly", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginLink.setOnClickListener {
            finish() // Finish this activity to go back to login activity
        }
    }
}

// User data class to store user information in Firebase Firestore
data class User(val fullName: String, val email: String, val phone: String)
