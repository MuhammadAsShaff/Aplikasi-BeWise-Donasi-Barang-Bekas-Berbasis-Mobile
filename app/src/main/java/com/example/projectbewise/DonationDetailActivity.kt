package com.example.projectbewise

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import java.util.*

class DonationDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var db: FirebaseFirestore
    private lateinit var donationId: String
    private var googleMap: GoogleMap? = null
    private var lokasiDonasi: GeoPoint? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_detail)

        db = FirebaseFirestore.getInstance()

        val backButton: ImageView = findViewById(R.id.back_button)
        val donationImage: ImageView = findViewById(R.id.donation_image)
        val namaDonasiTextView: TextView = findViewById(R.id.nama_donasi_text_view)
        val tempatDonasiTextView: TextView = findViewById(R.id.tempat_donasi_text_view)
        val sisaHariTextView: TextView = findViewById(R.id.sisa_hari_text_view)
        val deskripsiDonasiTextView: TextView = findViewById(R.id.deskripsi_donasi_text_view)
        val progressBar: ProgressBar = findViewById(R.id.progress_bar)
        val pelaporanEditText: EditText = findViewById(R.id.pelaporan_edit_text)
        val kirimButton: Button = findViewById(R.id.kirim_button)

        backButton.setOnClickListener {
            onBackPressed()
        }

        donationId = intent.getStringExtra("donation_id") ?: ""

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        loadDonationDetails(donationId, donationImage, namaDonasiTextView, tempatDonasiTextView, sisaHariTextView, deskripsiDonasiTextView, progressBar, pelaporanEditText)

        kirimButton.setOnClickListener {
            val pelaporan = pelaporanEditText.text.toString()
            if (pelaporan.isNotEmpty()) {
                submitPelaporan(donationId, pelaporan)
            }
        }
    }

    private fun loadDonationDetails(donationId: String, donationImage: ImageView, namaDonasiTextView: TextView, tempatDonasiTextView: TextView,
                                    sisaHariTextView: TextView, deskripsiDonasiTextView: TextView, progressBar: ProgressBar, pelaporanEditText: EditText) {
        db.collection("donasi").document(donationId).get().addOnSuccessListener { document ->
            if (document != null) {
                val imageUrl = document.getString("imageUrl")
                val namaDonasi = document.getString("namaDonasi")
                val tempatDonasi = document.getString("tempatDonasi")
                val deskripsiDonasi = document.getString("deskripsiDonasi")
                val tanggalMulai = document.getTimestamp("tanggalMulai")?.toDate()
                val tanggalSelesai = document.getTimestamp("tanggalSelesai")?.toDate()
                val pelaporan = document.getString("pelaporan")
                lokasiDonasi = document.getGeoPoint("lokasiDonasi")

                Glide.with(this).load(imageUrl).into(donationImage)
                namaDonasiTextView.text = namaDonasi
                tempatDonasiTextView.text = tempatDonasi
                deskripsiDonasiTextView.text = deskripsiDonasi
                pelaporanEditText.setText(pelaporan ?: "")

                if (tanggalMulai != null && tanggalSelesai != null) {
                    val currentDate = Date()
                    val diff: Long = tanggalSelesai.time - currentDate.time
                    val daysLeft = (diff / (1000 * 60 * 60 * 24)).toInt()
                    sisaHariTextView.text = "Sisa Hari $daysLeft"

                    val totalDays = ((tanggalSelesai.time - tanggalMulai.time) / (1000 * 60 * 60 * 24)).toInt()
                    val progress = ((totalDays - daysLeft) / totalDays.toFloat() * 100).toInt()
                    progressBar.progress = progress
                }

                // Set map location
                googleMap?.let {
                    lokasiDonasi?.let { geoPoint ->
                        val latLng = LatLng(geoPoint.latitude, geoPoint.longitude)
                        it.addMarker(MarkerOptions().position(latLng).title("Lokasi Donasi"))
                        it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    }
                }
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isScrollGesturesEnabled = false
        googleMap?.uiSettings?.isZoomGesturesEnabled = false
        googleMap?.uiSettings?.isTiltGesturesEnabled = false
        googleMap?.uiSettings?.isRotateGesturesEnabled = false

        // Set map location if already loaded
        lokasiDonasi?.let { geoPoint ->
            val latLng = LatLng(geoPoint.latitude, geoPoint.longitude)
            googleMap?.addMarker(MarkerOptions().position(latLng).title("Lokasi Donasi"))
            googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        }
    }

    private fun submitPelaporan(donationId: String, pelaporan: String) {
        db.collection("donasi").document(donationId).update("pelaporan", pelaporan)
            .addOnSuccessListener {
                // Handle successful update
            }
            .addOnFailureListener {
                // Handle update failure
            }
    }
}

