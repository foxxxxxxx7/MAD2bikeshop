package com.wit.mad2bikeshop.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.CardBookBinding
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.utils.customTransformation

interface BookListener {
//    fun onDeleteBooking(booking: BookModel)
//    fun onUpdateBooking(booking: BookModel)
    fun onBookingClick(booking: BookModel)
}

class BookAdapter constructor(
    private var bookings: ArrayList<BookModel>,
    private val listener: BookListener,
    private val readOnly: Boolean)
    : RecyclerView.Adapter<BookAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardBookBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, readOnly)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val booking = bookings[holder.adapterPosition]
        holder.bind(booking, listener)
    }

    fun removeAt(position: Int) {
        bookings.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = bookings.size

    inner class MainHolder(val binding: CardBookBinding,private val readOnly : Boolean) :
                            RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(booking: BookModel, listener: BookListener) {
            binding.root.tag = booking
            binding.booking = booking
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            Picasso.get().load(booking.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
//
//            binding.name.text = booking.name
//            binding.phoneNumber.text = booking.phoneNumber
//            binding.date.text = booking.date
            binding.root.setOnClickListener { listener.onBookingClick(booking) }
//            binding.buttonDelete.setOnClickListener { listener.onDeleteBooking(booking) }
//            binding.buttonUpdate.setOnClickListener { listener.onUpdateBooking(booking) }

            binding.executePendingBindings()
            /* binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)*/
        }




    }
}