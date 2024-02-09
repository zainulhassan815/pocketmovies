package org.dreamerslab.pocketmovies.ui.app

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.dreamerslab.pocketmovies.ui.styles.PocketMoviesTheme

@Composable
fun App() {
    val navController = rememberNavController()

    TrackNavigationSideEffect(navController)

    PocketMoviesTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PocketMoviesNavHost(
                navController = navController
            )
        }
    }
}

@Composable
fun TrackNavigationSideEffect(navController: NavController) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { _, destination, arguments ->
            Log.v("PocketMoviesNavigation", "$destination: $arguments")
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}
