package com.custom.home.domain.usecase

import com.custom.core.repository.CitySearchRepository
import com.custom.core.util.OperationResult
import com.custom.core.util.toAppError
import com.custom.home.domain.model.CityUiModel
import com.custom.home.domain.toUiModel
import com.custom.home.ui.MINIMUM_CHARACTERS_TO_SEARCH
import javax.inject.Inject

class SearchCitiesUseCase @Inject constructor(
    private val citySearchRepository: CitySearchRepository
) {

    suspend operator fun invoke(params: String): OperationResult<List<CityUiModel>> {

        if (params.isBlank() || params.length < MINIMUM_CHARACTERS_TO_SEARCH) {
            return OperationResult.Success(emptyList())
        }

        val result = citySearchRepository.searchCities(params)

        return result.fold(
            onSuccess = { domainList ->
                OperationResult.Success(domainList.map { it.toUiModel() })
            },
            onFailure = { error ->
                OperationResult.Failure(error.toAppError())
            }
        )
    }
}