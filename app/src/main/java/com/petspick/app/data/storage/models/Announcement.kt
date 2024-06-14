package com.petspick.app.data.storage.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.petspick.app.utils.Const

@Entity(tableName = Const.TABLE_NAME)
data class Announcement(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String = "",
    @ColumnInfo(name = "userId") var userId: String = "",
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "sex") val sex: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "age") val age: String = "",
    @ColumnInfo(name = "type") val type: String = "",
    @ColumnInfo(name = "price") val price: String = "",
    @ColumnInfo(name = "image") val image: String = "",
    @ColumnInfo(name = "description") val description: String = "",
)