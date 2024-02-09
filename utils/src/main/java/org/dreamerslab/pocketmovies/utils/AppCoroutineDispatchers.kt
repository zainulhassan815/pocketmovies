package org.dreamerslab.pocketmovies.utils

import kotlinx.coroutines.CoroutineDispatcher

data class AppCoroutineDispatchers(
    val default: CoroutineDispatcher,
    val io: CoroutineDispatcher,
    val main: CoroutineDispatcher,
)
