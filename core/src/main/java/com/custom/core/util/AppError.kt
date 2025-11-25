package com.custom.core.util

import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

sealed class AppError {
    data object NetworkError : AppError()

    data object NotFound : AppError()

    data object InvalidData : AppError()

    data object JobCanceled : AppError()

    data object LocationDisabled : AppError()

    data class Unknown(val message: String?) : AppError()
}

fun Throwable.toAppError(): AppError {
    return when  {
        this is IOException -> AppError.NetworkError
        this is CancellationException -> AppError.JobCanceled
        this is IllegalStateException -> AppError.LocationDisabled
        this is retrofit2.HttpException && this.code() == 404 -> AppError.NotFound
        else -> AppError.Unknown(this.message)
    }
}