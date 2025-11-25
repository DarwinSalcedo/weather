package com.custom.core.util

sealed class OperationResult<out T> {
    data class Success<out T>(val data: T) : OperationResult<T>()
    data class Failure(val error: AppError) : OperationResult<Nothing>()
}