package com.example.storyapp.paging

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.storyapp.data.Story

@Dao
interface StoryDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(stories: List<Story>)

    @Query("SELECT * FROM Story")
    fun getAllStory(): PagingSource<Int, Story>

    @Query("DELETE FROM Story")
    suspend fun deleteAll()
}