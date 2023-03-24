package com.dee.popularmovies.data.service

import com.dee.popularmovies.data.res.MovieListResponse
import retrofit2.http.*

interface ServiceApi {

    /**for getting popular movies*/
    @GET(EndPoint.POPULAR)
    suspend fun getMovieList(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieListResponse

    /**for getting Top Rated movies*/
    @GET(EndPoint.TOP_RATED)
    suspend fun getTopRatedMovieList(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieListResponse

    /**for getting Now Playing movies*/
    @GET(EndPoint.NOW_PLAYING)
    suspend fun nowPlayingMovieList(
        @Query("api_key") api_key: String,
        @Query("page") page: Int
    ): MovieListResponse

}