package com.example.shopify_memory_game.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shopify_memory_game.data.network.request.Image
import com.example.shopify_memory_game.internal.getImages
import retrofit2.HttpException
import java.io.IOException

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

    private val _downloadedImages = MutableLiveData<List<Image>>()
    val downloadedImages: LiveData<List<Image>>
        get() = _downloadedImages


    suspend fun fetchImages(clear: Boolean) {
        errorHandling(clear) {
            val fetchedImages = apiServiceProvider.getCards().cards.getImages()

            if (fetchedImages.isNullOrEmpty() && clear) {
                error.postValue(HttpErrors.SUCCESS)
            }

            _downloadedImages.postValue(
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
            Log.d("test", "HTTPEX")
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
            Log.d("test", "NULLPOINTER")
            error.postValue(HttpErrors.SUCCESS)
            true
        }
    }
}