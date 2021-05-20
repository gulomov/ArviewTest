package com.example.arview.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game")
data class GameInfo(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "channel")
    var channel: Int,
    @ColumnInfo(name = "viewer")
    var viewer: Int,
    @ColumnInfo(name = "image")
    var image: String
)