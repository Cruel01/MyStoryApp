package com.example.storyapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Signin
import com.example.storyapp.network.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignVM: ViewModel() {

    companion object {
        private const val TAG = "RegisterActivity"
    }

    fun register(email: String, username: String, password: String) {
        val client = ApiConfig.getApiService().signin(email, username, password)
        client.enqueue(object : Callback<Signin> {
            override fun onResponse(
                call: Call<Signin>,
                response: Response<Signin>
            ) {
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e(TAG, "on Failure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Signin>, t: Throwable) {
                Log.d(TAG, "onFailure : ${t.message}")
            }
        })
    }
}