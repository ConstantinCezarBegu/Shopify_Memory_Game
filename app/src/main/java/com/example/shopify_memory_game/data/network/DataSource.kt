package com.example.shopify_memory_game.data.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopify_memory_game.data.network.request.Card
import retrofit2.HttpException
import java.io.IOException

// Wrapper for the api class so that it can handle errors
class DataSource(
    private val apiServiceProvider: ApiServiceProvider
) {
    val error = MutableLiveData(HttpErrors.SUCCESS)

    enum class HttpErrors {
        SUCCESS,
        INTERNET_CONNECTION,
        AUTHENTICATION,
        HTTP_ERROR
    }

    private val _downloadedCards = MutableLiveData<List<Card>>()
    val downloadedCards: LiveData<List<Card>>
        get() = _downloadedCards


    suspend fun fetchImages(clear: Boolean) {
        errorHandling(clear) {
            val fetchedImages = apiServiceProvider.getCards().cards

            if (fetchedImages.isNullOrEmpty() && clear) {
                error.postValue(HttpErrors.SUCCESS)
            }

            _downloadedCards.postValue(
                fetchedImages
            )
        }
    }

    private suspend fun errorHandling(notifyError: Boolean, function: suspend () -> Unit): Boolean {
        return try {
            function()
            true
        } catch (e: IOException) {
            if (notifyError) error.postValue(HttpErrors.INTERNET_CONNECTION)
            false
        } catch (e: HttpException) {
            if (notifyError) when (e.code()) {
                401 -> {
                    error.postValue(HttpErrors.AUTHENTICATION)
                }
                else -> {
                    error.postValue(HttpErrors.HTTP_ERROR)
                }
            }
            false
        } catch (e: NullPointerException) {
            error.postValue(HttpErrors.SUCCESS)
            true
        }
    }
}