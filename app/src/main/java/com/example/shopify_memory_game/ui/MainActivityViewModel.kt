package com.example.shopify_memory_game.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shopify_memory_game.adapters.RecyclerViewSelectionImageTracker
import com.example.shopify_memory_game.data.network.request.Image
import com.example.shopify_memory_game.data.repository.Repository
import com.example.shopify_memory_game.internal.lazyDeferred

class MainActivityViewModel(
    private val repository: Repository,
    private val userData: com.example.shopify_memory_game.data.preference.UserData,
    private val handle: SavedStateHandle
) : ViewModel() {

    val imagesRecyclerViewTracker = RecyclerViewSelectionImageTracker(
        userData.matchSize
    )

    val imageList by lazyDeferred {
        userData.userScore = 0
        MutableLiveData(cardSelection(repository.getImages()))
    }

    var userScore: Int
        set(value) {
            userData.userScore = value
        }
        get() {
            return userData.userScore
        }

    var gridSize: Int
        set(value) {
            userData.gridSize = value
        }
        get() {
            return userData.gridSize
        }

    var matchSize: Int
        set(value) {
            userData.matchSize = value
        }
        get() {
            return userData.matchSize
        }

    private fun cardSelection(allImages: List<Image>): List<Image> {
        val selectedList =
            allImages.shuffled().take((20 + gridSize * 20) / matchSize)
        var finalList = listOf<Image>()
        for (i in 1..matchSize) {
            finalList = finalList + selectedList
        }
        return (finalList).shuffled()
    }


}