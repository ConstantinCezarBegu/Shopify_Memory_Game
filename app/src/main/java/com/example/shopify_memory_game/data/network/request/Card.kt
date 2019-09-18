package com.example.shopify_memory_game.data.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Card(
    @Json(name = "id")
    val id: Long,
    @Json(name = "image")
    val image: Image
)