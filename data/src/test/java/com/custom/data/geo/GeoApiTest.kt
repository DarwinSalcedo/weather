package com.custom.data.geo

import com.custom.data.remote.AuthInterceptor
import com.custom.data.remote.GeoApi
import com.google.gson.Gson
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GeoApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var geoApi: GeoApi

    private val mockCityResponseJson = """
        [
          {
            "name": "Buenos Aires",
            "lat": -34.6037,
            "lon": -58.3816,
            "country": "AR"
          },
           {
            "name": "Buenos Aires2",
            "lat": -65.6037,
            "lon": -5.3816,
            "country": "AA"
          }
        ]
    """.trimIndent()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        geoApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
            .create(GeoApi::class.java)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `Given a query when  searchCities is invoke then return a valid request with the correct parameters`() = runTest {
        val query = "Madrid"

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockCityResponseJson)
        )

        geoApi.searchCities(query)

        val recordedRequest = mockWebServer.takeRequest()

        assertEquals("/geo/1.0/direct", recordedRequest.path?.substringBefore('?'))

        assertTrue(recordedRequest.path!!.contains("q=$query"))

        assertTrue(recordedRequest.path!!.contains("limit=5"))

        assertTrue(recordedRequest.path!!.contains("appid="))
    }

    @Test
    fun `When the searchCities is invoke then return a list of CityDto `() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(mockCityResponseJson)
        )

        val dtos = geoApi.searchCities("Buenos Aires")

        assertEquals(2, dtos.size)
        val cityDto = dtos.first()
        assertEquals("Buenos Aires", cityDto.name)
        assertEquals("AR", cityDto.country)
        assertEquals(-34.6037, cityDto.latitude, 0.0001)
        assertEquals(-58.3816, cityDto.longitude, 0.0001)
    }
}
