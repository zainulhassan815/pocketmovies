package org.dreamerslab.pocketmovies.ui.moviedetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.dreamerslab.pocketmovies.data.repository.YtsMoviesRepository
import org.dreamerslab.pocketmovies.domain.models.Movie
import javax.inject.Inject

sealed class MovieDetailsUiState {
    data object InvalidMovieId : MovieDetailsUiState()
    data object Initial : MovieDetailsUiState()
    data object Loading : MovieDetailsUiState()
    data object Error : MovieDetailsUiState()
    data class Success(
        val movie: Movie
    ) : MovieDetailsUiState()
}

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val ytsMoviesRepository: YtsMoviesRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val movieId: Int? = savedStateHandle["movieId"]

    private val _state: MutableStateFlow<MovieDetailsUiState> = MutableStateFlow(
        value = when (movieId) {
            null -> MovieDetailsUiState.InvalidMovieId
            else -> MovieDetailsUiState.Initial
        }
    )
    val state: StateFlow<MovieDetailsUiState> = _state.asStateFlow()

    init {
        loadMovieDetails()
    }

    fun loadMovieDetails() {
        viewModelScope.launch {
            if (movieId == null) return@launch

            _state.update { MovieDetailsUiState.Loading }

            ytsMoviesRepository
                .getMovieDetails(movieId)
                .fold(
                    ifLeft = {
                        _state.update { MovieDetailsUiState.Error }
                    },
                    ifRight = { movie ->
                        val nextState = when (movie) {
                            null -> MovieDetailsUiState.InvalidMovieId
                            else -> MovieDetailsUiState.Success(movie)
                        }
                        _state.update { nextState }
                    }
                )
        }
    }
}