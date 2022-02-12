package com.wit.mad2bikeshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.wit.mad2bikeshop.databinding.ActivityBookBinding
import com.wit.mad2bikeshop.databinding.FragmentBookBinding
import com.wit.mad2bikeshop.main.BikeshopApp
import com.wit.mad2bikeshop.model.BookModel

class BookFragment : Fragment() {

    private lateinit var bookLayout: ActivityBookBinding
    lateinit var app: BikeshopApp
    var booking = BookModel()
    var edit = false
    var selectedDate: String = ""
    private var _fragBinding: FragmentBookBinding? = null
    private val fragBinding get() = _fragBinding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        app = activity?.application as BikeshopApp
        super.onCreate(savedInstanceState)
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

    fun setButtonListener(BookLayout: FragmentBookBinding) {
        bookLayout.bookButton.setOnClickListener {
            booking.date = selectedDate
            booking.name = bookLayout.bookName.text.toString()
            booking.phoneNumber = bookLayout.bookNumber.text.toString()
            booking.email = bookLayout.bookEmail.text.toString()
            booking.pickup = bookLayout.bookPickup.text.toString()
            booking.dropoff = bookLayout.bookDropoff.text.toString()
            if (booking.name.isEmpty() || booking.phoneNumber.isEmpty() || booking.email.isEmpty() || booking.pickup.isEmpty() || booking.dropoff.isEmpty()) {
                Toast.makeText(context, "Please complete ALL fields", Toast.LENGTH_LONG).show()
            } else {
                if (edit) {
                    app.bookStore.update(booking.copy())
                } else {
                    app.bookStore.create(booking.copy())
                    print("Add Button Pressed: $bookLayout.bookName, $bookLayout.bookNumber, $bookLayout.bookEmail")
                }
            }
        }

    }
}