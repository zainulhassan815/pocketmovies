package org.dreamerslab.pocketmovies.ui.moviedetails

import android.icu.number.Notation
import android.icu.number.NumberFormatter
import android.icu.number.Precision
import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.WatchLater
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import org.dreamerslab.pocketmovies.R
import org.dreamerslab.pocketmovies.domain.models.Cast
import org.dreamerslab.pocketmovies.domain.models.Movie
import org.dreamerslab.pocketmovies.ui.components.ErrorMessage
import org.dreamerslab.pocketmovies.ui.components.ExpandingText
import org.dreamerslab.pocketmovies.ui.components.LoadingContent
import org.dreamerslab.pocketmovies.ui.utils.copy
import org.dreamerslab.pocketmovies.utils.launchUrl
import java.util.Locale
import kotlin.time.Duration

@Composable
fun MovieDetailsScreen(
    onNavigateUp: () -> Unit,
    viewModel: MovieDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (val state = uiState) {
        MovieDetailsUiState.Initial,
        MovieDetailsUiState.Loading -> MovieDetailsLoading()

        MovieDetailsUiState.Error -> ErrorContent(
            onRetryClick = { viewModel.loadMovieDetails() }
        )

        MovieDetailsUiState.InvalidMovieId -> InvalidMovieIdContent(
            onNavigateUp = onNavigateUp
        )

        is MovieDetailsUiState.Success -> {
            val context = LocalContext.current

            MovieDetailsContent(
                movie = state.movie,
                onWatchTrailerClick = {
                    context.launchUrl(
                        url = "https://youtube.com/watch?v=" + state.movie.youtubeVideoCode
                    )
                },
                onNavigateUp = onNavigateUp,
            )
        }
    }
}

@Composable
private fun MovieDetailsLoading() {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            LoadingContent(
                message = stringResource(R.string.movie_details_screen_loading_movie_details_indicator_message)
            )
        }
    }
}

@Composable
private fun ErrorContent(
    onRetryClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            ErrorMessage(
                title = stringResource(R.string.unknown_error_title),
                message = stringResource(R.string.unknown_error_message),
                onActionClick = onRetryClick
            )
        }
    }
}

@Composable
private fun InvalidMovieIdContent(
    onNavigateUp: () -> Unit
) {
    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            ErrorMessage(
                title = stringResource(R.string.movie_details_screen_invalid_id_error_title),
                message = stringResource(R.string.movie_details_screen_invalid_id_error_message),
                actionTitle = stringResource(R.string.movie_details_screen_invalid_id_error_action_title),
                onActionClick = onNavigateUp
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun MovieDetailsContent(
    movie: Movie,
    onWatchTrailerClick: () -> Unit,
    onNavigateUp: () -> Unit,
) {
    Scaffold(
        contentWindowInsets = ScaffoldDefaults.contentWindowInsets.exclude(WindowInsets.statusBars),
    ) { paddingValues ->
        LazyColumn(
            contentPadding = paddingValues.copy(copyTop = false)
        ) {
            item("background_image") {
                BackgroundImage(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f),
                    imageUrl = movie.thumbnail,
                    contentDescription = movie.title,
                )
            }

            stickyHeader("top_app_bar") {
                TopAppBar(
                    title = {
                        Text(
                            text = movie.title,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    },
                    navigationIcon = {
                        IconButton(onNavigateUp) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                            )
                        }
                    }
                )
            }

            item("poster_info_section") {
                PosterInfoSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                    movie = movie
                )
            }

            if (movie.summary.isNotBlank())
                item("about_section") {
                    AboutSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        summary = movie.summary
                    )
                }

            if (movie.youtubeVideoCode != null) item("youtube_trailer_section") {
                YoutubeTrailerSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    onWatchTrailerClick = onWatchTrailerClick
                )
            }

            if (movie.genres.isNotEmpty()) item("genres_section") {
                GenresSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 16.dp),
                    genres = movie.genres
                )
            }

            if (movie.cast.isNotEmpty()) item("movie_cast_section") {
                MovieCastSection(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    cast = movie.cast,
                )
            }
        }
    }
}

@Composable
private fun BackgroundImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
        )
    }
}

@Composable
private fun PosterInfoSection(
    movie: Movie,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(MaterialTheme.shapes.extraSmall),
        ) {
            AsyncImage(
                model = movie.thumbnail,
                contentDescription = movie.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(4f / 5f),
                contentScale = ContentScale.Crop,
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides MaterialTheme.colorScheme.onSurface,
                LocalTextStyle provides MaterialTheme.typography.labelLarge
            ) {
                YearInfo(year = movie.year.toString())
                DurationInfo(duration = movie.duration)
                ImdbRating(rating = movie.rating)
                LikesCount(likes = movie.likes)
            }
        }
    }
}

@Composable
private fun AboutSection(
    summary: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.movie_details_screen_about_section_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExpandingText(
            text = summary,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ),
        )
    }
}

@Composable
private fun YoutubeTrailerSection(
    onWatchTrailerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier) {
        ElevatedButton(onWatchTrailerClick) {
            Image(
                painter = painterResource(R.drawable.youtube_logo),
                contentDescription = null,
                modifier = Modifier.width(18.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(stringResource(R.string.movie_details_screen_trailer_section_button_title))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GenresSection(
    genres: List<String>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.movie_details_screen_genres_section_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            genres.forEach { genre ->
                Text(
                    text = stringResource(
                        R.string.movie_details_screen_genres_section_genre_item,
                        genre
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun MovieCastSection(
    cast: List<Cast>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.movie_details_screen_cast_section_title),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = cast,
                key = { it.name }
            ) { cast ->
                ActorCard(cast = cast)
            }
        }
    }
}

@Composable
private fun ActorCard(
    cast: Cast
) {
    Column(
        modifier = Modifier.widthIn(max = 128.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(96.dp),
            shape = CircleShape,
            tonalElevation = 1.dp,
        ) {
            AsyncImage(
                model = cast.avatarUrl,
                contentDescription = cast.name,
                contentScale = ContentScale.Crop,

                )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = cast.name,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = cast.characterName,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun YearInfo(
    year: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.CalendarToday,
            contentDescription = null
        )

        Text(year)
    }
}

@Composable
private fun DurationInfo(
    duration: Duration,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Outlined.WatchLater,
            contentDescription = null
        )

        Text(
            text = duration.toString()
        )
    }
}

@Composable
private fun ImdbRating(
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.imdb_logo),
            contentDescription = null,
            modifier = Modifier.height(18.dp),
        )

        Text(
            text = stringResource(
                R.string.movie_details_screen_poster_info_section_rating,
                rating
            )
        )
    }
}

@Composable
private fun LikesCount(
    likes: Int,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            tint = Color(0xffef4444),
        )

        val formatted = remember(likes) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                NumberFormatter
                    .withLocale(Locale.getDefault())
                    .notation(Notation.compactShort())
                    .decimal(NumberFormatter.DecimalSeparatorDisplay.AUTO)
                    .precision(Precision.maxSignificantDigits(2))
                    .format(likes)
                    .toString()
            } else {
                likes.toString()
            }
        }

        Text(
            text = stringResource(
                R.string.movie_details_screen_poster_info_section_likes,
                formatted
            )
        )
    }
}