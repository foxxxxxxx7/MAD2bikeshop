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

/* This is an interface that has a single method, onBookingClick, which takes a BookModel as a
parameter. */
interface BookListener {
    //    fun onDeleteBooking(booking: BookModel)
//    fun onUpdateBooking(booking: BookModel)
    fun onBookingClick(booking: BookModel)
}

/* This is the constructor of the BookAdapter class. */
class BookAdapter constructor(
    private var bookings: ArrayList<BookModel>,
    private val listener: BookListener,
    private val readOnly: Boolean
) : RecyclerView.Adapter<BookAdapter.MainHolder>() {

    /**
     * This function inflates the layout for the view holder and returns a new view holder with the
     * view
     *
     * @param parent ViewGroup - The ViewGroup into which the new View will be added after it is bound
     * to an adapter position.
     * @param viewType Int
     * @return A MainHolder object
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardBookBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding, readOnly)
    }

    /**
     * The function takes a MainHolder and an Int as parameters, and returns Unit
     *
     * @param holder MainHolder - this is the view holder that will be used to display the data.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val booking = bookings[holder.adapterPosition]
        holder.bind(booking, listener)
    }

    /**
     * Removes the item at the given position from the list and notifies the adapter that the item has
     * been removed.
     *
     * @param position The position of the item in the list.
     */
    fun removeAt(position: Int) {
        bookings.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * `override fun getItemCount(): Int = bookings.size`
     *
     * This function returns the number of items in the `bookings` list
     */
    override fun getItemCount(): Int = bookings.size

    /* This is the constructor of the MainHolder class. */
    inner class MainHolder(val binding: CardBookBinding, private val readOnly: Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        /**
         * It binds the data to the view.
         *
         * @param booking BookModel - This is the data that we want to bind to the view.
         * @param listener BookListener - This is the interface that we created earlier.
         */
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