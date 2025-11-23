package com.custom.data.geo

import com.custom.core.model.CityModel
import com.custom.data.dto.CityDto
import com.custom.data.remote.GeoApi
import com.custom.data.repository.CitySearchRepositoryImpl
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.io.IOException

class CitySearchRepositoryTest {

    private val geoApiService: GeoApi = mockk()
    private lateinit var repository: CitySearchRepositoryImpl

    private val mockCityDto = listOf(
        CityDto(
            name = "Paris",
            country = "FR",
            latitude = 48.8566,
            longitude = 2.3522
        )
    )

    private val expectedModel = CityModel(
        name = "Paris",
        country = "FR",
        latitude = 48.8566,
        longitude = 2.3522
    )

    @Before
    fun setUp() {
        repository = CitySearchRepositoryImpl(geoApiService)
    }

    @Test
    fun `searchCities should return success result with correct model on successful API response`() = runTest {
        val query = "Paris"

        // Configuración del mock: Devuelve la lista de DTOs simulada
        coEvery { geoApiService.searchCities(query) } returns mockCityDto

        // Ejecutar la función del repositorio
        val result = repository.searchCities(query)

        // Afirmaciones
        assertTrue(result.isSuccess)

        // Verifica que la lista tenga el tamaño correcto y que el primer elemento sea idéntico al modelo esperado
        val modelList = result.getOrThrow()
        assertEquals(1, modelList.size)
        assertEquals(expectedModel, modelList.first())
    }

    @Test
    fun `searchCities should return failure result on API exception`() = runTest {
        val query = "ErrorCity"

        // Configuración del mock: Lanza un error de conexión
        coEvery { geoApiService.searchCities(query) } throws IOException("API not reachable")

        // Ejecutar la función del repositorio
        val result = repository.searchCities(query)

        // Afirmaciones
        assertTrue(result.isFailure) // Debe ser un fallo
        assertTrue(result.exceptionOrNull() is IOException) // Debe ser una excepción de red
    }
}