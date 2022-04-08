package com.wit.mad2bikeshop.ui.bookingList

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.adapters.BookAdapter
import com.wit.mad2bikeshop.adapters.BookListener
import com.wit.mad2bikeshop.databinding.FragmentBookingListBinding
import com.wit.mad2bikeshop.model.BookModel

class BookingListFragment : Fragment(), BookListener {

  //  lateinit var app: BikeshopApp
    private var _fragBinding: FragmentBookingListBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var bookingListViewModel: BookingListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // app = activity?.application as BikeshopApp
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
//        fragBinding.recyclerView.adapter =
//           BookAdapter(bookingListViewModel.findAll(), this@BookingListFragment)
        bookingListViewModel = ViewModelProvider(this).get(BookingListViewModel::class.java)
        bookingListViewModel.load()
        bookingListViewModel.observableBookingList.observe(viewLifecycleOwner, Observer {
                bookings ->
            bookings?.let { render(bookings) }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = BookingListFragmentDirections.actionBookingListFragmentToBookFragment()
            findNavController().navigate(action)
        }
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_booking_list, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }

    private fun render(bookingList: List<BookModel>) {
        fragBinding.recyclerView.adapter = BookAdapter(bookingList, this )
        if (bookingList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.bookingsNotFound.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.bookingsNotFound.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        bookingListViewModel.load()
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
        bookingListViewModel.del(booking)
        bookingListViewModel.load()
        showBookings(bookingListViewModel.findAll())
        Toast.makeText(context, "Booking Deleted!", Toast.LENGTH_LONG).show()

    }

    override fun onUpdateBooking(booking: BookModel) {
        showBookings(bookingListViewModel.findAll())
    }

    override fun onBookingClick(booking: BookModel) {
        val action = BookingListFragmentDirections.actionBookingListFragmentToBookingDetailFragment()
        findNavController().navigate(action)
    }

}