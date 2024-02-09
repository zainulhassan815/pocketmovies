package org.dreamerslab.pocketmovies.data.models

import com.squareup.moshi.Json

data class SingleMovieApiResponse(
    @field:Json(name = "status")
    val status: String,

    @field:Json(name = "status_message")
    val statusMessage: String,

    @field:Json(name = "data")
    val data: SingleMovieResponseData?,
)

data class MoviesListApiResponse(
    @field:Json(name = "status")
    val status: String,

    @field:Json(name = "status_message")
    val statusMessage: String,

    @field:Json(name = "data")
    val data: MoviesListResponseData?
)

data class SingleMovieResponseData(
    @field:Json(name = "movie")
    val movie: MovieDto
)

data class MoviesListResponseData(
    @field:Json(name = "movie_count")
    val movieCount: Int,

    @field:Json(name = "limit")
    val limit: Int,

    @field:Json(name = "page_number")
    val pageNumber: Int,

    @field:Json(name = "movies")
    val movies: List<MovieDto>
)
