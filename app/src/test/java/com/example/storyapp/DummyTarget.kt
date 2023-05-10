package com.example.storyapp

import com.example.storyapp.data.Story

object DummyTarget {
    fun generateDummyStoriesResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val stories = Story(
                "photoUrl + $i",
                "createdAt + $i",
                "name + $i",
                "description + $i",
                i.toDouble(),
                i.toString(),
                i.toDouble()
            )
            items.add(stories)
        }
        return items
    }
}