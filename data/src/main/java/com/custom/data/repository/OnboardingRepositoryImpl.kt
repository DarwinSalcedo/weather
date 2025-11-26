package com.custom.data.repository

import android.content.SharedPreferences
import com.custom.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val dispatcher: CoroutineDispatcher
) : OnboardingRepository {

    private val ONBOARDING_COMPLETED_KEY = "onboarding_completed"


    override fun hasUserSeenOnboarding(): Flow<Boolean> = callbackFlow {

        val initialValue = sharedPreferences.getBoolean(ONBOARDING_COMPLETED_KEY, false)
        trySend(initialValue)

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
            if (key == ONBOARDING_COMPLETED_KEY) {
                trySend(sharedPrefs.getBoolean(ONBOARDING_COMPLETED_KEY, false))
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }


    override suspend fun completeOnboarding() {
        withContext(dispatcher) {
            sharedPreferences.edit()
                .putBoolean(ONBOARDING_COMPLETED_KEY, true)
                .apply()
        }
    }
}