package org.dreamerslab.pocketmovies.data.models

import com.squareup.moshi.Json
import kotlinx.datetime.Instant
import org.dreamerslab.pocketmovies.domain.models.Torrent

data class TorrentDto(
    @field:Json(name = "url")
    val url: String,

    @field:Json(name = "hash")
    val hash: String,

    @field:Json(name = "quality")
    val quality: String,

    @field:Json(name = "type")
    val type: String,

    @field:Json(name = "is_repack")
    val isRepack: String,

    @field:Json(name = "video_codec")
    val videoCodec: String,

    @field:Json(name = "bit_depth")
    val bitDepth: String,

    @field:Json(name = "audio_channels")
    val audioChannels: String,

    @field:Json(name = "seeds")
    val seeds: Long,

    @field:Json(name = "peers")
    val peers: Long,

    @field:Json(name = "size")
    val size: String,

    @field:Json(name = "size_bytes")
    val sizeBytes: Long,

    @field:Json(name = "date_uploaded")
    val dateUploaded: String,

    @field:Json(name = "date_uploaded_unix")
    val dateUploadedUnix: Long,
) {
    fun mapToDomainTorrent(): Torrent = Torrent(
        url = this.url,
        hash = this.hash,
        quality = this.quality,
        type = this.type,
        size = this.size,
        dateUploaded = Instant.fromEpochSeconds(this.dateUploadedUnix)
    )
}