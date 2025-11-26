package com.custom.domain.repository

import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {

    fun hasUserSeenOnboarding(): Flow<Boolean>

    suspend fun completeOnboarding()
}