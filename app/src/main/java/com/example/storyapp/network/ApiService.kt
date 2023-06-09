package com.example.storyapp.network

import com.example.storyapp.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("login")
    @FormUrlEncoded
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<Login>

    @POST("register")
    @FormUrlEncoded
    fun signin(
        @Field("email") email: String,
        @Field("name") name: String,
        @Field("password") password: String
    ): Call<Signin>

    @GET("stories")
    fun getStory(
        @Header("Authorization") token: String,
    ): Call<ListStory>

    @GET("stories/{id}")
    fun getDetailStory(
        @Header("Authorization") token: String?,
        @Path("id") id : String
    ) : Call<DetailStory>

    @GET("stories")
    suspend fun getAllStory(
        @Header("Authorization") token : String?,
        @Query("page") page : Int,
        @Query("size") size : Int,
    ) : Response<ListStory>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization") token: String?,
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part
    ) : Call<PostStory>

    @GET("stories")
    fun getStoriesByLocation(
        @Header("Authorization") token: String?,
        @Query("location") location: String
    ): Call<ListStory>
}