package com.example.storyapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.*
import com.example.storyapp.network.ApiConfig
import com.example.storyapp.utils.UserPref
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UniversalVM(private val userPreference: UserPref): ViewModel() {

    companion object {
        private const val TAG = "Login"
        private const val TAGS = "Main"
        private const val TAGSS = "Detail"
        private const val TAGSSS = "CAMERA_ACTIVITY"
    }
    private val next = MutableLiveData<Handler<String>>()
    val nextGo: LiveData<Handler<String>> = next

    private val story = MutableLiveData<ArrayList<Story>>()
    val listStory: LiveData<ArrayList<Story>> = story
    private val detail = MutableLiveData<Story>()
    val detailStory : LiveData<Story> = detail
    val postStory = MutableLiveData<Handler<String>>()
    val uploadStory: LiveData<Handler<String>> = postStory

    fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        userPreference.saveToken(response.body()!!.User.token)
                        next.value = Handler(response.body()!!.User.name)
                    }

                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Login>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun getAllStory() {
        val client = ApiConfig.getApiService().getStory("Bearer ${userPreference.getToken()}")
        client.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        story.value= response.body()?.listStory
                    }
                }
                else {
                    Log.e(TAGS, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListStory>, t: Throwable) {
                Log.e(TAGS, "onFailure: ${t.message}")
            }
        })
    }

    fun getStory(id: String) {
        val client = ApiConfig.getApiService().getDetailStory("Bearer ${userPreference.getToken()}", id)
        client.enqueue(object : Callback<DetailStory> {
            override fun onResponse(call: Call<DetailStory>, response: Response<DetailStory>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        detail.value = response.body()?.story
                    }
                }
                else {
                    Log.e(TAGSS, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStory>, t: Throwable) {
                Log.e(TAGSS, "onFailure: ${t.message}")
            }

        })
    }

    fun postStory(description: RequestBody, file: MultipartBody.Part) {
        val client = ApiConfig.getApiService().uploadStory("Bearer ${userPreference.getToken()}", description, file)
        client.enqueue(object : Callback<PostStory> {
            override fun onResponse(call: Call<PostStory>, response: Response<PostStory>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        postStory.value = Handler(response.body()!!.message)
                    }
                }
                else {
                    Log.e(TAGSSS, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostStory>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

}

