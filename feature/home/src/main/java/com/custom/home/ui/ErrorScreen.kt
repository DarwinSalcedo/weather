package com.custom.home.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.custom.common.components.DefaultBodyContent
import com.custom.home.R

@Composable
fun ErrorScreen(message: String) {
    DefaultBodyContent(
        Icons.Default.Warning,
        stringResource(R.string.error),
        stringResource(R.string.ups_unexpected_error),
        message
    )
}