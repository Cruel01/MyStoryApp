package com.example.storyapp.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.utils.UserPref
import com.example.storyapp.viewModel.UniversalFactory
import com.example.storyapp.viewModel.UniversalVM

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userPref: UserPref
    private lateinit var loginVM: UniversalVM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userPref = UserPref(this.requireActivity())
        loginVM = ViewModelProvider(this.requireActivity(), UniversalFactory(userPref))[UniversalVM::class.java]

        if (userPref.getToken() != null) {
            next()
        }

        binding.passin.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                when {
                    s.toString().length >= 8 -> {
                        buttonOn()
                    }
                    else ->
                        buttonOff()
                }
            }
        })

        binding.loginn.setOnClickListener {
            val email = binding.usernamae.text.toString()
            val password = binding.passin.text.toString()
            when {
                email.isEmpty() -> {
                    binding.usernamae.error = "Isi Email"
                }
                else -> loginVM.login(email, password)
            }

            if (userPref.getToken() != null) {
                next()
            }
        }

        binding.regis.setOnClickListener {
            val signinFragment = SigninFragment()
            val fragmentManager = childFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragment_login, signinFragment, SigninFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }

            binding.loginn.visibility = View.GONE
        }
    }

    private fun next() {
        activity?.let {
            val intent = Intent(it, HomeActivity::class.java)
            it.startActivity(intent)
        }
    }

    private fun buttonOn() {
        val result = binding.passin.text
        binding.loginn.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun buttonOff() {
        binding.loginn.isEnabled = false
    }
}