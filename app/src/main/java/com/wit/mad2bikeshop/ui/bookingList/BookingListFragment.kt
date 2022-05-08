package com.wit.mad2bikeshop.ui.bookingList

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
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
import com.google.firebase.auth.FirebaseAuth
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.adapters.BookAdapter
import com.wit.mad2bikeshop.adapters.BookListener
import com.wit.mad2bikeshop.databinding.FragmentBookingListBinding
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.ui.auth.LoggedInViewModel
import com.wit.mad2bikeshop.utils.*
import timber.log.Timber

/*  */
class BookingListFragment : Fragment(), BookListener {

    //  lateinit var app: BikeshopApp
    private var _fragBinding: FragmentBookingListBinding? = null
    private val fragBinding get() = _fragBinding!!
    lateinit var loader: AlertDialog
    private lateinit var bookingListViewModel: BookingListViewModel
    val user = FirebaseAuth.getInstance().currentUser
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()

    /**
     * The function is called when the activity is created
     *
     * @param savedInstanceState This is a Bundle object that contains the activity's previously saved
     * state. If the activity has never existed before, the value of the Bundle object is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // app = activity?.application as BikeshopApp
        setHasOptionsMenu(true)
    }

    /**
     * It creates the view for the fragment.
     *
     * @param inflater LayoutInflater
     * @param container The parent that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     * @return The root view of the fragment.
     */
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
        val fab: FloatingActionButton = fragBinding.fab
        fab.setOnClickListener {
            val action = BookingListFragmentDirections.actionBookingListFragmentToBookFragment()
            findNavController().navigate(action)
        }


        bookingListViewModel = ViewModelProvider(this).get(BookingListViewModel::class.java)
        // bookingListViewModel.load()
        bookingListViewModel.observableBookingList.observe(
            viewLifecycleOwner,
            Observer { bookings ->
                bookings?.let {
                    render(bookings as ArrayList<BookModel>)
                    hideLoader(loader)
                    checkSwipeRefresh()
                }
            })

        setSwipeRefresh()

        val swipeDeleteHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                Timber.i("hello456")
                showLoader(loader, "Deleting Booking")
                val adapter = fragBinding.recyclerView.adapter as BookAdapter
                adapter.removeAt(viewHolder.adapterPosition)
                bookingListViewModel.delete(
                    user?.uid!!,
                    (viewHolder.itemView.tag as BookModel).uid!!
                )
                Timber.i(bookingListViewModel.liveFirebaseUser.value.toString())
                Timber.i("hello123")
                hideLoader(loader)

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

    /**
     * We're inflating the menu, finding the toggle button, setting the checked state, and setting the
     * listener
     *
     * @param menu The menu object that you want to inflate.
     * @param inflater The MenuInflater that you use to inflate the menu resource into the given menu.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_booking_list, menu)

        val item = menu.findItem(R.id.toggleBookings) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleBookings: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleBookings.isChecked = false

        toggleBookings.setOnCheckedChangeListener { buttonView, isChecked ->
    /**
     * If the user clicks on an item in the menu, and that item has an associated action, then perform
     * that action
     *
     * @param item The menu item that was selected.
     * @return The return value is a boolean.
     */
            if (isChecked) bookingListViewModel.loadAll()
            else bookingListViewModel.load()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }

    /**
     * The function renders the recycler view with the list of bookings
     *
     * @param bookingList ArrayList<BookModel> - The list of bookings to be displayed
     */
    private fun render(bookingList: ArrayList<BookModel>) {
        fragBinding.recyclerView.adapter = BookAdapter(
            bookingList, this, bookingListViewModel.readOnly.value!!
        )
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

    /**
     * It sets the swipe refresh listener.
     */
    fun setSwipeRefresh() {
        fragBinding.swiperefresh.setOnRefreshListener {
            fragBinding.swiperefresh.isRefreshing = true
            showLoader(loader, "Downloading Booking")
            if (bookingListViewModel.readOnly.value!!)
                bookingListViewModel.loadAll()
            else
                bookingListViewModel.load()

        }
    }

    /**
     * It checks if the swipe refresh is refreshing and if it is, it sets it to false.
     */
    private fun checkSwipeRefresh() {
        if (fragBinding.swiperefresh.isRefreshing)
            fragBinding.swiperefresh.isRefreshing = false
    }

    /**
     * The function is called when the activity is resumed
     */
    override fun onResume() {
        super.onResume()
        bookingListViewModel.load()
    }

    /* A static method that returns a new instance of the fragment. */
    companion object {
        @JvmStatic
        fun newInstance() =
            BookingListFragment().apply {
                arguments = Bundle().apply { }
            }
    }

    /**
     * It sets the binding variable to null.
     */
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

    /**
     * If the user is not in read-only mode, navigate to the booking detail fragment.
     *
     * @param booking BookModel - The booking object that was clicked
     */
    override fun onBookingClick(booking: BookModel) {
        val action =
            BookingListFragmentDirections.actionBookingListFragmentToBookingDetailFragment(booking.uid!!)
        if (!bookingListViewModel.readOnly.value!!)
            findNavController().navigate(action)
    }

}