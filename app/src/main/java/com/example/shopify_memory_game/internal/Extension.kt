package com.example.shopify_memory_game.internal

import com.example.shopify_memory_game.data.network.Image

val List<Image>.productIds: List<Long>
    get() = map(Image::product_id)
