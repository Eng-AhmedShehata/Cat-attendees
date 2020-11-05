package com.ashehata.catattendees.addDay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ashehata.catattendees.R
import com.ashehata.catattendees.helper.Logger
import com.ashehata.catattendees.helper.MEETING_COLLECTION
import com.ashehata.catattendees.helper.showToast
import com.ashehata.catattendees.meeting.MeetingDay
import com.ashehata.catattendees.meeting.MeetingPlace
import com.ashehata.catattendees.meeting.MeetingType
import com.ashehata.catattendees.meeting.setDayInfo
import com.ashehata.mylibrary.ValidateErrorType
import com.ashehata.mylibrary.ValidateME
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_add_day.*
import java.util.*


class AddMeetingFragment : Fragment() {

    private lateinit var firebaseFirestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_day, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseFirestore = FirebaseFirestore.getInstance()
        fillSpinner()
        setAddButton()
    }

    private fun fillSpinner() {
        val meetingPlace = MeetingPlace.values()

        val places = mutableListOf<String>()
        for (place in meetingPlace) {
            places.add(place.toString())
        }
        //Creating the ArrayAdapter instance having the country list
        val aa =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, places)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        //Setting the ArrayAdapter data on the Spinner
        spinner_places.adapter = aa
    }

    private fun setAddButton() {
        btn_add_day.setOnClickListener {
            val topic = et_topic.text.toString()

            ValidateME.checkAllValidation(
                listOf(
                    ValidateME.validateCustom("[a-zA-Z]{3,}", topic),
                ),
                onValidationResult = object : ValidateME.OnValidationResult {
                    override fun onSuccess() {
                        // so, continue your flow after a successfully validation
                        saveData()
                    }

                    override fun onError(validateErrorType: ValidateErrorType?, validatePosition: Int) {
                        // handle your error here after validation has failed
                        if (validateErrorType == ValidateErrorType.Custom) {
                            requireContext().showToast("Enter a topic first")
                        }

                    }
                })

        }
    }

    private fun saveData() {
        // show loading state
        rl_loading.loadingProgress(true)

        val topic = et_topic.text.toString()
        val type = when (rg_type.checkedRadioButtonId) {
            R.id.rb_offline -> MeetingType.Offline
            R.id.rb_online -> MeetingType.Online
            else -> MeetingType.Unknown
        }
        val place = spinner_places.selectedItem.toString()
        val dateInMilli = Date().time
        val period = 1.5

        val meetingDay = MeetingDay(
            topic = topic,
            type = type.toString(),
            place = place,
            dateMilliSecond = dateInMilli,
            period = period
        )

        val newDay = setDayInfo(meetingDay)

        // Add a new document with a generated ID
        firebaseFirestore.collection(MEETING_COLLECTION)
            .add(newDay)
            .addOnSuccessListener { documentReference ->
                rl_loading.loadingProgress(false)
                requireContext().showToast("Success")
                findNavController().popBackStack()
                Logger.i(
                    "success", "DocumentSnapshot added with ID: " + documentReference.id
                )
            }
            .addOnFailureListener { e ->
                rl_loading.loadingProgress(false)
                requireContext().showToast("Failed $e")
                Logger.e("failed", "Error adding document $e")
            }
    }

}