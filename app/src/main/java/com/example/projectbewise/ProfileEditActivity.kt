package com.example.projectbewise

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProfileEditActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var imageUri: Uri? = null
    private var currentProfileImageUrl: String? = null

    private lateinit var fullNameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var profileImageView: ImageView
    private lateinit var saveButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var profileNameTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        fullNameEditText = findViewById(R.id.edit_full_name)
        ageEditText = findViewById(R.id.edit_age)
        phoneEditText = findViewById(R.id.edit_phone)
        emailEditText = findViewById(R.id.edit_email)
        addressEditText = findViewById(R.id.edit_address)
        profileImageView = findViewById(R.id.profile_image)
        saveButton = findViewById(R.id.save_button)
        backButton = findViewById(R.id.back_button)
        profileNameTextView = findViewById(R.id.profile_name)

        backButton.setOnClickListener {
            onBackPressed()
        }

        profileImageView.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

        loadUserProfile()

        saveButton.setOnClickListener {
            saveUserProfile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
        }
    }

    private fun loadUserProfile() {
        val userId = auth.currentUser?.uid
        userId?.let {
            db.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val fullName = document.getString("fullName")
                        profileNameTextView.text = fullName // Set the profile name TextView
                        fullNameEditText.setText(fullName)
                        ageEditText.setText(document.getLong("age")?.toString())

                        val phone = document.getLong("phone")?.toString()
                        phoneEditText.setText(phone)

                        emailEditText.setText(document.getString("email"))
                        addressEditText.setText(document.getString("address"))

                        // Load profile image using Glide
                        currentProfileImageUrl = document.getString("profileImageUrl")
                        if (!currentProfileImageUrl.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(currentProfileImageUrl)
                                .into(profileImageView)
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun saveUserProfile() {
        val userId = auth.currentUser?.uid
        userId?.let {
            val ageText = ageEditText.text.toString()
            val phoneText = phoneEditText.text.toString()

            if (ageText.isBlank() || !ageText.all { it.isDigit() }) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return
            }

            if (phoneText.isBlank() || !phoneText.all { it.isDigit() }) {
                Toast.makeText(this, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                return
            }

            val user = hashMapOf(
                "fullName" to fullNameEditText.text.toString(),
                "age" to ageText.toInt(),
                "phone" to phoneText.toLong(),
                "email" to emailEditText.text.toString(),
                "address" to addressEditText.text.toString(),
                "profileImageUrl" to (currentProfileImageUrl ?: "") // Retain the current profile image URL if not changing
            )

            db.collection("users").document(it).set(user)
                .addOnSuccessListener {
                    if (imageUri != null) {
                        uploadProfileImage(userId)
                    } else {
                        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Failed to update profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun uploadProfileImage(userId: String) {
        val ref = storage.reference.child("profileImages/$userId")
        imageUri?.let { uri ->
            ref.putFile(uri)
                .continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let { throw it }
                    }
                    ref.downloadUrl
                }
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUri = task.result
                        db.collection("users").document(userId).update("profileImageUrl", downloadUri.toString())
                            .addOnSuccessListener {
                                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                finish()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(this, "Failed to update profile image: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Toast.makeText(this, "Failed to upload profile image", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
