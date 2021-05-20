package com.example.arview.network

import com.example.arview.network.dto.GamesResponse
import retrofit2.Response
import retrofit2.http.GET

interface Api {
    @GET("games/top")
    suspend fun getGamesList(): Response<GamesResponse>
}