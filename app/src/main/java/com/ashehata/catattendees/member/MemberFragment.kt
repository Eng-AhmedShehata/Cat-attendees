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
import com.ashehata.catattendees.databinding.FragmentAddMemberBinding
import com.ashehata.catattendees.databinding.FragmentMeetingBinding
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
    private var binding: FragmentAddMemberBinding? = null

    @Inject
    lateinit var memberAdapter: MemberAdapter

    val args: MemberFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMemberBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdd(args.documentID)
        // display data
        setupRv()
        displayMembers(args.documentID)
    }

    private fun setupRv() {
        binding?.rvMembers?.apply {
            adapter = memberAdapter
            setHasFixedSize(true)
        }
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
                    binding?.tvTotal?.append("${data.size}")
                    memberAdapter.submitList(data)
                } else {
                    Logger.e("Failed", "Error getting documents.")
                }
            }.addOnFailureListener {}

    }

    private fun setAdd(documentID: String) {
        binding?.addMember?.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}