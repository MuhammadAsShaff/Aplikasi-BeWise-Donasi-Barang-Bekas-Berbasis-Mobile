package com.example.projectbewise

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.GeoPoint

data class Donasi(
    @DocumentId val id: String = "",
    val namaDonasi: String = "",
    val tempatDonasi: String = "",
    val imageUrl: String = "",
    val tanggalMulai: Timestamp = Timestamp.now(),
    val tanggalSelesai: Timestamp = Timestamp.now(),
    val deskripsiDonasi: String = "",
    val pelaporan: String = "",
    val lokasiDonasi: GeoPoint = GeoPoint(0.0, 0.0)
)
