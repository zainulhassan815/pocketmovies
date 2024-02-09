package org.dreamerslab.pocketmovies.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.dreamerslab.pocketmovies.data.api.OrderBy
import org.dreamerslab.pocketmovies.data.api.SortBy
import org.dreamerslab.pocketmovies.data.repository.QueryParams
import org.dreamerslab.pocketmovies.data.repository.YtsMoviesPagingSource
import org.dreamerslab.pocketmovies.data.repository.YtsMoviesRepository
import org.dreamerslab.pocketmovies.domain.models.Movie
import javax.inject.Inject

data class SearchScreenUiState(
    val movies: Flow<PagingData<Movie>> = emptyFlow()
)

data class SearchQuery(
    val text: String = "",
    val sortBy: SortBy = SortBy.Title,
    val orderBy: OrderBy = OrderBy.Ascending,
)

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val ytsMoviesRepository: YtsMoviesRepository,
) : ViewModel() {

    private val _searchQuery: MutableStateFlow<SearchQuery> = MutableStateFlow(SearchQuery())
    val searchQuery: StateFlow<SearchQuery> = _searchQuery.asStateFlow()

    val state = _searchQuery
        .debounce(300)
        .mapLatest { query ->
            query.copy(
                text = query.text.lowercase().trim()
            )
        }
        .distinctUntilChanged()
        .mapLatest { query ->
            SearchScreenUiState(
                movies = Pager(
                    config = PagingConfig(
                        pageSize = YtsMoviesPagingSource.DefaultPageSize,
                    ),
                    pagingSourceFactory = {
                        YtsMoviesPagingSource(
                            ytsMoviesRepository = ytsMoviesRepository,
                            queryParams = QueryParams(
                                query = query.text,
                                sortBy = query.sortBy,
                                orderBy = query.orderBy,
                            )
                        )
                    }
                ).flow.cachedIn(viewModelScope)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SearchScreenUiState()
        )

    fun updateQuery(query: SearchQuery) {
        _searchQuery.update { query }
    }
}
