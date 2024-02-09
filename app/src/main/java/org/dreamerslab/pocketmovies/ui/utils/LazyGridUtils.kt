package org.dreamerslab.pocketmovies.ui.utils

import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable

/**
 * Spans item across full width.
 */
fun LazyGridScope.spannedItem(
    key: Any? = null,
    contentType: Any? = null,
    content: @Composable LazyGridItemScope.() -> Unit
) {
    item(
        key = key,
        span = { GridItemSpan(maxLineSpan) },
        contentType = contentType,
        content = content
    )
}