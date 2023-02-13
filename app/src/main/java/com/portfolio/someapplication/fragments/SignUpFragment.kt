package com.portfolio.someapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.portfolio.someapplication.R
import com.portfolio.someapplication.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = _binding ?: throw RuntimeException("FragmentSignUpBinding == null")

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }
        binding.btnLogin.setOnClickListener {
            launchLogInFragment()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
        val fab: FloatingActionButton = (activity as AppCompatActivity).findViewById(R.id.fab)
        fab.visibility = View.INVISIBLE
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
        val fab: FloatingActionButton = (activity as AppCompatActivity).findViewById(R.id.fab)
        fab.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun signUpUser() {
        val email = binding.etEmailAddress.text.toString()
        val password = binding.etPassword.text.toString()
        val confirmPassword = binding.etConfirmPassword.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(
                requireContext(),
                "Email and Password can't be blank",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(
                requireContext(),
                "Password and Confirm Password do not match",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) {
                if(it.isSuccessful){
                    Toast.makeText(
                        requireContext(),
                        "Login Successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    launchMainFragment()
//                    finish()
                }
                else{
                    Toast.makeText(
                        requireContext(),
                        "Error creating user.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun launchLogInFragment() {
        findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
    }

    private fun launchMainFragment() {
        findNavController().navigate(R.id.action_signUpFragment_to_mainFragment)
    }
}