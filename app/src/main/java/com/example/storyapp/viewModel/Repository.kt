package com.example.storyapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.storyapp.data.*
import com.example.storyapp.network.ApiConfig
import com.example.storyapp.network.ApiService
import com.example.storyapp.paging.Database
import com.example.storyapp.paging.Mediator
import com.example.storyapp.utils.UserPref
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface IRepository {
    val next: MutableLiveData<Handler<String>>
    val story: MutableLiveData<ArrayList<Story>>
    val detail: MutableLiveData<Story>
    val postStory: MutableLiveData<Handler<String>>
    val _storyList: MutableLiveData<List<Story>?>
    val storyList: LiveData<List<Story>?>
    val uploadStory: LiveData<Handler<String>>
    val detailStory : LiveData<Story>
    val nextGo: LiveData<Handler<String>>
    val listStory: LiveData<ArrayList<Story>>
    val location: String
    fun register(email: String, username: String, password: String)
    fun login(email: String, password: String)
    fun getAllStory() : LiveData<PagingData<Story>>
    fun getStory(id: String)
    fun postStory(description: RequestBody, file: MultipartBody.Part)
    fun loadStories()
}

class Repository(private val database: Database, private val apiService: ApiService, private val userPref: UserPref) :
    IRepository {

    override val next = MutableLiveData<Handler<String>>()
    override val story = MutableLiveData<ArrayList<Story>>()
    override val detail = MutableLiveData<Story>()
    override val postStory = MutableLiveData<Handler<String>>()
    override val _storyList = MutableLiveData<List<Story>?>()

    override val storyList: LiveData<List<Story>?> = _storyList
    override val uploadStory: LiveData<Handler<String>> = postStory
    override val detailStory : LiveData<Story> = detail
    override val nextGo: LiveData<Handler<String>> = next
    override val listStory: LiveData<ArrayList<Story>> = story

    companion object {
        private const val TAG = "USER_REPOSIRTORY"
    }

    override fun register(email: String, username: String, password: String) {
        val client = ApiConfig.getApiService().signin(email, username, password)
        client.enqueue(object : Callback<Signin> {
            override fun onResponse(call: Call<Signin>, response: Response<Signin>) {
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

    override fun login(email: String, password: String) {
        val client = ApiConfig.getApiService().login(email, password)
        client.enqueue(object : Callback<Login> {
            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        userPref.saveToken(response.body()!!.User.token)
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

    override fun getAllStory() : LiveData<PagingData<Story>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 10
            ),
            remoteMediator = Mediator(database, apiService, userPref),
            pagingSourceFactory = {
                database.storyDao().getAllStory()
            }
        ).liveData
    }

    override fun getStory(id: String) {
        val client = ApiConfig.getApiService().getDetailStory("Bearer ${userPref.getToken()}", id)
        client.enqueue(object : Callback<DetailStory> {
            override fun onResponse(call: Call<DetailStory>, response: Response<DetailStory>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        detail.value = response.body()?.story
                    }
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailStory>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    override fun postStory(description: RequestBody, file: MultipartBody.Part) {
        val client = ApiConfig.getApiService().uploadStory("Bearer ${userPref.getToken()}", description, file)
        client.enqueue(object : Callback<PostStory> {
            override fun onResponse(call: Call<PostStory>, response: Response<PostStory>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        postStory.value = Handler(response.body()!!.message)
                    }
                }
                else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<PostStory>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    override val location = "1.0"

    override fun loadStories() {
        val client = ApiConfig.getApiService().getStoriesByLocation("Bearer ${userPref.getToken()}", location)
        client.enqueue(object : Callback<ListStory> {
            override fun onResponse(call: Call<ListStory>, response: Response<ListStory>) {
                if (response.isSuccessful) {
                    val stories = response.body()
                    if (stories != null) {
                        _storyList.value = response.body()?.listStory
                    }
                } else {
                    Log.e("StoryViewModel", "Failed to retrieve stories: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<ListStory>, t: Throwable) {
                Log.e("StoryViewModel", "Failed to retrieve stories", t)
            }
        })
    }
}