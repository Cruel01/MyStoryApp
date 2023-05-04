package com.example.storyapp.data

import com.google.gson.annotations.SerializedName

data class Login(
    @field:SerializedName("loginResult")
    val User: User,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)