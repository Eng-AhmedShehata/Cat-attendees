package com.ashehata.catattendees.addMember

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.ashehata.catattendees.R
import com.ashehata.catattendees.helper.checkCameraPermission
import com.ashehata.catattendees.helper.requestPermission
import com.ashehata.catattendees.qrCode.reader.DecoderActivity
import kotlinx.android.synthetic.main.fragment_add_member.*


class AddMemberFragment : Fragment() {

    val args: AddMemberFragmentArgs by navArgs()

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