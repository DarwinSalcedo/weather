package com.custom.common.util


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.custom.domain.common.AppError
import com.custom.ui.R

@Composable
fun AppError.toUserMessage(): String {
    return when (this) {
        AppError.NetworkError -> stringResource(id = R.string.error_network)
        AppError.NotFound -> stringResource(id = R.string.error_not_found)
        AppError.InvalidData -> stringResource(id = R.string.error_invalid_data)
        AppError.LocationDisabled -> stringResource(id = R.string.error_location_disabled)
        AppError.LocationCanceled -> stringResource(id = R.string.error_location_canceled)
        is AppError.Unknown -> {
            val message = this.message
            if (message != null) {
                stringResource(id = R.string.error_unknown, message)
            } else {
                stringResource(id = R.string.error_unknown_unavailable)
            }
        }

        AppError.JobCanceled -> ""
    }
}