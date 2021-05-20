package com.example.arview.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.arview.data.dao.GameDao


    @Database(entities = [(GameInfo::class)], version = 1)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun gameDao(): GameDao
    }
