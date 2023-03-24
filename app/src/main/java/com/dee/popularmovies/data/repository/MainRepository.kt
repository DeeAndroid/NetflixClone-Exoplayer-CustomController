package com.dee.popularmovies.data.repository

import com.dee.popularmovies.data.service.ServiceApi
import com.dee.popularmovies.di.utility.ResponseReceiver
import javax.inject.Inject

class MainRepository @Inject constructor(private val api: ServiceApi) : ResponseReceiver {

    /**for getting popular movies*/
    suspend fun getMovieListRepo(
        api_key: String,
        page: Int
    ) = callApi {
        api.getMovieList(api_key, page)
    }

    /**for getting Top Rated movies*/
    suspend fun getTopRatedMovieListRepo(
        api_key: String,
        page: Int
    ) = callApi {
        api.getTopRatedMovieList(api_key, page)
    }

    /**for getting Now Playing movies*/
    suspend fun nowPlayingMovieListRepo(
        api_key: String,
        page: Int
    ) = callApi {
        api.nowPlayingMovieList(api_key, page)
    }

}