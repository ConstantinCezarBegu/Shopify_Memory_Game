package com.example.shopify_memory_game.adapters

import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.internal.getPosition

class RecyclerViewSelectionImageTracker(
    private val matchSize: Int
) {
    var cardsShuffledList = listOf<Card>()

    enum class CardsSelectionResponse {
        Add,
        Match,
        NoMatch
    }

    private val _cardsMatched: MutableList<RecyclerViewAdapter.ImageData> = mutableListOf()

    fun isCardFound(item: RecyclerViewAdapter.ImageData): Boolean {
        return item in _cardsMatched
    }

    fun isComplete() = cardsShuffledList.size == _cardsMatched.size

    private val _cardsSelected: MutableList<RecyclerViewAdapter.ImageData> = mutableListOf()


    fun isCardSelected(item: RecyclerViewAdapter.ImageData): Boolean {
        return item in _cardsSelected
    }

    fun clearSelected(): List<Int> {
        val toRefresh = _cardsSelected.getPosition()
        _cardsSelected.clear()
        return toRefresh
    }

    // modifies the list by adding or removing and notifies main activity what action to take
    fun modifyList(item: RecyclerViewAdapter.ImageData): CardsSelectionResponse {
        _cardsSelected.add(item)

        return if (areCardsTheSame()) {
            if (_cardsSelected.size == matchSize) {
                _cardsMatched.addAll(_cardsSelected)
                CardsSelectionResponse.Match
            } else {
                CardsSelectionResponse.Add
            }
        } else {
            CardsSelectionResponse.NoMatch
        }
    }

    private fun areCardsTheSame(): Boolean {
        val cardToMatch = _cardsSelected[0].product_id
        for (i in 0 until _cardsSelected.size) {
            if (_cardsSelected[i].product_id != cardToMatch) return false
        }
        return true
    }
}