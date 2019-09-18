package com.example.shopify_memory_game.data.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CardsListContainer(
    @Json(name = "products")
    val Cards: List<Card>
)