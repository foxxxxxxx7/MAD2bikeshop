package com.wit.mad2bikeshop.ui.map

import android.annotation.SuppressLint
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.ui.bookingList.BookingListViewModel
import timber.log.Timber

import android.app.AlertDialog
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.ui.auth.LoggedInViewModel
import com.wit.mad2bikeshop.utils.createLoader
import com.wit.mad2bikeshop.utils.hideLoader
import com.wit.mad2bikeshop.utils.showLoader

/* This is the code for the map fragment. It is responsible for displaying the map and the markers on
the map. */
class MapsFragment : Fragment() {

    private val bookingListViewModel: BookingListViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    lateinit var loader: AlertDialog

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        mapsViewModel.map = googleMap
        mapsViewModel.map.isMyLocationEnabled = true

        mapsViewModel.currentLocation.observe(viewLifecycleOwner, {
            val loc = LatLng(
                mapsViewModel.currentLocation.value!!.latitude,
                mapsViewModel.currentLocation.value!!.longitude
            )
            val waterfordDepot = LatLng(52.260791, -7.105922)
            val kilmacthomasDepot = LatLng(52.204365250330284, -7.425864411634394)
            val dungarvanDepot = LatLng(52.08538860777265, -7.623179554066156)


            googleMap.addMarker(
                MarkerOptions().position(waterfordDepot).title("Viking Bike Hire Waterford Depot")
            )
            googleMap.addMarker(
                MarkerOptions().position(kilmacthomasDepot)
                    .title("Viking Bike Hire Kilmacthomas Depot")
            )
            googleMap.addMarker(
                MarkerOptions().position(dungarvanDepot).title("Viking Bike Hire Dungarvan Depot")
            )

            mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
            mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 9f))

            bookingListViewModel.observableBookingList.observe(
                viewLifecycleOwner,
                Observer { bookings ->
                    bookings?.let {
                        render(bookings as ArrayList<BookModel>)
                        hideLoader(loader)
                    }
                })

        })
    }

    /**
     * It sets the menu for the activity.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     * is the state.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    /**
     * This function is called when the fragment is created. It creates a loader and returns the layout
     * for the fragment
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state. If
     * the activity has never existed before, the value of the Bundle object is null.
     * @return The view of the fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        loader = createLoader(requireActivity())

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    /**
     * A callback function that is called when the map is ready to be used.
     *
     * @param view View - The view returned by onCreateView(LayoutInflater, ViewGroup, Bundle)
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this
     * is the state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    /**
     * We're inflating the menu, finding the toggle button, setting the checked state, and setting the
     * onCheckedChangeListener
     *
     * @param menu The menu object that you want to inflate.
     * @param inflater MenuInflater
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_map, menu)

        val item = menu.findItem(R.id.toggleBookings) as MenuItem
        item.setActionView(R.layout.togglebutton_layout)
        val toggleBookings: SwitchCompat = item.actionView.findViewById(R.id.toggleButton)
        toggleBookings.isChecked = false

        toggleBookings.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) bookingListViewModel.loadAll()
            else bookingListViewModel.load()
        }
    }

    /**
     * The function takes an ArrayList of BookModel objects as a parameter, clears the map, adds
     * markers for the three depots, then adds a marker for each BookModel object in the ArrayList
     *
     * @param bookingsList ArrayList<BookModel> - This is the list of bookings that we want to display
     * on the map.
     */
    private fun render(bookingsList: ArrayList<BookModel>) {
        var markerColour: Float
        if (!bookingsList.isEmpty()) {
            mapsViewModel.map.clear()

            val waterfordDepot = LatLng(52.260791, -7.105922)
            val kilmacthomasDepot = LatLng(52.204365250330284, -7.425864411634394)
            val dungarvanDepot = LatLng(52.08538860777265, -7.623179554066156)

            mapsViewModel.map.addMarker(
                MarkerOptions().position(waterfordDepot).title("Viking Bike Hire Waterford Depot")
            )
            mapsViewModel.map.addMarker(
                MarkerOptions().position(kilmacthomasDepot)
                    .title("Viking Bike Hire Kilmacthomas Depot")
            )
            mapsViewModel.map.addMarker(
                MarkerOptions().position(dungarvanDepot).title("Viking Bike Hire Dungarvan Depot")
            )

            bookingsList.forEach {
                if (it.email.equals(this.bookingListViewModel.liveFirebaseUser.value!!.email))
                    markerColour = BitmapDescriptorFactory.HUE_ORANGE
                else
                    markerColour = BitmapDescriptorFactory.HUE_AZURE + 5

                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.name} €${it.phoneNumber}")
                        .snippet(it.email)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour))
                )
            }
        }
    }

    /**
     * The function is called when the fragment is resumed. It shows a loader, and then observes the
     * loggedInViewModel.liveFirebaseUser. If the user is logged in, it sets the
     * bookingListViewModel.liveFirebaseUser to the logged in user, and then calls the
     * bookingListViewModel.load() function
     */
    override fun onResume() {
        super.onResume()
        showLoader(loader, "Downloading Bookings")
        loggedInViewModel.liveFirebaseUser.observe(viewLifecycleOwner, Observer { firebaseUser ->
            if (firebaseUser != null) {
                bookingListViewModel.liveFirebaseUser.value = firebaseUser
                bookingListViewModel.load()
            }
        })
    }
}