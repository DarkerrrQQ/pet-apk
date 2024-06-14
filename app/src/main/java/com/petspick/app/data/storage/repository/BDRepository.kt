package com.petspick.app.data.storage.repository

import androidx.annotation.WorkerThread
import com.petspick.app.data.storage.dao.Dao
import com.petspick.app.data.storage.models.Announcement
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class BDRepository @Inject constructor (private val dao: Dao) {

    val all: Flow<List<Announcement>> = dao.getAll()

    @WorkerThread
    fun insert(item: Announcement) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(item)
        }
    }

    @WorkerThread
    fun delete(item: Announcement) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.delete(item)
        }
    }
}