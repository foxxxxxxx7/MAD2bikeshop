package com.wit.mad2bikeshop.ui.map

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

class MapsFragment : Fragment() {

    private val bookingListViewModel: BookingListViewModel by activityViewModels()
    private lateinit var mapsViewModel: MapsViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        mapsViewModel.map = googleMap

        mapsViewModel.map.uiSettings.isZoomControlsEnabled = true
        mapsViewModel.map.uiSettings.isMyLocationButtonEnabled = true
        val waterfordDepot = LatLng(52.260791, -7.105922)
        val kilmacthomasDepot = LatLng(52.204365250330284, -7.425864411634394)
        val dungarvanDepot = LatLng(52.08538860777265, -7.623179554066156)
        googleMap.addMarker(MarkerOptions().position(waterfordDepot).title("Viking Bike Hire Waterford Depot"))
        googleMap.addMarker(MarkerOptions().position(kilmacthomasDepot).title("Viking Bike Hire Kilmacthomas Depot"))
        googleMap.addMarker(MarkerOptions().position(dungarvanDepot).title("Viking Bike Hire Dungarvan Depot"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kilmacthomasDepot, 9f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mapsViewModel = ViewModelProvider(this).get(MapsViewModel::class.java)

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
}