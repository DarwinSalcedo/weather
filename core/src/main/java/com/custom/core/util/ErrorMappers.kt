package com.custom.core.util


fun mapAppErrorToUserMessage(error: AppError): String {
    return when (error) {
        AppError.NetworkError -> "Network error, the server is not working"
        AppError.NotFound -> "Resource not found, check the url and try again"
        AppError.InvalidData -> "The data is not valid, check the data and try again."
        AppError.LocationDisabled -> "The location service is disabled, enable it and try again."
        is AppError.Unknown -> "Unknown error, check the logs ${error.message ?: "Unavailable error"}"
        AppError.JobCanceled -> ""
    }
}