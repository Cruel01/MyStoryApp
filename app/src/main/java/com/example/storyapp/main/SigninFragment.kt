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
import androidx.fragment.app.viewModels
import com.example.storyapp.R
import com.example.storyapp.databinding.FragmentLoginBinding
import com.example.storyapp.databinding.FragmentSigninBinding
import com.example.storyapp.viewModel.SignVM

class SigninFragment : Fragment() {

    private lateinit var binding: FragmentSigninBinding
    private val signVM by viewModels<SignVM>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        return binding.root
        animation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animation()

        binding.signup.visibility = View.VISIBLE

        binding.password.addTextChangedListener(object: TextWatcher {
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
                else -> signVM.register(email, username, password)
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

    fun animation() {
        val judul = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(300)
        val txt_username = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(200)
        val txt_email = ObjectAnimator.ofFloat(binding.textView5, View.ALPHA, 1f).setDuration(300)
        val txt_password = ObjectAnimator.ofFloat(binding.textView6, View.ALPHA, 1f).setDuration(300)
        val username = ObjectAnimator.ofFloat(binding.username, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.email, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.password, View.ALPHA, 1f).setDuration(300)
        val button = ObjectAnimator.ofFloat(binding.signup, View.ALPHA, 1f).setDuration(300)

        val judul1 = AnimatorSet().apply {
            play(judul)
        }

        val button1 = AnimatorSet().apply {
            play(button)
        }

        val together = AnimatorSet().apply {
            playTogether(txt_email, txt_username, txt_password, email, username, password)
        }

        AnimatorSet().apply {
            playSequentially(judul1, together, button1)
            start()
        }
    }
}