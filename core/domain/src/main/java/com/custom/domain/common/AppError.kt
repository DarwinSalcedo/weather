package com.custom.domain.common

sealed class AppError {
    data object NetworkError : AppError()

    data object NotFound : AppError()

    data object InvalidData : AppError()

    data object JobCanceled : AppError()

    data object LocationDisabled : AppError()

    data object LocationCanceled : AppError()

    data class Unknown(val message: String?) : AppError()
}