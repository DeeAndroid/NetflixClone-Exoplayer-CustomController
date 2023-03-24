package com.dee.popularmovies.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dee.popularmovies.data.repository.MainRepository
import com.dee.popularmovies.data.res.MovieListResponse
import com.dee.popularmovies.data.res.ResultsItem
import com.dee.popularmovies.di.utility.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {


    /**for getting popular movies*/
    private val _movieList: MutableStateFlow<Resource<MovieListResponse>> = MutableStateFlow(
        Resource.Empty
    )
    val movieList: Flow<Resource<MovieListResponse>> get() = _movieList
    fun getMovieList(
        api_key: String,
        page: Int
    ) {
        viewModelScope.launch {
            _movieList.value = Resource.Loading
            _movieList.value = repository.getMovieListRepo(api_key, page)
        }
    }


    /**for getting Top Rated movies*/
    private val _topRatedMovieList: MutableStateFlow<Resource<MovieListResponse>> =
        MutableStateFlow(
            Resource.Empty
        )
    val topRatedMovieList: Flow<Resource<MovieListResponse>> get() = _topRatedMovieList
    fun topRatedMovieList(
        api_key: String,
        page: Int
    ) {
        viewModelScope.launch {
            _topRatedMovieList.value = Resource.Loading
            _topRatedMovieList.value = repository.getTopRatedMovieListRepo(api_key, page)
        }
    }


    /**for getting Now Playing movies*/
    private val _nowPlayingMovieList: MutableStateFlow<Resource<MovieListResponse>> =
        MutableStateFlow(
            Resource.Empty
        )
    val nowPlayingMovieList: Flow<Resource<MovieListResponse>> get() = _nowPlayingMovieList
    fun nowPlayingMovieList(
        api_key: String,
        page: Int
    ) {
        viewModelScope.launch {
            _nowPlayingMovieList.value = Resource.Loading
            _nowPlayingMovieList.value = repository.nowPlayingMovieListRepo(api_key, page)
        }
    }

}