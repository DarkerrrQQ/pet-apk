package com.petspick.app.data.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.petspick.app.utils.Const
import com.petspick.app.data.storage.dao.Dao
import com.petspick.app.data.storage.models.Announcement

@Database(entities = [Announcement::class], version = 2, exportSchema = true)
abstract class MainDatabase : RoomDatabase() {

    abstract fun dao(): Dao

    companion object {

        @Volatile
        private var INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MainDatabase::class.java,
                    Const.DB_NAME
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}