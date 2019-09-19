package com.example.shopify_memory_game.internal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.data.network.request.Image

val List<Image>.productIds: List<Long>
    get() = map(Image::product_id)


fun List<Card>.getImages(): List<Image>{
    return this.map { it.image }
}

fun ViewGroup.inflate(
    @LayoutRes layoutId: Int,
    inflater: LayoutInflater = LayoutInflater.from(context),
    attachToRoot: Boolean = false
): View {
    return inflater.inflate(layoutId, this, attachToRoot)
}