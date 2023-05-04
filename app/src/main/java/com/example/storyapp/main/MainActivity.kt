package com.example.storyapp.main

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.databinding.FragmentSigninBinding
import com.example.storyapp.utils.UserPref
import kotlin.math.sign

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var userPref: UserPref

    val fragmentManager = supportFragmentManager
    val signinFragment = SigninFragment()
    val fragments = fragmentManager.findFragmentByTag(SigninFragment::class.java.simpleName)
    val loginFragment = LoginFragment()
    val fragmentl = fragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animation()

        binding.signin.setOnClickListener {
            if (fragments !is SigninFragment) {
                Log.d("MyFlexibleFragment", "Fragment Name :" + SigninFragment::class.java.simpleName)
                fragmentManager
                    .beginTransaction()
                    .add(R.id.main_frame, signinFragment, SigninFragment::class.java.simpleName)
                    .commit()
            }
            binding.login.visibility = View.GONE
            binding.signin.visibility = View.GONE
        }

        binding.login.setOnClickListener {
            if (fragmentl !is LoginFragment) {
                fragmentManager
                    .beginTransaction()
                    .add(R.id.main_frame, loginFragment, LoginFragment::class.java.simpleName)
                    .commit()
            }

            binding.login.visibility = View.GONE
            binding.signin.visibility = View.GONE
        }
    }

    private fun next() {
        val intent = Intent(this@MainActivity, HomeActivity::class.java)
        startActivity(intent)
    }

    fun animation() {
        val judul = ObjectAnimator.ofFloat(binding.textView, View.ALPHA, 1f).setDuration(300)
        val gambar = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(200)
        val desc = ObjectAnimator.ofFloat(binding.imageView2, View.ALPHA, 1f).setDuration(300)
        val signin = ObjectAnimator.ofFloat(binding.signin, View.ALPHA, 1f).setDuration(200)
        val login = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(200)


        val judul1 = AnimatorSet().apply {
            playTogether(judul, gambar, desc)
        }

        val button1 = AnimatorSet().apply {
            playTogether(signin, login)
        }

        AnimatorSet().apply {
            playSequentially(judul, judul1, button1)
            start()
        }
    }
}