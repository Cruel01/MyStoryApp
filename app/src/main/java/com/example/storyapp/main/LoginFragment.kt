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
        childFragmentManager
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        animation()

        userPref = UserPref(this.requireActivity())
        loginVM = ViewModelProvider(this.requireActivity(), UniversalFactory(userPref))[UniversalVM::class.java]

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
            val fragmentManager = parentFragmentManager
            fragmentManager.beginTransaction().apply {
                replace(R.id.fragment_login, signinFragment, SigninFragment::class.java.simpleName)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun next() {
        activity?.let {
            val intent = Intent(it, HomeActivity::class.java)
            it.startActivity(intent)
        }
    }

    private fun animation() {
        val judul = ObjectAnimator.ofFloat(binding.textView3, View.ALPHA, 1f).setDuration(300)
        val txt_username = ObjectAnimator.ofFloat(binding.textView4, View.ALPHA, 1f).setDuration(200)
        val txt_password = ObjectAnimator.ofFloat(binding.textView5, View.ALPHA, 1f).setDuration(200)
        val regis = ObjectAnimator.ofFloat(binding.regis, View.ALPHA, 1f). setDuration(300)
        val username = ObjectAnimator.ofFloat(binding.usernamae, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.passin, View.ALPHA, 1f).setDuration(300)
        val button = ObjectAnimator.ofFloat(binding.loginn, View.ALPHA, 1f).setDuration(300)

        val judul1 = AnimatorSet().apply {
            play(judul)
        }

        val button1 = AnimatorSet().apply {
            play(button)
        }

        val together = AnimatorSet().apply {
            playTogether(txt_username, txt_password, username, password, regis)
        }

        AnimatorSet().apply {
            playSequentially(judul1, together, button1)
            start()
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