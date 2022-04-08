package com.wit.mad2bikeshop.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.wit.mad2bikeshop.R

class BookingDetailFragment : Fragment() {

    companion object {
        fun newInstance() = BookingDetailFragment()
    }

    private val args by navArgs<BookingDetailFragmentArgs>()
    private lateinit var viewModel: BookingDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.booking_detail_fragment, container, false)
        val view = inflater.inflate(R.layout.booking_detail_fragment, container, false)

        Toast.makeText(context,"Booking ID: ${args.bookingid}",Toast.LENGTH_LONG).show()

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookingDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}