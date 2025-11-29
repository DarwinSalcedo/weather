package com.custom.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.custom.data.R
import com.custom.domain.model.LanguageModel
import com.custom.domain.repository.LanguageRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LanguageRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferences,
    private val dispatcher: CoroutineDispatcher
) : LanguageRepository {

    private val LANGUAGE_KEY = "language"

    private var cachedLanguages: List<LanguageModel>? = null

    private suspend fun fetchAndCacheLanguages(): List<LanguageModel> {
        return withContext(dispatcher) {
            cachedLanguages?.let { return@withContext it }

            val languagesJson = context.resources.openRawResource(R.raw.languages)
                .bufferedReader()
                .use { it.readText() }

            val gson = Gson()
            val languageListType = object : TypeToken<List<LanguageModel>>() {}.type
            val languages: List<LanguageModel> = gson.fromJson(languagesJson, languageListType)
            cachedLanguages = languages
            languages
        }
    }

    override suspend fun getLanguages(): Flow<List<LanguageModel>> {
        return flowOf(fetchAndCacheLanguages())
    }

    override suspend fun saveLanguage(languageCode: String) {
        sharedPreferences.edit { putString(LANGUAGE_KEY, languageCode) }
    }

    override suspend fun hasInitialLanguageBeenSet(): Flow<String?> = callbackFlow {
        val initialValue = sharedPreferences.getString(LANGUAGE_KEY, null)
        trySend(initialValue)

        val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPrefs, key ->
            if (key == LANGUAGE_KEY) {
                trySend(sharedPrefs.getString(LANGUAGE_KEY, null))
            }
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

}