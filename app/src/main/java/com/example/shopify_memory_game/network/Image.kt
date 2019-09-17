package com.example.shopify_memory_game.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
"id": 5641966979,
        "product_id": 2759137027,
        "position": 1,
        "created_at": "2015-09-23T20:48:54-04:00",
        "updated_at": "2015-09-23T20:48:54-04:00",
        "alt": null,
        "width": 300,
        "height": 300,
        "src": "https://cdn.shopify.com/s/files/1/1000/7970/products/Aerodynamic_20Concrete_20Clock.png?v=1443055734",
 */
@JsonClass(generateAdapter = true)
data class Image(
    @Json(name = "id")
    val id: Long,
    @Json(name = "product_id")
    val product_id: Long,
    @Json(name = "width")
    val width: Int,
    @Json(name = "height")
    val height: Int,
    @Json(name = "src")
    val src: String
)
