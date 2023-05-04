package com.example.storyapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.storyapp.R
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.databinding.FragmentSigninBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    val fragmentManager = supportFragmentManager
    val signinFragment = SigninFragment()
    val fragments = fragmentManager.findFragmentByTag(SigninFragment::class.java.simpleName)
    val loginFragment = LoginFragment()
    val fragmentl = fragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

}