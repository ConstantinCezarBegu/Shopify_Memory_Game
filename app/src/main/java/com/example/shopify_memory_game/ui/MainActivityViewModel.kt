package com.example.shopify_memory_game.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.constantin.constaflux.data.repository.Repository
import com.example.shopify_memory_game.internal.lazyDeferred

class MainActivityViewModel(
    private val repository: Repository,
    private val handle: SavedStateHandle
) : ViewModel() {

    val imageList by lazyDeferred {
        repository.getImages()
    }


}