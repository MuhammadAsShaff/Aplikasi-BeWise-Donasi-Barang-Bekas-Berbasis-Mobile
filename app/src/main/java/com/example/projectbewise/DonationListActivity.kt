package com.example.projectbewise

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.projectbewise.adapter.DonationAdapter
import com.example.projectbewise.Donasi
import com.google.firebase.firestore.FirebaseFirestore

class DonationListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var searchEditText: EditText
    private lateinit var backButton: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_list)

        searchEditText = findViewById(R.id.search_bar)
        recyclerView = findViewById(R.id.recycler_view)
        backButton = findViewById(R.id.back_button)

        recyclerView.layoutManager = LinearLayoutManager(this)
        donationAdapter = DonationAdapter(emptyList(), this)
        recyclerView.adapter = donationAdapter

        db = FirebaseFirestore.getInstance()
        loadDonations()

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                donationAdapter.filter.filter(s)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Tambahkan listener untuk tombol back
        backButton.setOnClickListener {
            onBackPressed()
        }
    }

    private fun loadDonations() {
        db.collection("donasi")
            .get()
            .addOnSuccessListener { result ->
                val donations = result.toObjects(Donasi::class.java)
                donationAdapter.updateDonations(donations)
            }
            .addOnFailureListener { exception ->
                // Handle any errors
            }
    }
}
