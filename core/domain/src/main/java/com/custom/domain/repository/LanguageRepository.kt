package com.custom.domain.repository

import com.custom.domain.model.LanguageModel
import kotlinx.coroutines.flow.Flow


interface LanguageRepository {

    suspend fun getLanguages(): Flow<List<LanguageModel>>

    suspend fun saveLanguage(languageCode: String)

    suspend fun hasInitialLanguageBeenSet(): Flow<String?>

}