package com.example.shopify_memory_game.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Card(
    @Json(name = "id")
    val id: Long,
    @Json(name = "image")
    val image: Image
)