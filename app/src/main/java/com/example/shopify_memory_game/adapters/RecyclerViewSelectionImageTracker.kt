package com.example.shopify_memory_game.adapters

import android.util.Log

class RecyclerViewSelectionImageTracker(private val matchFunction: () -> Unit) {

    private val _cardsMatched: MutableList<RecyclerViewAdapter.ImageData> = mutableListOf()
    val cardsMatched: List<RecyclerViewAdapter.ImageData>
        get() {
            return _cardsMatched.toList()
        }

    fun isCardFound(item: RecyclerViewAdapter.ImageData): Boolean {
        return item in _cardsMatched
    }

    private val _cardsSelected: MutableList<RecyclerViewAdapter.ImageData> = mutableListOf()
    val cardsSelected: List<RecyclerViewAdapter.ImageData>
        get() {
            return _cardsSelected.toList()
        }


    fun isCardSelected(item: RecyclerViewAdapter.ImageData): Boolean {
        return item in _cardsSelected
    }

    fun modifyList(item: RecyclerViewAdapter.ImageData) {
        Log.d("test", "working ${_cardsSelected.size}")
        if (_cardsSelected.size == 1) {
            _cardsSelected.add(item)

            if (_cardsSelected[0].product_id == _cardsSelected[1].product_id) {
                _cardsMatched.addAll(_cardsSelected)
                matchFunction()
            }
            _cardsSelected.clear()
        } else {
            _cardsSelected.add(item)
        }
    }
}