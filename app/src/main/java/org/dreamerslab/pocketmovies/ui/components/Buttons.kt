package org.dreamerslab.pocketmovies.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.dreamerslab.pocketmovies.R

@Composable
fun LoadMoreContentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        FilledTonalButton(
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.home_screen_movies_section_load_more_button_title))
        }
    }
}