package com.example.shopify_memory_game.ui

import android.service.autofill.UserData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shopify_memory_game.adapters.RecyclerViewSelectionImageTracker
import com.example.shopify_memory_game.data.repository.Repository
import com.example.shopify_memory_game.internal.lazyDeferred

class MainActivityViewModel(
    private val repository: Repository,
    private val userData: com.example.shopify_memory_game.data.preference.UserData,
    private val handle: SavedStateHandle
) : ViewModel() {

    val imagesRecyclerViewTracker = RecyclerViewSelectionImageTracker{
        userData.userScore += 10
    }

    val imageList by lazyDeferred {
        userData.userScore = 0
        repository.getImages()
    }


}