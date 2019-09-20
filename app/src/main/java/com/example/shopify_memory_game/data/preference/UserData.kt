package com.example.shopify_memory_game.data.preference

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

class UserData(context: Context) {
    companion object {
        const val USER_SCORE = "userScore"
        const val DEFAULT_USER_SCORE = 0

        const val GRID_SIZE = "gridSize"
        const val DEFAULT_GRID_SIZE = 3

        const val MATCH_SIZE = "matchSize"
        const val DEFAULT_MATCH_SIZE = 2
    }

    private val encryptedSharedPreferences = EncryptedSharedPreferences.create(
        "${context.packageName}_encrypted_preferences",
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    var userScore: Int
        set(value) {
            val sharedPrefsEditor = encryptedSharedPreferences.edit()
            sharedPrefsEditor.putInt(USER_SCORE, value)
            sharedPrefsEditor.apply()
        }
        get() {
            return encryptedSharedPreferences.getInt(
                USER_SCORE,
                DEFAULT_USER_SCORE
            )
        }

    var gridSize: Int
        set(value) {
            val sharedPrefsEditor = encryptedSharedPreferences.edit()
            sharedPrefsEditor.putInt(GRID_SIZE, value)
            sharedPrefsEditor.apply()
        }
        get() {
            return encryptedSharedPreferences.getInt(
                GRID_SIZE,
                DEFAULT_GRID_SIZE
            )
        }

    var matchSize: Int
        set(value) {
            val sharedPrefsEditor = encryptedSharedPreferences.edit()
            sharedPrefsEditor.putInt(MATCH_SIZE, value)
            sharedPrefsEditor.apply()
        }
        get() {
            return encryptedSharedPreferences.getInt(
                MATCH_SIZE,
                DEFAULT_MATCH_SIZE
            )
        }

}