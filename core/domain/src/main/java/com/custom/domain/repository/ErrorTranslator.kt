package com.custom.domain.repository

import com.custom.domain.common.AppError


interface ErrorTranslator {
    fun map(throwable: Throwable): AppError
}