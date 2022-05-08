package com.wit.mad2bikeshop.ui.book

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.CalendarView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.FragmentBookBinding
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.ui.auth.LoggedInViewModel
import com.wit.mad2bikeshop.ui.map.MapsViewModel
import java.text.SimpleDateFormat
import java.util.*

/* This is the code for the BookFragment. It is the fragment that is used to book a bike. */
class BookFragment : Fragment() {

    //  lateinit var app: BikeshopApp
    var booking = BookModel()
    var edit = false
    private var _fragBinding: FragmentBookBinding? = null
    private val fragBinding get() = _fragBinding!!
    private lateinit var bookViewModel: BookViewModel
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private val mapsViewModel: MapsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //app = activity?.application as BikeshopApp
        setHasOptionsMenu(true)


    }

    /**
     * The function sets the button listener for the book button, and sets the date change listener for
     * the calendar view
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container The parent that this fragment's UI should be attached to.
     * @param savedInstanceState Bundle?
     * @return The root view is being returned.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        selectDate.setOnDateChangeListener { calView: CalendarView, year: Int, month: Int, dayOfMonth: Int ->
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

    /**
     * It renders the result of the book request.
     *
     * @param status Boolean - This is the status of the book. If the book is successfully added, it
     * will return true. If it fails, it will return false.
     */
    private fun render(status: Boolean) {
        when (status) {
            true -> {
                view?.let {
                    //Uncomment this if you want to immediately return to Report
                    //findNavController().popBackStack()
                }
            }
            false -> Toast.makeText(context, getString(R.string.bookError), Toast.LENGTH_LONG)
                .show()
        }
    }

    /* A static method that is used to create a new instance of the fragment. */
    companion object {
        @JvmStatic
        fun newInstance() =
            BookFragment().apply {
                arguments = Bundle().apply {}
            }
    }


    /**
     * It sets the binding variable to null.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

    /**
     * This function is called when the user clicks the "Book" button. It takes the data from the user
     * input fields and creates a new BookModel object, which is then passed to the BookViewModel to be
     * added to the database
     *
     * @param layout FragmentBookBinding - This is the binding object that is created when the fragment
     * is created.
     */
    fun setButtonListener(layout: FragmentBookBinding) {


        layout.bookButton.setOnClickListener {

            val readableDate = SimpleDateFormat("dd/MM/yy").format(Date(layout.bookDate.date))
            booking.date = readableDate
            booking.name = layout.bookName.text.toString()
            booking.phoneNumber = layout.bookNumber.text.toString()
            booking.email = layout.bookEmail.text.toString()
            booking.pickup = layout.bookPickup.text.toString()
            booking.dropoff = layout.bookDropoff.text.toString()
            //var selInt = R.array.bikeArray.
            booking.bike = layout.bookBike.selectedItemPosition
            if (booking.name.isEmpty() || booking.phoneNumber.isEmpty() || booking.pickup.isEmpty() || booking.dropoff.isEmpty()) {
                Toast.makeText(context, "Please complete ALL fields", Toast.LENGTH_LONG).show()
//            } else {
//                if (edit) {
//                   // bookViewModel.updateBook(booking.copy())
            } else {
                layout.bookName.setText("")
                layout.bookNumber.setText("")
                layout.bookEmail.setText("")
                layout.bookPickup.setText("")
                layout.bookDropoff.setText("")
                Toast.makeText(context, "Booking Added!", Toast.LENGTH_LONG).show()
//                    bookViewModel.addBook(booking.copy())
                bookViewModel.addBook(
                    loggedInViewModel.liveFirebaseUser,
                    BookModel(
                        name = booking.name,
                        date = booking.date,
                        phoneNumber = booking.phoneNumber,
                        pickup = booking.pickup,
                        dropoff = booking.dropoff,
                        email = loggedInViewModel.liveFirebaseUser.value?.email!!,
                        latitude = mapsViewModel.currentLocation.value!!.latitude,
                        longitude = mapsViewModel.currentLocation.value!!.longitude
                    )
                )
                print("Add Button Pressed: $layout.bookName, $layout.bookNumber, $layout.bookEmail")
                /*setResult(AppCompatActivity.RESULT_OK)*/
            }
        }
    }


    /**
     * > This function inflates the menu_book.xml file into the menu object
     *
     * @param menu The menu object that you want to inflate.
     * @param inflater The MenuInflater that you use to inflate your menu resource into the Menu
     * object.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_book, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**
     * If the user clicks on an item in the menu, and that item has an associated action, then perform
     * that action
     *
     * @param item The menu item that was selected.
     * @return The return value is a boolean.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}