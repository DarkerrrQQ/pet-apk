package com.petspick.app.data.storage.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.petspick.app.data.storage.models.Announcement
import com.petspick.app.utils.Const
import kotlinx.coroutines.flow.Flow

@Dao
interface Dao {
    @Query("SELECT * FROM ${Const.FAVORITE}")
    fun getAll(): Flow<List<Announcement>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(recipe: Announcement)

    @Delete
    suspend fun delete(recipe: Announcement)
}