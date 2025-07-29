package com.example.duroodcounter.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class DataStoreManager(private val context: Context) {

    companion object {
        val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")
    }

    suspend fun isFirstLaunch(): Boolean {
        val prefs = context.dataStore.data.first()
        return prefs[FIRST_LAUNCH_KEY] ?: true
    }

    suspend fun setFirstLaunchDone() {
        context.dataStore.edit { prefs ->
            prefs[FIRST_LAUNCH_KEY] = false
        }
    }
}
