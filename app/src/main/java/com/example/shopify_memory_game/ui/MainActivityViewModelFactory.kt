package com.example.shopify_memory_game.ui

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.shopify_memory_game.data.preference.UserData
import com.example.shopify_memory_game.data.repository.Repository
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivityViewModelFactory(
    private val context: Context,
    private val noMatchFunction: ()-> Unit,

    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory( owner, defaultArgs), KodeinAware
{

    override val kodein by closestKodein(context)
    private val repository: Repository by instance()
    private val userData: UserData by instance()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return MainActivityViewModel(repository, userData, noMatchFunction ,handle) as T
    }
}