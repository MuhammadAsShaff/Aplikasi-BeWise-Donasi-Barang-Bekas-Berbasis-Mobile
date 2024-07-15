package com.example.projectbewise

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : FragmentActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donation_confirmation) // Pastikan ini sesuai dengan layout Anda

        // Inisialisasi SupportMapFragment
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Tambahkan marker di lokasi yang diinginkan dan geser kamera
        val lokasi = LatLng(-6.200000, 106.816666) // Koordinat Jakarta, ganti sesuai kebutuhan
        mMap!!.addMarker(MarkerOptions().position(lokasi).title("Marker in Jakarta"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(lokasi, 15f))
    }
}

