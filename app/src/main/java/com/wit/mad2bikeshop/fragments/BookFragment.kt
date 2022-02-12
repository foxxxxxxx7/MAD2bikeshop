package com.wit.mad2bikeshop.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.FragmentBookBinding
import com.wit.mad2bikeshop.main.BikeshopApp
import com.wit.mad2bikeshop.model.BookModel

class BookFragment : Fragment() {

    lateinit var app: BikeshopApp
    var booking = BookModel()
    var edit = false
    var selectedDate: String = ""
    private var _fragBinding: FragmentBookBinding? = null
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

        _fragBinding = FragmentBookBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_book)
        return root;
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            BookFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    fun setButtonListener(layout: FragmentBookBinding) {
        layout.bookButton.setOnClickListener {
            booking.date = selectedDate
            booking.name = layout.bookName.text.toString()
            booking.phoneNumber = layout.bookNumber.text.toString()
            booking.email = layout.bookEmail.text.toString()
            booking.pickup = layout.bookPickup.text.toString()
            booking.dropoff = layout.bookDropoff.text.toString()
            if (booking.name.isEmpty() || booking.phoneNumber.isEmpty() || booking.email.isEmpty() || booking.pickup.isEmpty() || booking.dropoff.isEmpty()) {
                Toast.makeText(context, "Please complete ALL fields", Toast.LENGTH_LONG).show()
            } else {
                if (edit) {
                    app.bookStore.update(booking.copy())
                } else {
                    app.bookStore.create(booking.copy())
                    print("Add Button Pressed: $layout.bookName, $layout.bookNumber, $layout.bookEmail")
                    /* setResult(AppCompatActivity.RESULT_OK)*/
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_book, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item,
            requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}