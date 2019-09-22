package com.example.shopify_memory_game.internal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.data.network.request.Image

val List<Card>.productIds: List<Long>
    get() = map(Card::id)


fun List<Card>.getImages(): List<Image> {
    return this.map { it.image }
}

fun ViewGroup.inflate(
    @LayoutRes layoutId: Int,
    inflater: LayoutInflater = LayoutInflater.from(context),
    attachToRoot: Boolean = false
): View {
    return inflater.inflate(layoutId, this, attachToRoot)
}

// Does not get triggered the first time
fun <T> LiveData<T>.observeChange(
    lifecycleOwner: LifecycleOwner,
    observer: Observer<T>
) {
    var ignoredCount = 0
    observe(lifecycleOwner, Observer<T> { t ->
        if (ignoredCount++ > 0) {
            observer.onChanged(t)
        }
    })
}