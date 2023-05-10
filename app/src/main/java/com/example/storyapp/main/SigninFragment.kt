package com.example.storyapp.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentSigninBinding
import com.example.storyapp.viewModel.UniversalVM

class SigninFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private val viewModel by viewModels<UniversalVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signup.visibility = View.VISIBLE

        binding.password.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    s.toString().length >= 8 -> {
                        buttonOn()
                    }
                    else -> {
                        buttonOff()
                    }
                }
            }
        })

        binding.signup.setOnClickListener {
            val username = binding.username.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            when {
                username.isEmpty() -> {
                    binding.username.error = "Isi Username"
                }
                email.isEmpty() -> {
                    binding.email.error = "Isi Email"
                }
                else -> viewModel.register(email, username, password)
            }
            next()
        }
    }

    private fun buttonOn() {
        val result = binding.password.text
        binding.signup.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun buttonOff() {
        binding.signup.isEnabled = false
    }

    private fun next() {
        val loginFragment = LoginFragment()
        val fragmentManager = childFragmentManager
        fragmentManager.beginTransaction().apply {
            replace(R.id.fragment_signin, loginFragment, LoginFragment::class.java.simpleName)
            addToBackStack(null)
            commit()
        }
        binding.signup.visibility = View.GONE
    }
}