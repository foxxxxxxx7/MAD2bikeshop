package com.wit.mad2bikeshop.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.adapters.BookAdapter
import com.wit.mad2bikeshop.adapters.BookListener
import com.wit.mad2bikeshop.databinding.FragmentBookBinding
import com.wit.mad2bikeshop.databinding.FragmentBookingListBinding
import com.wit.mad2bikeshop.main.BikeshopApp
import com.wit.mad2bikeshop.model.BookModel

class BookingListFragment : Fragment(), BookListener {

    lateinit var app: BikeshopApp
    private var _fragBinding: FragmentBookingListBinding? = null
    private val fragBinding get() = _fragBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as BikeshopApp
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _fragBinding = FragmentBookingListBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_booklist)
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        fragBinding.recyclerView.adapter = BookAdapter(app.bookStore.findAll(), this@BookingListFragment)

        return root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookingListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    private fun showBookings(bookings: List<BookModel>) {
        view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter =
            BookAdapter(bookings, this@BookingListFragment)
        view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter?.notifyDataSetChanged()
    }

    override fun onDeleteBooking(booking: BookModel) {
        app.bookStore.delete(booking)
        showBookings(app.bookStore.findAll())

    }

    override fun onUpdateBooking(booking: BookModel) {
        print("update")
    }


}