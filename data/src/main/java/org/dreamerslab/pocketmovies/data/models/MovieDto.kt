package org.dreamerslab.pocketmovies.data.models

import com.squareup.moshi.Json
import kotlinx.datetime.Instant
import org.dreamerslab.pocketmovies.domain.models.Movie
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class MovieDto(
    @field:Json(name = "id")
    val id: Int,

    @field:Json(name = "url")
    val url: String,

    @field:Json(name = "imdb_code")
    val imdbCode: String,

    @field:Json(name = "title")
    val title: String,

    @field:Json(name = "title_english")
    val titleEnglish: String?,

    @field:Json(name = "title_long")
    val titleLong: String?,

    @field:Json(name = "slug")
    val slug: String,

    @field:Json(name = "year")
    val year: Int,

    @field:Json(name = "rating")
    val rating: Double,

    @field:Json(name = "runtime")
    val runtime: Int,

    @field:Json(name = "genres")
    val genres: List<String>,

    @field:Json(name = "like_count")
    val likeCount: Int?,

    @field:Json(name = "summary")
    val summary: String?,

    @field:Json(name = "description_intro")
    val descriptionIntro: String?,

    @field:Json(name = "description_full")
    val descriptionFull: String?,

    @field:Json(name = "yt_trailer_code")
    val ytTrailerCode: String?,

    @field:Json(name = "language")
    val language: String?,

    @field:Json(name = "mpa_rating")
    val mpaRating: String?,

    @field:Json(name = "background_image")
    val backgroundImage: String,

    @field:Json(name = "background_image_original")
    val backgroundImageOriginal: String,

    @field:Json(name = "small_cover_image")
    val smallCoverImage: String,

    @field:Json(name = "medium_cover_image")
    val mediumCoverImage: String,

    @field:Json(name = "large_cover_image")
    val largeCoverImage: String,

    @field:Json(name = "cast")
    val cast: List<CastDto>?,

    @field:Json(name = "torrents")
    val torrents: List<TorrentDto>?,

    @field:Json(name = "date_uploaded")
    val dateUploaded: String,

    @field:Json(name = "date_uploaded_unix")
    val dateUploadedUnix: Long
) {
    fun mapToDomainMovie(): Movie = Movie(
        id = this.id,
        imdbCode = this.imdbCode,
        title = this.title,
        summary = this.summary ?: this.descriptionIntro ?: this.descriptionFull ?: "",
        thumbnail = this.mediumCoverImage,
        backgroundImage = this.backgroundImage,
        year = this.year,
        likes = this.likeCount ?: 0,
        rating = this.rating,
        duration = this.runtime.toDuration(DurationUnit.MINUTES),
        genres = this.genres,
        cast = this.cast?.map(CastDto::mapToDomainCast) ?: emptyList(),
        torrents = this.torrents?.map { it.mapToDomainTorrent() } ?: emptyList(),
        youtubeVideoCode = this.ytTrailerCode,
        dateUploaded = Instant.fromEpochSeconds(this.dateUploadedUnix),
    )
}