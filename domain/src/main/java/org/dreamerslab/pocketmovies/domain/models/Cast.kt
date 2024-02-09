package org.dreamerslab.pocketmovies.domain.models

data class Cast(
    val name: String,
    val characterName: String,
    val imdbCode: String,
    val avatarUrl: String? = null,
)