package com.example.projectbewise

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationItemView

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Menyesuaikan ukuran teks dan jenis font secara manual
        val menuView = bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        for (i in 0 until menuView.childCount) {
            val itemView = menuView.getChildAt(i) as BottomNavigationItemView
            val smallLabel = itemView.findViewById<TextView>(com.google.android.material.R.id.smallLabel)
            val largeLabel = itemView.findViewById<TextView>(com.google.android.material.R.id.largeLabel)

            smallLabel.textSize = 12f // Ukuran teks untuk item tidak aktif
            smallLabel.typeface = resources.getFont(R.font.poppins_bold) // Jenis font untuk item tidak aktif
            largeLabel.textSize = 14f // Ukuran teks untuk item aktif
            largeLabel.typeface = resources.getFont(R.font.poppins_bold) // Jenis font untuk item aktif
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                // Handle home action
                return true
            }
            R.id.navigation_voucher -> {
                // Handle voucher action
                return true
            }
            R.id.navigation_donation -> {
                // Handle donation action
                return true
            }
            R.id.navigation_history -> {
                // Handle history action
                return true
            }
            R.id.navigation_profile -> {
                // Handle profile action
                return true
            }
        }
        return false
    }
}
