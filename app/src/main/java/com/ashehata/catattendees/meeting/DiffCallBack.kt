package com.ashehata.catattendees.meeting

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

class DiffCallBack: DiffUtil.ItemCallback<MeetingDay>() {

    override fun areItemsTheSame(oldItem: MeetingDay, newItem: MeetingDay): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MeetingDay, newItem: MeetingDay): Boolean {
        return oldItem == newItem
    }
}

