package com.example.dz16firebasedatabase

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dz16firebasedatabase.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            login()
        }
        binding.hasNotAccountTv.setOnClickListener {
            toSignUpFragment()
        }
    }

    private fun toSignUpFragment() {
        findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
    }

    private fun login() {
        val email = binding.emailEt.text.toString().trim()
        val pass = binding.passwordEt.text.toString().trim()

        if (email.isBlank() || pass.isBlank()) {
            Toast.makeText(requireContext(), "Email и пароль не могут быть пустыми.", Toast.LENGTH_LONG).show()
            return
        }

        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(requireActivity()) {
            if (it.isSuccessful) {
                Toast.makeText(requireContext(), "Вы успешно зашли в учетную запись", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_loginFragment_to_contactsFragment)
            }
            else {
                Toast.makeText(requireContext(), "Не удалось войти в учетную запись", Toast.LENGTH_LONG).show()
                binding.hasNotAccountTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
