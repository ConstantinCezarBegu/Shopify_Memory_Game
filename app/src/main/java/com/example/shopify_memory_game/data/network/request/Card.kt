package com.example.shopify_memory_game.data.network.request

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(
    tableName = "memory_card",
    indices = [Index(value = ["id"])]
)
@JsonClass(generateAdapter = true)
data class Card(
    @PrimaryKey(autoGenerate = false)
    @Json(name = "id")
    val id: Long,
    @Json(name = "title")
    val title: String,
    @Embedded
    @Json(name = "image")
    val image: Image
)