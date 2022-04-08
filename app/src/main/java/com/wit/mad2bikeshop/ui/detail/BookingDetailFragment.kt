package com.wit.mad2bikeshop.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.BookingDetailFragmentBinding
import com.wit.mad2bikeshop.model.BookModel

class BookingDetailFragment : Fragment() {

    companion object {
        fun newInstance() = BookingDetailFragment()
    }

    private val args by navArgs<BookingDetailFragmentArgs>()
    private lateinit var detailViewModel: BookingDetailViewModel
    private var _fragBinding: BookingDetailFragmentBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.booking_detail_fragment, container, false)
//        val view = inflater.inflate(R.layout.booking_detail_fragment, container, false)
//
//        Toast.makeText(context,"Booking ID: ${args.bookingid}",Toast.LENGTH_LONG).show()
//
//        return view
        _fragBinding = BookingDetailFragmentBinding.inflate(inflater, container, false)
        val root = fragBinding.root

        detailViewModel = ViewModelProvider(this).get(BookingDetailViewModel::class.java)
        detailViewModel.observableBooking.observe(viewLifecycleOwner, Observer {
                booking ->
            booking?.let { render(booking) } })
        return root
    }
    private fun render(booking: BookModel) {
//        fragBinding.editMessage.setText("A Message")
//        fragBinding.editUpvotes.setText("0")
        fragBinding.editName.setText(booking.name)
        fragBinding.editPhoneNumber.setText(booking.phoneNumber)
        fragBinding.editDate.setText(booking.date)
        fragBinding.editEmail.setText(booking.email)
        fragBinding.editPickup.setText(booking.pickup)
        fragBinding.editDropoff.setText(booking.dropoff)
        fragBinding.editPrice.setText(booking.price.toString())
        fragBinding.editID.setText(booking.id.toString())

        fragBinding.bookingvm = detailViewModel
    }

    override fun onResume() {
        super.onResume()
        detailViewModel.getBooking(args.bookingid)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        detailViewModel = ViewModelProvider(this).get(BookingDetailViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}