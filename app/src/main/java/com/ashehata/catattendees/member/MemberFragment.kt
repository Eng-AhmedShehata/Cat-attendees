package com.ashehata.catattendees.member

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ashehata.catattendees.R
import com.ashehata.catattendees.helper.*
import com.ashehata.catattendees.meeting.MeetingDay
import com.ashehata.catattendees.meeting.getDayInfo
import com.ashehata.catattendees.qrCode.generator.Member
import com.ashehata.catattendees.qrCode.reader.DecoderActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_decoder.*
import kotlinx.android.synthetic.main.fragment_add_member.*
import kotlinx.android.synthetic.main.fragment_meeting.*
import javax.inject.Inject

@AndroidEntryPoint
class MemberFragment : Fragment() {

    private lateinit var firebaseFirestore: FirebaseFirestore

    @Inject
    lateinit var memberAdapter: MemberAdapter

    val args: MemberFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_member, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdd(args.documentID)
        // display data
        setupRv()
        displayMembers(args.documentID)
    }

    private fun setupRv() {
        rv_members.adapter = memberAdapter
        rv_members.setHasFixedSize(true)
    }

    private fun displayMembers(documentID: String) {
        firebaseFirestore = FirebaseFirestore.getInstance()
        firebaseFirestore.collection(MEETING_COLLECTION)
            .document(documentID)
            .collection(MEMBER_COLLECTION)
            .orderBy("name", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val data = mutableListOf<Member>()
                    for (document in task.result!!) {
                        data.add(document.toObject(Member::class.java))
                    }
                    tv_total.append("${data.size}")
                    memberAdapter.submitList(data)
                } else {
                    Logger.e("Failed", "Error getting documents.")
                }
            }.addOnFailureListener {}

    }

    private fun setAdd(documentID: String) {
        add_member.setOnClickListener {
            if (requireActivity().checkCameraPermission()) {
                //main logic or main code
                startActivity(Intent(requireContext(), DecoderActivity::class.java).apply {
                    putExtra("docID", documentID)
                })
            } else {
                requireActivity().requestPermission();
            }
        }
    }

}