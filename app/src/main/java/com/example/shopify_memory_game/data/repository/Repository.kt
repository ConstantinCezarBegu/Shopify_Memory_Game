package com.example.shopify_memory_game.data.repository

import androidx.lifecycle.LiveData
import com.example.shopify_memory_game.data.db.dao.CardDao
import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.data.preference.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// stores in db the data fetched
class Repository(
    private val cardDao: CardDao,
    private val dataSource: com.example.shopify_memory_game.data.network.DataSource,
    private val userData: UserData
) {

    init {
        dataSource.downloadedCards.observeForever { cardsResponse ->
            persistFetchedImages(cardsResponse)
        }
    }

    val errorLiveData = dataSource.error

    suspend fun getImages(): LiveData<List<Card>> {
        fetchImages()
        return withContext(Dispatchers.IO) {
            return@withContext cardDao.getCards()
        }
    }

    private suspend fun fetchImages() {
        dataSource.fetchImages(true)
    }


    private fun persistFetchedImages(fetchedCategories: List<Card>) {
        GlobalScope.launch(Dispatchers.IO) {
            cardDao.insertAll(fetchedCategories)
        }
    }

}