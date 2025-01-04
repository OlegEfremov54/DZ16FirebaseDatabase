package com.example.dz16firebasedatabase

data class ContactModel(val name: String? = null, val phone: String? = null) {
    override fun toString(): String {
        return "$name, $phone"
    }
}