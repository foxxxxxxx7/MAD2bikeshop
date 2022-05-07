package com.wit.mad2bikeshop.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.ui.bookingList.BookingListViewModel

class MapsFragment : Fragment() {

    private val bookingListViewModel: BookingListViewModel by activityViewModels()

    private val callback = OnMapReadyCallback { googleMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val vikingBikeHireLocationW = LatLng(52.260791, -7.105922)
        val vikingBikeHireLocationK = LatLng(52.204365250330284, -7.425864411634394)
        val vikingBikeHireLocationD = LatLng(52.08538860777265, -7.623179554066156)
        googleMap.addMarker(MarkerOptions().position(vikingBikeHireLocationW).title("Viking Bike Hire Waterford Depot"))
        googleMap.addMarker(MarkerOptions().position(vikingBikeHireLocationK).title("Viking Bike Hire Kilmacthomas Depot"))
        googleMap.addMarker(MarkerOptions().position(vikingBikeHireLocationD).title("Viking Bike Hire Dungarvan Depot"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vikingBikeHireLocationK, 9f))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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