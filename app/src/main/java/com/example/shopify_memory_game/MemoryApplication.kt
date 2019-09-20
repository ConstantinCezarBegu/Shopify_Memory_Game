package com.example.shopify_memory_game

import android.app.Application
import com.example.shopify_memory_game.data.repository.Repository
import com.example.shopify_memory_game.data.db.MemoryDatabase
import com.example.shopify_memory_game.data.network.ApiServiceProvider
import com.example.shopify_memory_game.data.network.DataSource
import com.example.shopify_memory_game.data.preference.UserData
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class MemoryApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MemoryApplication))
        bind() from singleton { MemoryDatabase(instance()) }
        bind() from singleton { instance<MemoryDatabase>().imageDao() }
        bind() from singleton { ApiServiceProvider() }
        bind() from singleton { DataSource(instance()) }
        bind() from singleton { UserData(instance()) }
        bind() from singleton { Repository(instance(), instance(), instance()) }
    }
}