package com.ashehata.catattendees.meeting

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ashehata.catattendees.R
import kotlinx.android.synthetic.main.root_meeting_day.view.*
import javax.inject.Inject

class MeetingAdapter @Inject constructor() :
    ListAdapter<MeetingDay, MeetingAdapter.MeetingViewHolder>(DiffCallBack()) {

    lateinit var onDay: OnDay

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeetingViewHolder {
        return MeetingViewHolder(LayoutInflater.from(parent.context), parent, onDay)
    }

    override fun onBindViewHolder(holder: MeetingViewHolder, position: Int) {
        val meetingDay = getItem(position)
        holder.bind(meetingDay)
    }
    interface OnDay {
        fun onDayClicked(id: String)
    }
    fun setUpClicked(onDay: OnDay) {
        this.onDay = onDay
    }

    class MeetingViewHolder(inflater: LayoutInflater, private val parent: ViewGroup, private val onDay: OnDay) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.root_meeting_day, parent, false)) {

        private var date: TextView = itemView.tv_date
        private var type: TextView = itemView.tv_type
        private var topic: TextView = itemView.tv_topic


        fun bind(meetingDay: MeetingDay) {
            topic.text = meetingDay.topic
            type.text = meetingDay.type
            date.text = meetingDay.getRealDate()

            itemView.setOnClickListener {
                onDay.onDayClicked(meetingDay.id)
            }
        }

    }
}