package com.dee.popularmovies.ui.movies.listneres

import com.dee.popularmovies.data.res.ResultsItem

interface OnItemClickListener {
    fun onItemClick(movielist: ResultsItem?, position: Int)
}