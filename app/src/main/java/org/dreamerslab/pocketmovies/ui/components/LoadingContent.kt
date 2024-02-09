package org.dreamerslab.pocketmovies.ui.components

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.dreamerslab.pocketmovies.R

@Composable
fun LoadingContent(
    modifier: Modifier = Modifier,
    message: String = stringResource(R.string.loading_more_content)
) {
    CompositionLocalProvider(
        LocalContentColor provides MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val infiniteTransition = rememberInfiniteTransition(
                label = "Loading Content Icon"
            )
            val angle by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1500,
                        easing = EaseInOut,
                    ),
                ),
                label = "Loading Content Icon Rotation"
            )


            Icon(
                imageVector = Icons.Rounded.HourglassEmpty,
                contentDescription = null,
                modifier = Modifier.rotate(angle)
            )

            Text(
                text = message,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}