package com.example.storyapp.paging

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.storyapp.data.Story

@Database(entities = [Story::class, Remote::class], version = 2, exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun storyDao() : StoryDAO
    abstract fun remoteDao() : RemoteDAO

    companion object {
        @Volatile
        private var INSTANCE: com.example.storyapp.paging.Database? = null

        @JvmStatic
        fun getDatabase(context: Context): com.example.storyapp.paging.Database {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    com.example.storyapp.paging.Database::class.java,"Database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}