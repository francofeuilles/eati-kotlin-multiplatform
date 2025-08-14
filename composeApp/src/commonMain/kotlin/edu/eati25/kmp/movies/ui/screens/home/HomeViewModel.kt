package edu.eati25.kmp.movies.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.eati25.kmp.movies.data.Movie
import edu.eati25.kmp.movies.data.MoviesRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    var state by mutableStateOf(UiState())
        private set

    init {
        viewModelScope.launch {
            state = UiState(isLoading = true)
            moviesRepository.movies.collect { movies ->
                if (movies.isNotEmpty()) {
                    state =
                        UiState(
                            isLoading = false,
                            movies = movies
                        )
                }
            }
        }
    }

    data class UiState(
        val isLoading: Boolean = false,
        val movies: List<Movie> = emptyList(),
    )
}