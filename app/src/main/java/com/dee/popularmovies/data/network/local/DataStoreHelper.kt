package com.dee.popularmovies.data.network.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dee.popularmovies.data.network.local.Constants.MVE_PREF_NAME
import kotlinx.coroutines.flow.map


//Instance of DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = MVE_PREF_NAME)


/**
 * Add Boolean to the data store
 */
suspend fun Context.writeBool(key: String, value: Boolean) {
    dataStore.edit { pref -> pref[booleanPreferencesKey(key)] = value }
}

/**
 * Reading the Boolean from the data store
 */
fun Context.readBool(key: String): LiveData<Boolean?> {
    return dataStore.data.map { pref ->
        pref[booleanPreferencesKey(key)]
    }.asLiveData()
}

