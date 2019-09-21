package com.example.shopify_memory_game.adapters

class RecyclerViewSelectionImageTracker(
    private val matchSize: Int,
    private val matchFunction: () -> Unit,
    private val noMatchFunction: () -> Unit
) {

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

    fun clearSelected() {
        _cardsSelected.clear()
    }

    fun modifyList(item: RecyclerViewAdapter.ImageData) {
        _cardsSelected.add(item)
        if (_cardsSelected.size == matchSize) {
            if (areCardsTheSame()) {
                _cardsMatched.addAll(_cardsSelected)
                matchFunction()
                _cardsSelected.clear()
            } else {
                noMatchFunction()
            }
        } else {
            if (!areCardsTheSame()) noMatchFunction()
        }
    }

    fun areCardsTheSame(): Boolean {
        val cardToMatch = _cardsSelected[0].product_id
        for (i in 0 until _cardsSelected.size) {
            if (_cardsSelected[i].product_id != cardToMatch) return false
        }
        return true
    }
}