package com.custom.language.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.custom.language.R


@Composable
fun LanguageListItem(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(name) },
        leadingContent = {
            if (isSelected) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = stringResource(R.string.selected),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Spacer(Modifier.width(24.dp))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    )
}