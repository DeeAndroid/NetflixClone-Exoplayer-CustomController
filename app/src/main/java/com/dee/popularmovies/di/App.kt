package com.dee.popularmovies.di

import android.app.Application
import android.content.Context
import com.google.android.exoplayer2.database.ExoDatabaseProvider
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var appContext: Context
        lateinit var cache: SimpleCache
    }

    private val cacheSize: Long = 90 * 1024 * 1024
    private lateinit var cacheEvictor: LeastRecentlyUsedCacheEvictor
    private lateinit var exoplayerDatabaseProvider: ExoDatabaseProvider


    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext

        cacheEvictor = LeastRecentlyUsedCacheEvictor(cacheSize)
        exoplayerDatabaseProvider = ExoDatabaseProvider(this)
        cache = SimpleCache(cacheDir, cacheEvictor, exoplayerDatabaseProvider)
    }
}