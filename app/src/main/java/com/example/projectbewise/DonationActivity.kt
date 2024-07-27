package com.example.projectbewise

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class DonationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private lateinit var imageView: ImageView

    private lateinit var tanggalMulai: EditText
    private lateinit var tanggalSelesai: EditText
    private lateinit var jenisDonasiSpinner: Spinner
    private lateinit var jenisDonasiLainnya: EditText
    private lateinit var tempatDonasi: EditText

    private lateinit var googleMap: GoogleMap
    private var selectedLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_donation)

        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        val backButton: ImageView = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()
        }

        val addDonationButton: Button = findViewById(R.id.add_donation_button)
        addDonationButton.setOnClickListener {
            addDonation()
        }

        val uploadImageLayout: LinearLayout = findViewById(R.id.upload_image_layout)
        uploadImageLayout.setOnClickListener {
            selectImage()
        }

        imageView = findViewById(R.id.image_view)
        tanggalMulai = findViewById(R.id.tanggal_mulai)
        tanggalSelesai = findViewById(R.id.tanggal_selesai)
        jenisDonasiSpinner = findViewById(R.id.jenis_donasi_spinner)
        jenisDonasiLainnya = findViewById(R.id.jenis_donasi_lainnya)
        tempatDonasi = findViewById(R.id.tempat_donasi)

        tanggalMulai.setOnClickListener { showDatePickerDialog(tanggalMulai) }
        tanggalSelesai.setOnClickListener { showDatePickerDialog(tanggalSelesai) }

        setupJenisDonasiSpinner()

        // Initialize map
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.uiSettings.isZoomControlsEnabled = true

        // Default location (e.g., city center)
        val defaultLocation = LatLng(0.5071, 101.4478) // Pekanbaru, Indonesia
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))

        googleMap.setOnMapClickListener { location ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(location).title("Lokasi Donasi"))
            selectedLocation = location
        }
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = "$dayOfMonth/${month + 1}/$year"
                editText.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun setupJenisDonasiSpinner() {
        val jenisDonasiList = listOf(
            "Peralatan Kantor",
            "Pakaian",
            "Peralatan Tidur",
            "Peralatan Masak",
            "Buku",
            "Dan lain lain"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, jenisDonasiList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        jenisDonasiSpinner.adapter = adapter

        jenisDonasiSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (parent.getItemAtPosition(position) == "Dan lain lain") {
                    jenisDonasiLainnya.visibility = View.VISIBLE
                } else {
                    jenisDonasiLainnya.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                jenisDonasiLainnya.visibility = View.GONE
            }
        }
    }

    private fun addDonation() {
        val namaDonasi = findViewById<EditText>(R.id.nama_donasi).text.toString()
        val jenisDonasi = if (jenisDonasiLainnya.visibility == View.VISIBLE) {
            jenisDonasiLainnya.text.toString()
        } else {
            jenisDonasiSpinner.selectedItem.toString()
        }
        val tanggalMulaiStr = tanggalMulai.text.toString()
        val tanggalSelesaiStr = tanggalSelesai.text.toString()
        val deskripsiDonasi = findViewById<EditText>(R.id.deskripsi_donasi).text.toString()
        val tempatDonasiStr = tempatDonasi.text.toString()

        if (namaDonasi.isEmpty() || jenisDonasi.isEmpty() || tanggalMulaiStr.isEmpty() || tanggalSelesaiStr.isEmpty() || deskripsiDonasi.isEmpty() || tempatDonasiStr.isEmpty() || imageUri == null || selectedLocation == null) {
            Toast.makeText(this, "Harap isi semua kolom dan pilih lokasi pada peta", Toast.LENGTH_SHORT).show()
            return
        }

        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val tanggalMulai = format.parse(tanggalMulaiStr)?.time ?: 0L
        val tanggalSelesai = format.parse(tanggalSelesaiStr)?.time ?: 0L

        uploadImage { imageUrl ->
            val donasi = hashMapOf(
                "namaDonasi" to namaDonasi,
                "jenisDonasi" to jenisDonasi,
                "tanggalMulai" to Timestamp(Date(tanggalMulai)),
                "tanggalSelesai" to Timestamp(Date(tanggalSelesai)),
                "deskripsiDonasi" to deskripsiDonasi,
                "tempatDonasi" to tempatDonasiStr,
                "imageUrl" to imageUrl,
                "lokasiDonasi" to GeoPoint(selectedLocation!!.latitude, selectedLocation!!.longitude)
            )

            db.collection("donasi")
                .add(donasi)
                .addOnSuccessListener {
                    showSuccessPopup()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal menambahkan donasi", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun showSuccessPopup() {
        val builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.popup_donation_success, null)
        builder.setView(dialogView)

        val alertDialog = builder.create()
        alertDialog.show()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            alertDialog.dismiss()
            val intent = Intent(this, DonationListActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000) // Menunggu selama 3 detik
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImage(callback: (String) -> Unit) {
        if (imageUri != null) {
            val ref = storageReference.child("donasi/" + UUID.randomUUID().toString())
            ref.putFile(imageUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        callback(uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal mengupload gambar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
