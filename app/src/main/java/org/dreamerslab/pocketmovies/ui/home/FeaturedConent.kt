package org.dreamerslab.pocketmovies.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import org.dreamerslab.pocketmovies.R
import org.dreamerslab.pocketmovies.domain.models.Movie
import org.dreamerslab.pocketmovies.ui.components.FeaturedContentCard

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun FeaturedContentCarousel(
    movies: List<Movie>,
    onMovieClick: (movie: Movie) -> Unit,
) {
    val state = rememberPagerState { movies.size }
    AutoScrollPager(state)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.home_screen_featured_content_section_title),
            style = MaterialTheme.typography.titleMedium,
        )

        HorizontalPager(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            pageSpacing = 16.dp,
            key = { movies[it].id }
        ) {
            val movie = movies[it]

            FeaturedContentCard(
                title = movie.title,
                subtitle = movie.summary.ifBlank { movie.year.toString() },
                imageUrl = movie.thumbnail,
                onClick = { onMovieClick(movie) }
            )
        }

        PagerIndicator(state)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AutoScrollPager(
    state: PagerState,
    delay: Long = 3_000L
) {
    val isDraggedState = state.interactionSource.collectIsDraggedAsState()
    LaunchedEffect(isDraggedState) {
        snapshotFlow { isDraggedState.value }
            .collectLatest { isDragged ->
                if (!isDragged) {
                    while (true) {
                        delay(delay)
                        val nextPage = when {
                            state.currentPage < state.pageCount - 1 -> state.currentPage + 1
                            else -> 0
                        }
                        state.animateScrollToPage(page = nextPage)
                    }
                }
            }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerIndicator(
    state: PagerState
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
    ) {
        repeat(state.pageCount) { index ->
            val isActive = state.currentPage == index

            val color = when {
                isActive -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.secondary
            }

            val width by animateDpAsState(
                targetValue = if (isActive) 32.dp else 16.dp,
                label = "Page Indicator Width"
            )

            Box(
                modifier = Modifier
                    .width(width)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}