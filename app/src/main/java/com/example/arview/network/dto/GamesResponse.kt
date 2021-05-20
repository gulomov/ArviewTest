package com.example.arview.network.dto

data class GamesResponse(
    val _total: Int = 0,
    val top: List<Top>
)

data class Top(
    val channels: Int,
    val viewers: Int,
    val game: Game
)

data class Game(
    val _id: Int,
    val box: Box,
    val giantbomb_id: Int,
    val logo: Box,
    val name: String
)

data class Box(
    val large: String,
    val medium: String,
    val small: String,
    val template: String,
)