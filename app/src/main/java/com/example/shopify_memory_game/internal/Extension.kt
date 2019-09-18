package com.example.shopify_memory_game.internal

import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.data.network.request.Image

val List<Image>.productIds: List<Long>
    get() = map(Image::product_id)


fun List<Card>.getImages(): List<Image>{
    return this.map { it.image }
}