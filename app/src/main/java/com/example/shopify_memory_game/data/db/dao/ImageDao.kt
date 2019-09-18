package com.example.shopify_memory_game.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shopify_memory_game.data.network.Image
import com.example.shopify_memory_game.internal.productIds
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface ImageDao {
    
    @Query("SELECT * FROM memory_image")
    fun getImages(): Flow<List<Image>>

    @Transaction
    suspend fun insertAllForWorker(imageList: List<Image>){
        clearOld(imageList.productIds)
        updateCurrent(imageList)
        insertNew(imageList)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNew(imageList: List<Image>)

    @Update
    fun updateCurrent(imageList: List<Image>)

    @Query("DELETE FROM memory_image WHERE memory_image.product_id NOT IN (:id)")
    fun clearOld(id: List<Long>)
}