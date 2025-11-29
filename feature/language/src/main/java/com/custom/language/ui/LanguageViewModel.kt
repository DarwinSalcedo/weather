package com.custom.language.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.custom.di.IoDispatcher
import com.custom.domain.model.LanguageModel
import com.custom.domain.repository.LanguageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class LanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _availableLanguages = MutableStateFlow<List<LanguageModel>>(emptyList())
    val availableLanguages = _availableLanguages.asStateFlow()

    private val _selectedLanguageCode = MutableStateFlow<String?>(null)
    val selectedLanguageCode: StateFlow<String?> = _selectedLanguageCode.asStateFlow()

    init {
        fetchAvailableLanguages()
        _selectedLanguageCode.value = Locale.getDefault().language
    }

    fun selectLanguage(code: String) {
        _selectedLanguageCode.value = code
    }

    fun saveLanguageAndComplete(onLanguageSelected: () -> Unit) {
        val selectedCode = _selectedLanguageCode.value
        if (selectedCode != null) {
            viewModelScope.launch(dispatcher) {
                languageRepository.saveLanguage(selectedCode)
            }
            viewModelScope.launch {
                onLanguageSelected()
            }
        }
    }

    fun fetchAvailableLanguages() {
        viewModelScope.launch(dispatcher) {
            languageRepository.getLanguages().collect {
                _availableLanguages.value = it
            }
        }
    }
}