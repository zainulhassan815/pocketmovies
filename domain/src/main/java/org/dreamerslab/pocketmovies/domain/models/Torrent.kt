package org.dreamerslab.pocketmovies.domain.models

import kotlinx.datetime.Instant

data class Torrent(
    val url: String,
    val hash: String,
    val quality: String,
    val type: String,
    val size: String,
    val dateUploaded: Instant,
)