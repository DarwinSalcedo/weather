package com.custom.home.usecase

import com.custom.domain.common.OperationResult
import com.custom.domain.model.CityModel
import com.custom.domain.repository.CitySearchRepository
import com.custom.domain.repository.ErrorTranslator
import com.custom.home.domain.model.CityUiModel
import com.custom.home.domain.mapper.toUiModel
import com.custom.home.domain.usecase.SearchCitiesUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SearchCitiesUseCaseTest {

    private val mockCitySearchRepository: CitySearchRepository = mockk()
    private val mockErrorTranslator: ErrorTranslator = mockk()

    private lateinit var useCase: SearchCitiesUseCase

    private val domainCityModel = CityModel(
        name = "Paris",
        country = "FR",
        latitude = 48.8566,
        longitude = 2.3522
    )
    private val mockDomainList = listOf(domainCityModel)

    private val expectedUiModel = domainCityModel.toUiModel()
    private val expectedUiList = listOf(expectedUiModel)


    @Before
    fun setUp() {
        useCase = SearchCitiesUseCase(mockCitySearchRepository,mockErrorTranslator)
    }

    @Test
    fun `Given a valid query, when invoke is called, then should return success with mapped CityUiModel list on successful fetch`() =
        runTest {
            val query = "Paris"

            coEvery { mockCitySearchRepository.searchCities(query) } returns Result.success(
                mockDomainList
            )

            val result = useCase.invoke(query)

            Assert.assertTrue(result is OperationResult.Success)
            val actualUiList = (result as OperationResult.Success).data

            Assert.assertEquals(1, actualUiList.size)
            Assert.assertEquals(expectedUiList.first().name, actualUiList.first().name)
            Assert.assertTrue(actualUiList.first() is CityUiModel)
        }

    @Test
    fun `Given an invalid query, when invoke is called, then should return Failure result on repository exception`() =
        runTest {
            val query = "Madrid"
            val mockException = IOException("Connection reset by peer")

            coEvery { mockCitySearchRepository.searchCities(query) } returns Result.failure(
                mockException
            )

            val result = useCase.invoke(query)

            Assert.assertTrue(result is OperationResult.Failure)

        }

    @Test
    fun `Given a short query, when invoke is called, then should invoke should return empty list `() =
        runTest {
            val query = "PA"

            coEvery { mockCitySearchRepository.searchCities(any()) } throws IllegalStateException("Should not be called")

            val result = useCase.invoke(query)

            Assert.assertTrue(result is OperationResult.Success)
            Assert.assertTrue((result as OperationResult.Success).data.isEmpty())
        }

    @Test
    fun `Given a blank query, when invoke is called, then should invoke should return empty list for blank query`() =
        runTest {
            val query = " "

            coEvery { mockCitySearchRepository.searchCities(any()) } throws IllegalStateException("Should not be called")

            val result = useCase.invoke(query)

            Assert.assertTrue(result is OperationResult.Success)
            Assert.assertTrue((result as OperationResult.Success).data.isEmpty())
        }
}