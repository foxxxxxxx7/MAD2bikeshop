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

class MapsFragment : Fragment() {

    private val bookingListViewModel: BookingListViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()
    private val loggedInViewModel : LoggedInViewModel by activityViewModels()
    lateinit var loader : AlertDialog

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

            bookingListViewModel.observableBookingList.observe(viewLifecycleOwner, Observer { bookings ->
                bookings?.let {
                    render(bookings as ArrayList<BookModel>)
                    hideLoader(loader)
                }
            })

        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)
        loader = createLoader(requireActivity())

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

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
                if(it.email.equals(this.bookingListViewModel.liveFirebaseUser.value!!.email))
                    markerColour = BitmapDescriptorFactory.HUE_ORANGE
                else
                    markerColour = BitmapDescriptorFactory.HUE_AZURE + 5

                mapsViewModel.map.addMarker(
                    MarkerOptions().position(LatLng(it.latitude, it.longitude))
                        .title("${it.name} €${it.phoneNumber}")
                        .snippet(it.email)
                        .icon(BitmapDescriptorFactory.defaultMarker(markerColour ))
                )
            }
        }
    }

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