package com.example.projectbewise.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.projectbewise.DonationDetailActivity
import com.example.projectbewise.R
import com.example.projectbewise.Donasi
import java.util.*
import kotlin.collections.ArrayList

class DonationAdapter(
    private var donations: List<Donasi>,
    private val context: Context
) : RecyclerView.Adapter<DonationAdapter.DonationViewHolder>(), Filterable {

    private var donationsFiltered: List<Donasi> = donations

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_card_donasi, parent, false)
        return DonationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val donasi = donationsFiltered[position]
        holder.bind(donasi, context)
    }

    override fun getItemCount(): Int {
        return donationsFiltered.size
    }

    fun updateDonations(newDonations: List<Donasi>) {
        donations = newDonations
        donationsFiltered = newDonations
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                donationsFiltered = if (charString.isEmpty()) {
                    donations
                } else {
                    val filteredList = ArrayList<Donasi>()
                    for (row in donations) {
                        if (row.namaDonasi.toLowerCase(Locale.getDefault()).contains(charString.toLowerCase(Locale.getDefault())) ||
                            row.tempatDonasi.toLowerCase(Locale.getDefault()).contains(charString.toLowerCase(Locale.getDefault()))) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = donationsFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults) {
                donationsFiltered = filterResults.values as List<Donasi>
                notifyDataSetChanged()
            }
        }
    }

    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaDonasi: TextView = itemView.findViewById(R.id.nama_donasi)
        private val tempatDonasi: TextView = itemView.findViewById(R.id.tempat_donasi)
        private val imageView: ImageView = itemView.findViewById(R.id.image_view)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.progress_bar)
        private val sisaHari: TextView = itemView.findViewById(R.id.sisa_hari)
        private val detailButton: Button = itemView.findViewById(R.id.detail_button)

        fun bind(donasi: Donasi, context: Context) {
            namaDonasi.text = donasi.namaDonasi
            tempatDonasi.text = donasi.tempatDonasi
            Glide.with(context).load(donasi.imageUrl).into(imageView)

            val tanggalMulai = donasi.tanggalMulai.toDate()
            val tanggalSelesai = donasi.tanggalSelesai.toDate()
            val currentDate = Date()

            val totalDays = (tanggalSelesai.time - tanggalMulai.time) / (1000 * 60 * 60 * 24)
            val remainingDays = (tanggalSelesai.time - currentDate.time) / (1000 * 60 * 60 * 24)

            progressBar.max = totalDays.toInt()
            progressBar.progress = (totalDays - remainingDays).toInt()

            sisaHari.text = "Sisa Hari $remainingDays"

            detailButton.setOnClickListener {
                val intent = Intent(context, DonationDetailActivity::class.java)
                intent.putExtra("donation_id", donasi.id)
                context.startActivity(intent)
            }
        }
    }
}
