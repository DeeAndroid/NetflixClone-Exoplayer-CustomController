package com.dee.popularmovies.data.res

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class MovieListResponse(

    @field:SerializedName("page") val page: Int? = null,

    @field:SerializedName("total_pages") val totalPages: Int? = null,

    @field:SerializedName("results") val results: List<ResultsItem?>? = null,

    @field:SerializedName("total_results") val totalResults: Int? = null
)

@Parcelize
data class ResultsItem(

    @field:SerializedName("first_air_date") val firstAirDate: String? = null,

    @field:SerializedName("overview") val overview: String? = null,

    @field:SerializedName("original_language") val originalLanguage: String? = null,

    @field:SerializedName("genre_ids") val genreIds: List<Int?>? = null,

    @field:SerializedName("poster_path") val posterPath: String? = null,

    @field:SerializedName("origin_country") val originCountry: List<String?>? = null,

    @field:SerializedName("backdrop_path") val backdropPath: String? = null,

    @field:SerializedName("original_name") val originalName: String? = null,

    @field:SerializedName("popularity") val popularity: String? = null,

    @field:SerializedName("vote_average") val voteAverage: String? = null,

    @field:SerializedName("name") val name: String? = null,

    @field:SerializedName("id") val id: Int? = null,

    @field:SerializedName("vote_count") val voteCount: Int? = null
) : Parcelable
