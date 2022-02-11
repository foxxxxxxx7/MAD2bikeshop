package com.wit.mad2bikeshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.CardBookBinding
import com.wit.mad2bikeshop.model.BookModel


class BookAdapter constructor(private var bookings: List<BookModel>)
    : RecyclerView.Adapter<BookAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardBookBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val booking = bookings[holder.adapterPosition]
        holder.bind(booking)
    }

    override fun getItemCount(): Int = bookings.size

    inner class MainHolder(val binding : CardBookBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(booking: BookModel) {
            binding.name.text = booking.name
            binding.phoneNumber.text = booking.phoneNumber.toString()
            binding.date.text = booking.date
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
        }
    }
}