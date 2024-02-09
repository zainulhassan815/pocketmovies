package org.dreamerslab.pocketmovies.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onSearchClick: () -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    val movies = viewModel.movies.collectAsLazyPagingItems()
    val featuredContentUiState by viewModel.featuredContentUiState.collectAsStateWithLifecycle()

    val showErrorMessage = movies.loadState.refresh is LoadState.Error
            && featuredContentUiState is FeaturedContentUiState.Error

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.home_screen_appbar_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            // TODO: Handle menu action
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    IconButton(onSearchClick) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            showErrorMessage -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                ErrorMessage(
                    title = stringResource(R.string.unknown_error_title),
                    message = stringResource(R.string.unknown_error_message),
                    onActionClick = {
                        movies.retry()
                        viewModel.loadFeaturedContent()
                    }
                )
            }

            else -> {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .consumeWindowInsets(paddingValues),
                    columns = GridCells.Adaptive(minSize = 160.dp),
                    contentPadding = paddingValues.plus(
                        plus = PaddingValues(16.dp),
                        layoutDirection = LocalLayoutDirection.current
                    ),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    featuredContentSection(
                        state = featuredContentUiState,
                        onRetryClick = viewModel::loadFeaturedContent,
                        onMovieClick = onMovieClick,
                    )

                    moviesGridSection(
                        movies = movies,
                        onRetryClick = { movies.retry() },
                        onMovieClick = onMovieClick
                    )
                }
            }
        }
    }
}

private fun LazyGridScope.featuredContentSection(
    state: FeaturedContentUiState,
    onRetryClick: () -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
) {
    spannedItem("featured_content") {
        when (state) {
            FeaturedContentUiState.Initial,
            FeaturedContentUiState.Loading -> Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .materialPlaceholder()
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 5f)
                        .materialPlaceholder()
                )
            }

            FeaturedContentUiState.Error -> Box(
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

            is FeaturedContentUiState.Success -> FeaturedContentCarousel(
                movies = state.movies,
                onMovieClick = onMovieClick
            )
        }
    }
}

private fun LazyGridScope.moviesGridSection(
    movies: LazyPagingItems<Movie>,
    onRetryClick: () -> Unit,
    onMovieClick: (movie: Movie) -> Unit,
) {
    when (movies.loadState.refresh) {
        is LoadState.Loading -> {
            spannedItem {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp)
                        .materialPlaceholder()
                )
            }

            items(2) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .materialPlaceholder()
                )
            }
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

        is LoadState.NotLoading -> {
            spannedItem {
                Text(
                    text = stringResource(R.string.home_screen_movies_section_title),
                    modifier = Modifier.padding(top = 16.dp)
                )
            }

            items(
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
    }

    when (movies.loadState.append) {
        is LoadState.Error -> spannedItem {
            LoadMoreContentButton(onClick = onRetryClick)
        }

        LoadState.Loading -> spannedItem { LoadingContent() }

        is LoadState.NotLoading -> Unit
    }
}
