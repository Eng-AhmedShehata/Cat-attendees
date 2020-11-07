package com.ashehata.catattendees.member

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ashehata.catattendees.R
import com.ashehata.catattendees.qrCode.generator.Member
import kotlinx.android.synthetic.main.root_member.view.*
import javax.inject.Inject

class MemberAdapter @Inject constructor() :
    ListAdapter<Member, MemberAdapter.MemberViewHolder>(DiffCallBackMember()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder(LayoutInflater.from(parent.context), parent)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = getItem(position)
        holder.bind(member)
    }

    class MemberViewHolder(inflater: LayoutInflater, private val parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.root_member, parent, false)) {

        private var name: TextView = itemView.tv_name
        private var level: TextView = itemView.tv_level

        fun bind(member: Member) {
            name.text = member.name
            level.text = member.level
        }
    }

    class DiffCallBackMember : DiffUtil.ItemCallback<Member>() {

        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }
    }
}