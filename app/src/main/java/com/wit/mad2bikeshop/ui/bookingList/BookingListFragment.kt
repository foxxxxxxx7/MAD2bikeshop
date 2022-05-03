package com.wit.mad2bikeshop.ui.bookingList

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.adapters.BookAdapter
import com.wit.mad2bikeshop.adapters.BookListener
import com.wit.mad2bikeshop.databinding.FragmentBookingListBinding
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.ui.auth.LoggedInViewModel
import com.wit.mad2bikeshop.utils.*

class BookingListFragment : Fragment(), BookListener {

  //  lateinit var app: BikeshopApp
    private var _fragBinding: FragmentBookingListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader : AlertDialog
    private lateinit var bookingListViewModel: BookingListViewModel
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()

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
        loader = createLoader(requireActivity())
        fragBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
//        fragBinding.recyclerView.adapter =
//           BookAdapter(bookingListViewModel.findAll(), this@BookingListFragment)
        bookingListViewModel = ViewModelProvider(this).get(BookingListViewModel::class.java)
        bookingListViewModel.load()
        bookingListViewModel.observableBookingList.observe(viewLifecycleOwner, Observer {
                bookings ->
            bookings?.let {
                render(bookings as ArrayList<BookModel>)
                hideLoader(loader)
                checkSwipeRefresh() }
        })

        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = BookingListFragmentDirections.actionBookingListFragmentToBookFragment()
            findNavController().navigate(action)
        }

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = fragBinding.recyclerView.adapter as BookAdapter
                adapter.removeAt(viewHolder.adapterPosition)

            }
        }
        val itemTouchDeleteHelper = ItemTouchHelper(swipeDeleteHandler)
        itemTouchDeleteHelper.attachToRecyclerView(fragBinding.recyclerView)

        val swipeEditHandler = object : SwipeToEditCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
               onBookingClick(viewHolder.itemView.tag as BookModel)
            }
        }
        val itemTouchEditHelper = ItemTouchHelper(swipeEditHandler)
        itemTouchEditHelper.attachToRecyclerView(fragBinding.recyclerView)


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

    private fun render(bookingList: ArrayList<BookModel>) {
        fragBinding.recyclerView.adapter = BookAdapter(bookingList, this )
        if (bookingList.isEmpty()) {
            fragBinding.recyclerView.visibility = View.GONE
            fragBinding.bookingsNotFound.visibility = View.VISIBLE
            fragBinding.John.visibility = View.VISIBLE
        } else {
            fragBinding.recyclerView.visibility = View.VISIBLE
            fragBinding.bookingsNotFound.visibility = View.GONE
            fragBinding.John.visibility = View.GONE
        }
    }

    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader,"Downloading Booking")
            bookingListViewModel.load()

        }
    }

    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
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

//    private fun showBookings(bookings: List<BookModel>) {
//        view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter =
//            BookAdapter(bookings, this@BookingListFragment)
//        view?.findViewById<RecyclerView>(R.id.recyclerView)?.adapter?.notifyDataSetChanged()
//    }
//
//    override fun onDeleteBooking(booking: BookModel) {
//        //bookingListViewModel.del(booking)
//        bookingListViewModel.load()
//     //   showBookings(bookingListViewModel.findAll())
//        Toast.makeText(context, "Booking Deleted!", Toast.LENGTH_LONG).show()
//
//    }
//
//    override fun onUpdateBooking(booking: BookModel) {
//     //   showBookings(bookingListViewModel.findAll())
//    }

    override fun onBookingClick(booking: BookModel) {
        val action = BookingListFragmentDirections.actionBookingListFragmentToBookingDetailFragment(booking.id)
        findNavController().navigate(action)
    }

}