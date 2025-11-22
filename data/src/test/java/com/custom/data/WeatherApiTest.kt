package com.custom.data

import com.custom.data.remote.AuthInterceptor
import com.custom.data.remote.WeatherApi
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class WeatherApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: WeatherApi
    private lateinit var gson: Gson

    private val mockWeatherResponseJson = """
            {
            "coord": { "lon": -65.78, "lat": -28.46 },
            "weather": [
                { "id": 800, "main": "Clear", "description": "clear sky", "icon": "01d" }
            ],
            "base": "stations",
            "main": {
                "temp": 23.21,
                "feels_like": 22.58,
                "temp_min": 23.21,
                "temp_max": 23.21,
                "pressure": 1017,
                "humidity": 38
            },
            "visibility": 10000,
            "wind": { "speed": 4.92, "deg": 45, "gust": 8.94 },
            "clouds": { "all": 0 },
            "dt": 1763812166,
            "sys": { "country": "AR", "sunrise": 1763803161, "sunset": 1763852381 },
            "timezone": -10800,
            "id": 3837702,
            "name": "San Fernando del Valle de Catamarca",
            "cod": 200
            }
        """.trimIndent()

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        gson = Gson()

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(WeatherApi::class.java)

    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `when a api call is made, then a request is sent to the server with the correct parameters`() =
        runTest {
            val cityName = "Cordoba"

            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(mockWeatherResponseJson)
            )

            apiService.getCurrentWeather(cityName)


            val recordedRequest = mockWebServer.takeRequest()

            assertEquals("/data/2.5/weather", recordedRequest.path?.substringBefore('?'))

            assertTrue(recordedRequest.path!!.contains("q=$cityName"))

            assertTrue(recordedRequest.path!!.contains("units=metric"))

            assertTrue(recordedRequest.path!!.contains("appid="))
        }

    @Test
    fun `Given a success response, when getCurrentWeather is called, then WeatherDto is returned`() =
        runTest {
            mockWebServer.enqueue(
                MockResponse()
                    .setResponseCode(HttpURLConnection.HTTP_OK)
                    .setBody(mockWeatherResponseJson)
            )

            val weatherDto = apiService.getCurrentWeather("Catamarca")

            assertEquals("San Fernando del Valle de Catamarca", weatherDto.cityName)
            assertEquals(23.21, weatherDto.main.temperature, 0.01) // Usamos delta para Doubles
            assertEquals("clear sky", weatherDto.weather.first().description)
            assertEquals(38, weatherDto.main.humidity)
            assertEquals(4.92, weatherDto.wind.speed, 0.01)
        }
}