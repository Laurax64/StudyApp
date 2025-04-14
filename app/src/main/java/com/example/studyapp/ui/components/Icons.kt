package com.example.studyapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * An icon with the recommended size and tint for the leading icon in a top bar.
 */
@Composable
fun TopBarLeadingIcon(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Icon(
        painter = painterResource(id = iconRes),
        tint = MaterialTheme.colorScheme.onSurface,
        contentDescription = contentDescription,
        modifier = modifier
            .size(24.dp)
            .clickable { onClick() }
    )
}

/**
 * An icon with the recommended size and tint for the trailing icon in a top bar.
 */
@Composable
fun TopBarTrailingIcon(
    iconRes: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    Icon(
        painter = painterResource(id = iconRes),
        tint = MaterialTheme.colorScheme.onSurfaceVariant,
        contentDescription = contentDescription,
        modifier = modifier
            .size(24.dp)
            .clickable { onClick() }
    )
}

