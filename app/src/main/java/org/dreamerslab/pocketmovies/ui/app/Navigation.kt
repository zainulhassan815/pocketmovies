package org.dreamerslab.pocketmovies.ui.app

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import org.dreamerslab.pocketmovies.domain.models.Movie
import org.dreamerslab.pocketmovies.ui.home.HomeScreen
import org.dreamerslab.pocketmovies.ui.moviedetails.MovieDetailsScreen
import org.dreamerslab.pocketmovies.ui.search.SearchScreen

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Search : Screen("screen")
    data object MovieDetails : Screen("movie-details")
}

sealed class LeafScreen(
    route: String,
    val requiredArguments: List<NamedNavArgument> = emptyList(),
    val optionalArguments: List<NamedNavArgument> = emptyList(),
) : Screen(route) {

    val arguments = requiredArguments + optionalArguments

    fun buildPathWithoutArguments(root: Screen): String {
        return "${root.route}/$route"
    }

    fun buildPath(root: Screen): String {
        return buildPathWithoutArguments(root)
            .appendRequiredArguments(requiredArguments)
            .appendOptionalArguments(optionalArguments)
    }

    data object Home : LeafScreen("home")
    data object Search : LeafScreen("search")
    data object MovieDetails : LeafScreen(
        route = "movie-details",
        requiredArguments = listOf(
            navArgument(Arguments.argumentMovieId) {
                type = NavType.IntType
            }
        )
    ) {
        fun buildPath(rootScreen: Screen, movie: Movie) = Uri.Builder()
            .path(buildPathWithoutArguments(rootScreen))
            .appendPath(movie.id.toString())
            .build()
            .toString()

        object Arguments {
            const val argumentMovieId = "movieId"
        }
    }

}

private fun String.appendRequiredArguments(navArguments: List<NamedNavArgument>): String {
    val arguments = navArguments
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "/", prefix = "/") { "{${it.name}}" }
        .orEmpty()
    return "$this$arguments"
}

private fun String.appendOptionalArguments(navArguments: List<NamedNavArgument>): String {
    val arguments = navArguments
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "&", prefix = "?") { "${it.name}={${it.name}}" }
        .orEmpty()
    return "$this$arguments"
}

@Composable
fun PocketMoviesNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
            )
        }
    ) {
        addHomeScreen(navController)
    }
}

private fun NavGraphBuilder.addHomeScreen(
    controller: NavController
) {
    navigation(
        route = Screen.Home.route,
        startDestination = LeafScreen.Home.buildPath(Screen.Home)
    ) {
        addHomeLeafScreen(controller, Screen.Home)
        addSearchScreen(controller, Screen.Home)
        addMovieDetailsLeafScreen(controller, Screen.Home)
    }
}

private fun NavController.navigateToSearch(
    root: Screen
) = navigate(LeafScreen.Search.buildPath(root))

private fun NavGraphBuilder.addSearchScreen(
    controller: NavController,
    root: Screen
) {
    navigation(
        route = Screen.Search.route,
        startDestination = LeafScreen.Search.buildPath(root)
    ) {
        addSearchLeafScreen(controller, root)

        addMovieDetailsScreen(controller, Screen.Search)
    }
}

private fun NavController.navigateToMovieDetails(
    root: Screen,
    movie: Movie
) = navigate(LeafScreen.MovieDetails.buildPath(root, movie))

private fun NavGraphBuilder.addMovieDetailsScreen(
    controller: NavController,
    root: Screen,
) {
    navigation(
        route = Screen.MovieDetails.route,
        startDestination = LeafScreen.MovieDetails.buildPath(root)
    ) {
        addMovieDetailsLeafScreen(controller, root)

        addMovieDetailsLeafScreen(controller, Screen.MovieDetails)
    }
}

private fun NavGraphBuilder.addHomeLeafScreen(
    controller: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.Home.buildPath(root),
    ) {
        HomeScreen(
            onSearchClick = {
                controller.navigateToSearch(root)
            },
            onMovieClick = { movie ->
                controller.navigateToMovieDetails(root, movie)
            }
        )
    }
}

private fun NavGraphBuilder.addSearchLeafScreen(
    controller: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.Search.buildPath(root),
    ) {
        SearchScreen(
            onNavigateUp = controller::navigateUp,
            onMovieClick = { movie ->
                controller.navigateToMovieDetails(root, movie)
            }
        )
    }
}

private fun NavGraphBuilder.addMovieDetailsLeafScreen(
    controller: NavController,
    root: Screen
) {
    composable(
        route = LeafScreen.MovieDetails.buildPath(root),
        arguments = LeafScreen.MovieDetails.arguments
    ) {
        MovieDetailsScreen(
            onNavigateUp = controller::navigateUp
        )
    }
}