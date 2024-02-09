package org.dreamerslab.pocketmovies.data.models

import com.squareup.moshi.Json
import org.dreamerslab.pocketmovies.domain.models.Cast

data class CastDto(
    @field:Json(name = "name")
    val name: String,

    @field:Json(name = "character_name")
    val characterName: String,

    @field:Json(name = "imdb_code")
    val imdbCode: String,

    @field:Json(name = "url_small_image")
    val urlSmallImage: String? = null,
) {
    fun mapToDomainCast(): Cast = Cast(
        name = this.name,
        characterName = this.characterName,
        imdbCode = this.imdbCode,
        avatarUrl = this.urlSmallImage,
    )
}