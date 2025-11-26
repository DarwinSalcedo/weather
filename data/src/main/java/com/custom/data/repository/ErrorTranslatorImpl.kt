package com.custom.data.repository

import com.custom.domain.common.AppError
import com.custom.domain.exception.LocationCanceledException
import com.custom.domain.exception.LocationDisabledException
import com.custom.domain.repository.ErrorTranslator
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ErrorTranslatorImpl  @Inject constructor() : ErrorTranslator {

    override fun map(throwable: Throwable): AppError {
        return when (val error = throwable) {

            is IOException -> AppError.NetworkError

            is CancellationException -> AppError.JobCanceled

            is LocationCanceledException -> AppError.LocationDisabled

            is LocationDisabledException -> AppError.LocationCanceled

            is HttpException -> mapHttpError(error.code(), error.message)

            else -> AppError.Unknown(error.message)
        }
    }

    private fun mapHttpError(code: Int, message: String?): AppError {
        return when (code) {
            404 -> AppError.NotFound
            in 400..499 -> AppError.InvalidData
            in 500..599 -> AppError.Unknown("$message $code")
            else -> AppError.Unknown("$message $code")
        }
    }
}