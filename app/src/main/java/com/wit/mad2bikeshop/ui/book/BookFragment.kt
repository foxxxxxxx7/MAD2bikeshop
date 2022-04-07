package com.wit.mad2bikeshop.ui.book

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.FragmentBookBinding
import com.wit.mad2bikeshop.model.BookModel
import java.text.SimpleDateFormat
import java.util.*

////////////////////may come in handy when adding total to order///////////////////////////////
//override fun onResume() {
//    super.onResume()
//    val reportViewModel = ViewModelProvider(this).get(ReportViewModel::class.java)
//    reportViewModel.observableDonationsList.observe(viewLifecycleOwner, Observer {
//        totalDonated = reportViewModel.observableDonationsList.value!!.sumOf { it.amount }
//        fragBinding.progressBar.progress = totalDonated
//        fragBinding.totalSoFar.text = getString(R.string.total_donated,totalDonated)
//    })
//}
class BookFragment : Fragment() {

  //  lateinit var app: BikeshopApp
    var booking = BookModel()
    var edit = false
    private var _fragBinding: FragmentBookBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var bookViewModel: BookViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //app = activity?.application as BikeshopApp
        setHasOptionsMenu(true)


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _fragBinding = FragmentBookBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_book)

        bookViewModel = ViewModelProvider(this).get(BookViewModel::class.java)
        bookViewModel.observableStatus.observe(viewLifecycleOwner, Observer { status ->
            status?.let { render(status) }
        })


        setButtonListener(fragBinding)
        val selectDate = fragBinding.bookDate
        //https://stackoverflow.com/questions/16031314/how-can-i-get-selected-date-in-calenderview-in-android#:~:text=Set%20listener%20to%20set%20selected,date%20to%20get%20selected%20date.
        //I found this solution on StackOverflow after the date kept appearing as today's date
        // Set date change listener on calenderView.
        // Callback notified when user select a date from CalenderView on UI.
        selectDate.setOnDateChangeListener{calView: CalendarView, year: Int , month: Int, dayOfMonth: Int ->
            // Create calender object with which will have system date time.
            val calender: Calendar = Calendar.getInstance()
            // Set attributes in calender object as per selected date.
            calender.set(year, month, dayOfMonth)
            // Now set calenderView with this calender object to highlight selected date on UI.
            calView.setDate(calender.timeInMillis, true, true)
            Log.d("SelectedDate", "$dayOfMonth/${month + 1}/$year")

        }
        return root
    }

    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context,getString(R.string.bookError),Toast.LENGTH_LONG).show()
        }
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

            val readableDate = SimpleDateFormat("dd/MM/yyyy").format(Date(layout.bookDate.date))
            booking.date = readableDate
            booking.name = layout.bookName.text.toString()
            booking.phoneNumber = layout.bookNumber.text.toString()
            booking.email = layout.bookEmail.text.toString()
            booking.pickup = layout.bookPickup.text.toString()
            booking.dropoff = layout.bookDropoff.text.toString()
            if (booking.name.isEmpty() || booking.phoneNumber.isEmpty() || booking.email.isEmpty() || booking.pickup.isEmpty() || booking.dropoff.isEmpty()) {
                Toast.makeText(context, "Please complete ALL fields", Toast.LENGTH_LONG).show()
            } else {
                if (edit) {
                    bookViewModel.updateBook(booking.copy())
                } else {
                    layout.bookName.setText("")
                    layout.bookNumber.setText("")
                    layout.bookEmail.setText("")
                    layout.bookPickup.setText("")
                    layout.bookDropoff.setText("")
                    Toast.makeText(context, "Booking Added!", Toast.LENGTH_LONG).show()
                    bookViewModel.addBook(booking.copy())
                    print("Add Button Pressed: $layout.bookName, $layout.bookNumber, $layout.bookEmail")
                    /*setResult(AppCompatActivity.RESULT_OK)*/
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_book, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}