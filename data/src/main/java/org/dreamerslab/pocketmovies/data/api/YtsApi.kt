package org.dreamerslab.pocketmovies.data.api

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import org.dreamerslab.pocketmovies.data.models.MoviesListApiResponse
import org.dreamerslab.pocketmovies.data.models.SingleMovieApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An interface to interact with YTS api.
 * For details, see: https://yts.mx/api
 */
interface YtsApi {

    @GET("/api/v2/list_movies.json")
    suspend fun listMovies(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("quality") quality: String? = null,
        @Query("minimum_rating") minimumRating: Int? = null,
        @Query("query_term") queryTerm: String? = null,
        @Query("genre") genre: String? = null,
        @Query("sort_by") sortBy: SortBy? = SortBy.DateAdded,
        @Query("order_by") orderBy: OrderBy? = OrderBy.Descending,
        @Query("with_rt_ratings") withRottenTomatoesRatings: Boolean? = false,
    ): Either<CallError, MoviesListApiResponse>

    @GET("/api/v2/movie_details.json")
    suspend fun getMovieDetails(
        @Query("movie_id") movieId: Int,
        @Query("with_cast") withCast: Boolean? = false,
        @Query("with_images") withImages: Boolean? = false
    ): Either<CallError, SingleMovieApiResponse>

    @GET("/api/v2/movie_suggestions.json")
    suspend fun getSuggestions(
        @Query("movie_id") movieId: Int,
    ): Either<CallError, MoviesListApiResponse>

}