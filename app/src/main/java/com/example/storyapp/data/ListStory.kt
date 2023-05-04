package com.example.storyapp.data

import com.google.gson.annotations.SerializedName

data class ListStory(
    @field:SerializedName("listStory")
    val listStory: ArrayList<Story>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)
