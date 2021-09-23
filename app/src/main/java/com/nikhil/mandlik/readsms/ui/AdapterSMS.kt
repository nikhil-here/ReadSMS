package com.nikhil.mandlik.readsms.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nikhil.mandlik.readsms.databinding.ListItemSmsBinding
import com.nikhil.mandlik.readsms.entity.SMS

class AdapterSMS () : ListAdapter<SMS, AdapterSMS.SMSViewHolder>(SMS_COMPARATOR) {

    inner class SMSViewHolder(private val binding: ListItemSmsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sms: SMS) {
            binding.tvID.text = "ID : ${sms.id}"
            binding.tvBody.text = sms.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SMSViewHolder {
        val binding =
            ListItemSmsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SMSViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SMSViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val SMS_COMPARATOR = object : DiffUtil.ItemCallback<SMS>() {
            override fun areItemsTheSame(oldItem: SMS, newItem: SMS): Boolean =
                oldItem.id.equals(newItem.id)

            override fun areContentsTheSame(oldItem: SMS, newItem: SMS): Boolean =
                oldItem.body.equals(newItem.body)
        }
    }

}

