package com.example.shopify_memory_game.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.shopify_memory_game.adapters.RecyclerViewSelectionImageTracker
import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.data.repository.Repository
import com.example.shopify_memory_game.internal.lazyDeferred

class MainActivityViewModel(
    private val repository: Repository,
    private val userData: com.example.shopify_memory_game.data.preference.UserData,
    private val handle: SavedStateHandle
) : ViewModel() {

    companion object {
        const val POSITION_RECYCLER_VIEW = "positionRecyclerView"
    }

    private var cardsShuffledList = listOf<Card>()
    fun cardsShuffledList(imageList: List<Card>): List<Card> {
        if (cardsShuffledList.isEmpty()) cardsShuffledList = cardSelection(imageList)
        return cardsShuffledList
    }

    var positionRecyclerView: Int
        get() = handle.get<Int>(POSITION_RECYCLER_VIEW) ?: 0
        set(value) {
            handle.set(POSITION_RECYCLER_VIEW, value)
        }

    val imagesRecyclerViewTracker = RecyclerViewSelectionImageTracker(
        userData.matchSize
    )

    val imageList by lazyDeferred {
        userData.userScore = 0
        repository.getImages()
    }

    val errorLiveData = repository.errorLiveData

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

    private fun cardSelection(allCards: List<Card>): List<Card> {
        val selectedList =
            allCards.shuffled().take((20 + gridSize * 20) / matchSize)
        var finalList = listOf<Card>()
        for (i in 1..matchSize) {
            finalList = finalList + selectedList
        }
        return (finalList).shuffled()
    }


}