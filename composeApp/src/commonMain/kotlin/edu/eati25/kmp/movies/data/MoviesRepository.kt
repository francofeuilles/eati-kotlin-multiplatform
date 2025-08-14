package edu.eati25.kmp.movies.data

import edu.eati25.kmp.movies.data.database.MoviesDao
import kotlinx.coroutines.flow.onEach

class MoviesRepository(
    private val moviesDao: MoviesDao,
    private val moviesService: MoviesService
) {

    val movies = moviesDao.getPopularMovies().onEach { movies ->
        if (movies.isEmpty()) {
            val popularMovies = moviesService.getPopularMovies().results.map {
                it.toDomainMovie()
            }
            moviesDao.save(popularMovies)
        }
    }

    fun getMovieById(id: Int) = moviesDao.getMovieById(id).onEach {
        if (it == null) {
            val movie = moviesService.getMovieDetails(id).toDomainMovie()
            moviesDao.save(listOf(movie))
        }
    }

    suspend fun toggleFavorite(movie: Movie) {
        moviesDao.save(listOf(movie.copy(isFavorite = !movie.isFavorite)))
    }

    private fun RemoteMovie.toDomainMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate,
            poster = "https://image.tmdb.org/t/p/w185$posterPath",
            backdrop = backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity,
            voteAverage = voteAverage,
            isFavorite = false
        )
    }
}