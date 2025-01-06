package com.example.dz16firebasedatabase

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.dz16firebasedatabase.databinding.FragmentContactsBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.database


class ContactsFragment : Fragment(), ContactAdapter.OnItemClickListener {

    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    //private lateinit var parent: AppCompatActivity
    private var contactList = mutableListOf<ContactModel>()
    private var contactAdapter: ContactAdapter? = null
    private val DATABASE_NAME = "contacts"
    private val USER_ID: String? = FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        //parent = activity as AppCompatActivity
        contactAdapter = ContactAdapter(requireContext())
        contactAdapter!!.updateList(contactList)
        contactAdapter!!.setOnItemClickListener(this)
        binding.recycleView.adapter = contactAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readDatabase()
        binding.insertContactBtn.setOnClickListener {
            val name = binding.nameEt.text.toString().trim()
            val phone = binding.phoneEt.text.toString().trim()
            if (name.isBlank() && phone.isBlank()) {
                return@setOnClickListener
            }
            val contact = ContactModel(name, phone)
            insertContact(contact)
            with(binding) {
                nameEt.text.clear()
                phoneEt.text.clear()
            }
        }
        //parent.setSupportActionBar(binding.toolbar)
        //parent.supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    private fun insertContact(contact: ContactModel) {
        val database = Firebase.database.reference
            .child(DATABASE_NAME)
            .child(USER_ID!!)
        val map: HashMap<String, ContactModel> = HashMap()
        map[contact.name!!] = contact
        database.updateChildren(map as Map<String, Any>)
        readDatabase()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun readDatabase() {
        val dataSnapshot = Firebase.database.reference
            .child(DATABASE_NAME)
            .child(USER_ID!!)
            .get()
        dataSnapshot.addOnSuccessListener {
            contactList.clear()
            for (element in it.children) {
                val contact = element.getValue(ContactModel::class.java)!!
                contactList.add(contact)
            }
            contactAdapter!!.updateList(contactList)
            binding.recycleView.adapter!!.notifyDataSetChanged()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(contact: ContactModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Удаление контакта")
            .setMessage("Вы собираетесь удалить контакт: ${contact.name}")
            .setNeutralButton("Отмена", null)
            .setPositiveButton("Удалить") { dialog, _ ->
                removeUser(contact)
                readDatabase()
                dialog.dismiss()
            }
            .create().show()
    }

    private fun removeUser(contact: ContactModel) {
        Firebase.database.getReference()
            .child(DATABASE_NAME)
            .child(USER_ID!!)
            .child(contact.name!!)
            .removeValue()
    }
}