package com.example.shopify_memory_game.data.db.dao

import androidx.room.*
import com.example.shopify_memory_game.data.network.request.Card
import com.example.shopify_memory_game.internal.productIds

@Dao
interface CardDao {

    @Query("SELECT * FROM memory_card")
    fun getCards(): List<Card>

    @Transaction
    suspend fun insertAll(imageList: List<Card>) {
        clearOld(imageList.productIds)
        updateCurrent(imageList)
        insertNew(imageList)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertNew(imageList: List<Card>)

    @Update
    fun updateCurrent(imageList: List<Card>)

    @Query("DELETE FROM memory_card WHERE memory_card.id NOT IN (:id)")
    fun clearOld(id: List<Long>)
}