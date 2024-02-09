package org.dreamerslab.pocketmovies.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dreamerslab.pocketmovies.data.api.OrderBy
import org.dreamerslab.pocketmovies.data.api.SortBy
import org.dreamerslab.pocketmovies.data.repository.PaginationParams
import org.dreamerslab.pocketmovies.data.repository.QueryParams
import org.dreamerslab.pocketmovies.data.repository.YtsMoviesPagingSource
import org.dreamerslab.pocketmovies.data.repository.YtsMoviesRepository
import org.dreamerslab.pocketmovies.domain.models.Movie
import javax.inject.Inject

sealed class FeaturedContentUiState {
    data object Initial : FeaturedContentUiState()
    data object Loading : FeaturedContentUiState()
    data object Error : FeaturedContentUiState()
    data class Success(
        val movies: List<Movie> = emptyList()
    ) : FeaturedContentUiState()
}

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val ytsMoviesRepository: YtsMoviesRepository
) : ViewModel() {

    val movies = Pager(
        config = PagingConfig(
            pageSize = YtsMoviesPagingSource.DefaultPageSize,
            initialLoadSize = YtsMoviesPagingSource.DefaultPageSize * 2,
        ),
        pagingSourceFactory = {
            YtsMoviesPagingSource(
                ytsMoviesRepository = ytsMoviesRepository,
                queryParams = QueryParams(
                    sortBy = SortBy.Rating,
                    orderBy = OrderBy.Descending
                )
            )
        }
    ).flow.cachedIn(viewModelScope)

    private val _featuredContentUiState = MutableStateFlow<FeaturedContentUiState>(
        value = FeaturedContentUiState.Initial
    )
    val featuredContentUiState: StateFlow<FeaturedContentUiState> =
        _featuredContentUiState.asStateFlow()

    init {
        loadFeaturedContent()
    }

    fun loadFeaturedContent() {
        viewModelScope.launch {
            _featuredContentUiState.update { FeaturedContentUiState.Loading }
            ytsMoviesRepository.getMovies(
                paginationParams = PaginationParams(perPage = 10)
            ).fold(
                ifLeft = {
                    _featuredContentUiState.update { FeaturedContentUiState.Error }
                },
                ifRight = { data ->
                    _featuredContentUiState.update { FeaturedContentUiState.Success(data) }
                }
            )
        }
    }
}