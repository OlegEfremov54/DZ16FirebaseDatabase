package com.example.dz16firebasedatabase

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(private val context: Context) : RecyclerView.Adapter<ContactAdapter.NoteViewHolder>(){

    private val contacts = mutableListOf<ContactModel>()
    private var _onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        _onItemClickListener = onItemClickListener
    }

    fun updateList(newList: List<ContactModel>) {
        contacts.clear()
        contacts.addAll(newList)
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTV : TextView = itemView.findViewById(R.id.name_tv)
        private val phoneTV: TextView = itemView.findViewById(R.id.phone_tv)

        fun bind(contact: ContactModel) {
            nameTV.text = contact.name
            phoneTV.text = contact.phone
            itemView.setOnClickListener { _onItemClickListener?.onClick(contact) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val viewHolder = NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.item_contact, parent, false))
        return viewHolder
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentEmail = contacts[position]
        holder.bind(currentEmail)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun interface OnItemClickListener {
        fun onClick(contact: ContactModel)
    }
}