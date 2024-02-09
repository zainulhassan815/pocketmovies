package org.dreamerslab.pocketmovies.data.repository

import arrow.core.Either
import arrow.retrofit.adapter.either.networkhandling.CallError
import kotlinx.coroutines.withContext
import org.dreamerslab.pocketmovies.data.api.OrderBy
import org.dreamerslab.pocketmovies.data.api.SortBy
import org.dreamerslab.pocketmovies.data.api.YtsApi
import org.dreamerslab.pocketmovies.data.models.MovieDto
import org.dreamerslab.pocketmovies.domain.models.Movie
import org.dreamerslab.pocketmovies.utils.AppCoroutineDispatchers
import javax.inject.Inject

data class PaginationParams(
    val page: Int = 1,
    val perPage: Int = 20,
) {
    companion object {
        val default = PaginationParams()
    }
}

data class QueryParams(
    val query: String? = null,
    val genre: String? = null,
    val quality: String? = null,
    val sortBy: SortBy = SortBy.DateAdded,
    val orderBy: OrderBy = OrderBy.Descending,
) {
    companion object {
        val default = QueryParams()
    }
}

class YtsMoviesRepository @Inject constructor(
    private val ytsApi: YtsApi,
    private val dispatchers: AppCoroutineDispatchers,
) {
    suspend fun getMovies(
        paginationParams: PaginationParams = PaginationParams.default,
        queryParams: QueryParams = QueryParams.default
    ): Either<CallError, List<Movie>> {
        return withContext(dispatchers.io) {
            ytsApi.listMovies(
                limit = paginationParams.perPage,
                page = paginationParams.page,
                queryTerm = queryParams.query,
                quality = queryParams.quality,
                genre = queryParams.genre,
                sortBy = queryParams.sortBy,
                orderBy = queryParams.orderBy
            ).map {
                it.data?.movies?.map(MovieDto::mapToDomainMovie) ?: emptyList()
            }
        }
    }

    suspend fun getMovieDetails(movieId: Int): Either<CallError, Movie?> {
        return withContext(dispatchers.io) {
            ytsApi.getMovieDetails(
                movieId = movieId,
                withCast = true
            ).map {
                it.data?.movie?.mapToDomainMovie()
            }
        }
    }

    suspend fun getMovieSuggestions(movieId: Int): Either<CallError, List<Movie>> {
        return withContext(dispatchers.io) {
            ytsApi.getSuggestions(
                movieId = movieId
            ).map {
                it.data?.movies?.map(MovieDto::mapToDomainMovie) ?: emptyList()
            }
        }
    }
}