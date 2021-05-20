package com.example.arview.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.arview.data.GameInfo
import com.example.arview.data.dao.GameDao
import com.example.arview.network.Api
import com.example.arview.network.Resource
import com.example.arview.network.dto.Game
import com.example.arview.network.isNetworkAvailable
import com.example.arview.network.pojo.ErrorResponse
import com.example.arview.network.safeApiCall
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import retrofit2.Converter

class Repository constructor(
    var errorConverter: Converter<ResponseBody, ErrorResponse>,
    private val api: Api,
    private val db: GameDao,
    private val context: Context

) {

    suspend fun getTopGames() = flow {
        emit(Resource.Loading)
        emit(safeApiCall(errorConverter) { api.getGamesList() })
    }

    fun getGamesFromDb(): LiveData<List<GameInfo>> {
        return db.getGame()
    }

    suspend fun insert(gameInfo: GameInfo) {
        db.saveGame(gameInfo)
        Log.d("here", " 2 ${gameInfo.toString()}")
    }
}