package com.example.storyapp.paging

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertALL(remoteKey: List<Remote>)

    @Query("SELECT * FROM remote WHERE id = :id")
    suspend fun getKeyId(id: String): Remote?

    @Query("DELETE FROM remote")
    suspend fun deleteKey()
}