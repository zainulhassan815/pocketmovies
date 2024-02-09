package org.dreamerslab.pocketmovies.data.api

/**
 * Orders the results by either Ascending or Descending order
 */
enum class OrderBy {
    Ascending,
    Descending;

    override fun toString(): String = when (this) {
        Ascending -> "asc"
        Descending -> "desc"
    }
}

/**
 * Sorts the results by chosen value
 */
enum class SortBy {
    Title,
    Year,
    Rating,
    Peers,
    Seeds,
    DownloadCount,
    LikeCount,
    DateAdded;

    override fun toString(): String = when (this) {
        Title -> "title"
        Year -> "year"
        Rating -> "rating"
        Peers -> "peers"
        Seeds -> "seeds"
        DownloadCount -> "download_count"
        LikeCount -> "like_count"
        DateAdded -> "date_added"
    }
}