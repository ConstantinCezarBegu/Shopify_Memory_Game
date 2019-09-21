package com.example.shopify_memory_game.data.network.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "src")
    val src: String
)
