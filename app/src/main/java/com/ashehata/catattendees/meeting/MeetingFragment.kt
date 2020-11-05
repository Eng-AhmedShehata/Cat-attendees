package com.ashehata.catattendees.meeting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ashehata.catattendees.R
import com.ashehata.catattendees.helper.Logger
import com.ashehata.catattendees.helper.MEETING_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_meeting.*
import javax.inject.Inject

@AndroidEntryPoint
class MeetingFragment : Fragment(), MeetingAdapter.OnDay {

    private lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var meetingAdapter: MeetingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meeting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFab()
        displayDays()
    }

    private fun displayDays() {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(MEETING_COLLECTION)
            .orderBy("dateMilliSecond", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener { task ->
                pb_loading_days.isVisible = false
                if (task.isSuccessful) {
                    val data = mutableListOf<MeetingDay>()
                    for (document in task.result!!) {
                        data.add(getDayInfo(document.data, document.id))
                    }
                    meetingAdapter.submitList(data)

                } else {
                    Logger.e("Failed", "Error getting documents.")

                }
            }.addOnFailureListener {
                pb_loading_days.isVisible = false
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    private fun setFab() {
        rv_meetings_days.adapter = meetingAdapter
        rv_meetings_days.setHasFixedSize(true)
        meetingAdapter.setUpClicked(this)

        fab_add_day.setOnClickListener {
            findNavController().navigate(R.id.action_meetingFragment_to_addDayFragment)
        }
    }

    override fun onDayClicked(id: String) {
        val action = MeetingFragmentDirections.actionMeetingFragmentToAddMemberFragment(id)
        findNavController().navigate(action)
    }

}