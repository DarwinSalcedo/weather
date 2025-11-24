package com.custom.home.domain.usecase

import com.custom.core.repository.CitySearchRepository
import com.custom.home.domain.model.CityUiModel
import com.custom.home.domain.toUiModel
import com.custom.home.ui.MINIMUM_CHARACTERS_TO_SEARCH
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val citySearchRepository: CitySearchRepository
) {

    suspend operator fun invoke(query: String): Result<List<CityUiModel>> {
        if (query.isBlank() || query.length < MINIMUM_CHARACTERS_TO_SEARCH) {
            return Result.success(emptyList())
        }

        val result = citySearchRepository.searchCities(query)

        return result.fold(
            onSuccess = { domainList ->
                Result.success(domainList.map { it.toUiModel() })
            },
            onFailure = { error ->
                Result.failure(error)
            }
        )
    }
}