package org.dreamerslab.pocketmovies.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import arrow.core.getOrHandle
import arrow.retrofit.adapter.either.networkhandling.HttpError
import arrow.retrofit.adapter.either.networkhandling.IOError
import arrow.retrofit.adapter.either.networkhandling.UnexpectedCallError
import org.dreamerslab.pocketmovies.domain.models.Movie

class YtsMoviesPagingSource(
    private val ytsMoviesRepository: YtsMoviesRepository,
    private val queryParams: QueryParams = QueryParams.default,
) : PagingSource<Int, Movie>() {

    companion object {
        const val InitialPageNumber = 1
        const val DefaultPageSize = 20
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val position = params.key ?: InitialPageNumber
        return ytsMoviesRepository.getMovies(
            paginationParams = PaginationParams(
                page = position,
                perPage = params.loadSize
            ),
            queryParams = queryParams,
        ).map { movies ->
            LoadResult.Page(
                data = movies,
                prevKey = if (position == 1) null else position.dec(),
                nextKey = if (movies.isEmpty()) null else position + (params.loadSize / DefaultPageSize)
            )
        }.getOrHandle {
            val error = when (it) {
                is HttpError -> Throwable(it.message)
                is IOError -> Throwable(it.cause)
                is UnexpectedCallError -> Throwable(it.cause)
            }
            LoadResult.Error(error)
        }
    }
}