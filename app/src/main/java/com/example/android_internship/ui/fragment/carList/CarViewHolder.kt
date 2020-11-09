package com.example.android_internship.ui.fragment.carList

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.android_internship.R
import com.jakewharton.rxbinding3.view.clicks

class CarViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val carTextView = itemView.findViewById<TextView>(R.id.carTextView)
    val cardClicks = itemView.findViewById<CardView>(R.id.carCard).clicks()
}