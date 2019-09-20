package com.constantin.constaflux.data.repository

import androidx.lifecycle.LiveData
import com.example.shopify_memory_game.data.db.dao.ImageDao
import com.example.shopify_memory_game.data.network.request.Image
import com.example.shopify_memory_game.data.preference.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(
    private val imageDao: ImageDao,
    private val dataSource: com.example.shopify_memory_game.data.network.DataSource,
    private val userData: UserData
) {

    init {
        dataSource.downloadedImages.observeForever { cardsImageResponse ->
            persistFetchedImages(cardsImageResponse)
        }
    }

    val errorLiveData = dataSource.error

    suspend fun getImages(): LiveData<List<Image>> {
        fetchImages()
        return withContext(Dispatchers.IO) {
            return@withContext imageDao.getImages()
        }
    }

    private suspend fun fetchImages() {
        dataSource.fetchImages(true)
    }


    private fun persistFetchedImages(fetchedCategories: List<Image>) {
        GlobalScope.launch(Dispatchers.IO) {
            imageDao.insertAll(fetchedCategories)
        }
    }

}