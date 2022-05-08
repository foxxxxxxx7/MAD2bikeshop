package com.wit.mad2bikeshop.ui.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.wit.mad2bikeshop.R
import com.wit.mad2bikeshop.databinding.BookingDetailFragmentBinding
import com.wit.mad2bikeshop.model.BookModel
import com.wit.mad2bikeshop.ui.auth.LoggedInViewModel
import com.wit.mad2bikeshop.ui.bookingList.BookingListViewModel
import timber.log.Timber

/* This is the code for the BookingDetailFragment. It is a fragment that is used to display the details
of a booking. */
class BookingDetailFragment : Fragment() {

    companion object {
        fun newInstance() = BookingDetailFragment()
    }

    private val args by navArgs<BookingDetailFragmentArgs>()
    private lateinit var detailViewModel: BookingDetailViewModel
    private var _fragBinding: BookingDetailFragmentBinding? = null
    private val fragBinding get() = _fragBinding!!
    private val loggedInViewModel: LoggedInViewModel by activityViewModels()
    private val bookingListViewModel: BookingListViewModel by activityViewModels()
    val user = FirebaseAuth.getInstance().currentUser


    /**
     * The function inflates the layout, sets the title, sets up the view model, and sets up the
     * onClickListeners for the buttons
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment,
     * @param container The ViewGroup into which the new View will be added after it is bound to an
     * adapter position.
     * @param savedInstanceState A Bundle object containing the activity's previously saved state.
     * @return The root view of the fragment.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //return inflater.inflate(R.layout.booking_detail_fragment, container, false)
//        val view = inflater.inflate(R.layout.booking_detail_fragment, container, false)
//
//        Toast.makeText(context,"Booking ID: ${args.bookingid}",Toast.LENGTH_LONG).show()
//
//        return view

        _fragBinding = BookingDetailFragmentBinding.inflate(inflater, container, false)
        val root = fragBinding.root
        activity?.title = getString(R.string.action_details)

        detailViewModel = ViewModelProvider(this).get(BookingDetailViewModel::class.java)
        detailViewModel.observableBooking.observe(viewLifecycleOwner, Observer { render() })

        fragBinding.editDonationButton.setOnClickListener {
            Timber.i("RFOX")
            Timber.i(detailViewModel.observableBooking.value?.uid!!)
            Timber.i(fragBinding.bookingvm?.observableBooking!!.value!!.toString())
            detailViewModel.updateBook(
                user?.uid!!,
                detailViewModel.observableBooking.value?.uid!!,
                fragBinding.bookingvm?.observableBooking!!.value!!
            )

//            (viewHolder.itemView.tag as BookModel).uid!!)

            //Force Reload of list to guarantee refresh
            bookingListViewModel.load()
            findNavController().navigateUp()
            //findNavController().popBackStack()

        }

        fragBinding.deleteDonationButton.setOnClickListener {
            bookingListViewModel.delete(
                user?.uid!!,
                detailViewModel.observableBooking.value?.uid!!
            )
            findNavController().navigateUp()
        }
        return root
    }

    /**
     * `fragBinding.bookingvm = detailViewModel`
     *
     * This is the line that binds the data to the UI
     */
    private fun render() {
////        fragBinding.editMessage.setText("A Message")
////        fragBinding.editUpvotes.setText("0")
//        fragBinding.editName.setText(booking.name)
//        fragBinding.editPhoneNumber.setText(booking.phoneNumber)
//        fragBinding.editDate.setText(booking.date)
//        fragBinding.editEmail.setText(booking.email)
//        fragBinding.editPickup.setText(booking.pickup)
//        fragBinding.editDropoff.setText(booking.dropoff)
//        fragBinding.editPrice.setText(booking.price.toString())
//
//        fragBinding.spinner2.setSelection(booking.bike)
//        fragBinding.editID.setText(booking.id.toString())
////        fragBinding.spinner2. /////// TODO!!! set value from order to spinner

        fragBinding.bookingvm = detailViewModel
        Timber.i("Retrofit fragBinding.bookingvm == $fragBinding.bookingvm")
    }

    /**
     * The function is called when the activity is resumed. It calls the getBooking function in the
     * detailViewModel, which is a ViewModel class. The getBooking function is called with the user's
     * uid and the bookingid passed in as arguments
     */
    override fun onResume() {
        super.onResume()
        detailViewModel.getBooking(
            user?.uid!!,
            args.bookingid
        )
    }

    /**
     * It sets the binding variable to null.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _fragBinding = null
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        detailViewModel = ViewModelProvider(this).get(BookingDetailViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}