package com.example.arview.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.arview.data.GameInfo

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGame(gameInfo: GameInfo)

    @Query("Select * from game")
    fun getGame(): LiveData<List<GameInfo>>
}