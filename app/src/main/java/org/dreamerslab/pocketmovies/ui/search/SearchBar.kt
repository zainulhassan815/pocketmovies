package org.dreamerslab.pocketmovies.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.dreamerslab.pocketmovies.R
import org.dreamerslab.pocketmovies.data.api.OrderBy
import org.dreamerslab.pocketmovies.data.api.SortBy

@Composable
internal fun SearchBar(
    query: SearchQuery,
    onChange: (query: SearchQuery) -> Unit,
    onNavigateUp: () -> Unit,
    onClear: () -> Unit,
) {
    Column(
        modifier = Modifier
            .background(
                brush = Brush.verticalGradient(
                    0f to MaterialTheme.colorScheme.surface,
                    1f to MaterialTheme.colorScheme.surface.copy(alpha = 0.5f),
                )
            ),
    ) {
        Spacer(modifier = Modifier.windowInsetsTopHeight(WindowInsets.systemBars))

        TextField(
            value = query.text,
            onValueChange = { onChange(query.copy(text = it)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = 8.dp,
                    horizontal = 16.dp,
                ),
            shape = RoundedCornerShape(4.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
            placeholder = {
                Text(stringResource(R.string.search_screen_search_field_placeholder))
            },
            leadingIcon = {
                IconButton(onNavigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                    )
                }
            },
            trailingIcon = {
                IconButton(onClear) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = null,
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                // container color
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                focusedContainerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),

                // text color
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onSurface,

                // placeholder color
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedPlaceholderColor = MaterialTheme.colorScheme.surfaceVariant,

                // icon colors
                unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                focusedLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                focusedTrailingIconColor = MaterialTheme.colorScheme.onSurface,

                // indicator color
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
            )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SortByFilter(
                selected = query.sortBy,
                onChange = { onChange(query.copy(sortBy = it)) }
            )

            OrderByFilter(
                selected = query.orderBy,
                onChange = { onChange(query.copy(orderBy = it)) }
            )
        }
    }
}

@Composable
private fun SortByFilter(
    selected: SortBy,
    onChange: (sortBy: SortBy) -> Unit,
    options: List<SortBy> = listOf(SortBy.Title, SortBy.Year, SortBy.Rating, SortBy.DateAdded, SortBy.DownloadCount),
) {
    var showDropDown by remember { mutableStateOf(false) }

    fun getResId(option: SortBy): Int = when (option) {
        SortBy.Title -> R.string.sort_by_title
        SortBy.Year -> R.string.sort_by_year
        SortBy.Rating -> R.string.sort_by_rating
        SortBy.DateAdded -> R.string.sort_by_date_added
        SortBy.DownloadCount -> R.string.sort_by_downloads
        else -> throw IllegalArgumentException("$option option is not supported")
    }

    val sortByOptions = remember(options) { options.associateWith(::getResId) }

    Box {
        AssistChip(
            onClick = { showDropDown = !showDropDown },
            label = {
                Text(stringResource(getResId(selected)))
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Sort,
                    contentDescription = null,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        )

        DropdownMenu(
            expanded = showDropDown,
            onDismissRequest = { showDropDown = false }
        ) {
            sortByOptions.entries.forEach { (option, resId) ->
                DropdownMenuItem(
                    text = { Text(stringResource(resId)) },
                    onClick = {
                        showDropDown = false
                        onChange(option)
                    },
                    trailingIcon = {
                        if (selected == option) Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun OrderByFilter(
    selected: OrderBy,
    onChange: (order: OrderBy) -> Unit,
    options: List<OrderBy> = listOf(OrderBy.Ascending, OrderBy.Descending)
) {
    var showDropDown by remember { mutableStateOf(false) }

    fun getResId(order: OrderBy): Int = when (order) {
        OrderBy.Ascending -> R.string.order_by_asc
        OrderBy.Descending -> R.string.order_by_desc
    }

    val orderByOptions = remember(options) { options.associateWith(::getResId) }

    Box {
        AssistChip(
            onClick = { showDropDown = !showDropDown },
            label = {
                Text(stringResource(getResId(selected)))
            },
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier.size(AssistChipDefaults.IconSize)
                )
            },
            colors = AssistChipDefaults.assistChipColors(
                containerColor = MaterialTheme.colorScheme.surface,
            )
        )

        DropdownMenu(
            expanded = showDropDown,
            onDismissRequest = { showDropDown = false }
        ) {
            orderByOptions.entries.forEach { (option, resId) ->
                DropdownMenuItem(
                    text = { Text(stringResource(resId)) },
                    onClick = {
                        showDropDown = false
                        onChange(option)
                    },
                    trailingIcon = {
                        if (selected == option) Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = null,
                        )
                    }
                )
            }
        }
    }
}