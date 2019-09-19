package com.example.shopify_memory_game.data.network

import com.example.shopify_memory_game.data.network.request.CardsListContainer
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface ApiServiceProvider {
//https://shopicruit.myshopify.com/admin/products.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6


    @GET("products.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6")
    suspend fun getCards(): CardsListContainer

    companion object {
        operator fun invoke(
        ): ApiServiceProvider {
            val requestInterceptor = Interceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .build()
                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://shopicruit.myshopify.com/admin/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
                .create(ApiServiceProvider::class.java)
        }
    }
}