package org.dreamerslab.pocketmovies.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import org.dreamerslab.pocketmovies.R
import org.dreamerslab.pocketmovies.domain.models.Movie
import org.dreamerslab.pocketmovies.ui.components.ErrorMessage
import org.dreamerslab.pocketmovies.ui.components.LoadMoreContentButton
import org.dreamerslab.pocketmovies.ui.components.LoadingContent
import org.dreamerslab.pocketmovies.ui.components.MovieCard
import org.dreamerslab.pocketmovies.ui.utils.materialPlaceholder
import org.dreamerslab.pocketmovies.ui.utils.plus
import org.dreamerslab.pocketmovies.ui.utils.spannedItem

@Composable
fun SearchScreen(
    onNavigateUp: () -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val query by viewModel.searchQuery.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val movies = state.movies.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onChange = viewModel::updateQuery,
                onNavigateUp = onNavigateUp,
                onClear = { viewModel.updateQuery(query.copy(text = "")) },
            )
        }
    ) { paddingValues ->
        SearchResult(
            movies = movies,
            onRetryClick = { movies.retry() },
            onMovieClick = onMovieClick,
            contentPadding = paddingValues.plus(
                plus = PaddingValues(16.dp),
                layoutDirection = LocalLayoutDirection.current
            ),
        )
    }
}


@Composable
fun SearchResult(
    movies: LazyPagingItems<Movie>,
    onRetryClick: () -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues(),
) {
    val controller = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                controller?.hide()
                focusManager.clearFocus()
                return Offset.Zero
            }
        }
    }

    LazyVerticalGrid(
        modifier = modifier.nestedScroll(nestedScrollConnection),
        state = state,
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (movies.loadState.refresh) {
            is LoadState.Loading -> items(6) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .materialPlaceholder()
                )
            }

            is LoadState.Error -> spannedItem {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 320.dp)
                        .aspectRatio(4f / 5f),
                    contentAlignment = Alignment.Center,
                ) {
                    ErrorMessage(
                        title = stringResource(R.string.unknown_error_title),
                        message = stringResource(R.string.unknown_error_message),
                        onActionClick = onRetryClick
                    )
                }
            }

            is LoadState.NotLoading -> items(
                count = movies.itemCount,
                key = { movies[it]!!.id }
            ) {
                val movie = movies[it]!!

                MovieCard(
                    title = movie.title,
                    subtitle = movie.year.toString(),
                    imageUrl = movie.thumbnail,
                    onClick = { onMovieClick(movie) }
                )
            }
        }

        when (movies.loadState.append) {
            is LoadState.Error -> spannedItem {
                LoadMoreContentButton(onClick = onRetryClick)
            }

            LoadState.Loading -> spannedItem { LoadingContent() }

            is LoadState.NotLoading -> Unit
        }
    }
}
