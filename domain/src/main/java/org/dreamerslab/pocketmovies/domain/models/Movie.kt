package org.dreamerslab.pocketmovies.domain.models

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class Movie(
    val id: Int,
    val imdbCode: String,
    val title: String,
    val summary: String,
    val thumbnail: String,
    val backgroundImage: String,
    val year: Int,
    val likes: Int,
    val rating: Double,
    val duration: Duration,
    val genres: List<String> = emptyList(),
    val torrents: List<Torrent> = emptyList(),
    val cast: List<Cast> = emptyList(),
    val youtubeVideoCode: String? = null,
    val dateUploaded: Instant
)