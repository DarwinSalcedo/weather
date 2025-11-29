package com.custom.data.di

import android.content.Context
import android.content.SharedPreferences
import com.custom.data.BuildConfig
import com.custom.data.remote.AuthInterceptor
import com.custom.data.remote.GeoApi
import com.custom.data.remote.WeatherApi
import com.custom.data.repository.CitySearchRepositoryImpl
import com.custom.data.repository.ErrorTranslatorImpl
import com.custom.data.repository.LanguageRepositoryImpl
import com.custom.data.repository.LocationRepositoryImpl
import com.custom.data.repository.OnboardingRepositoryImpl
import com.custom.data.repository.WeatherRepositoryImpl
import com.custom.di.IoDispatcher
import com.custom.domain.repository.CitySearchRepository
import com.custom.domain.repository.ErrorTranslator
import com.custom.domain.repository.LanguageRepository
import com.custom.domain.repository.LocationRepository
import com.custom.domain.repository.OnboardingRepository
import com.custom.domain.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    private const val PREFS_NAME = "app_prefs"

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(
        apiService: WeatherApi
    ): WeatherRepository {
        return WeatherRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGeoApi(retrofit: Retrofit): GeoApi {
        return retrofit.create(GeoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCitySearchRepository(
        apiService: GeoApi
    ): CitySearchRepository {
        return CitySearchRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        @ApplicationContext context: Context,
        fusedLocationProviderClient: FusedLocationProviderClient
    ): LocationRepository {
        return LocationRepositoryImpl(context, fusedLocationProviderClient)
    }

    @Provides
    @Singleton
    fun provideErrorTranslator(): ErrorTranslator = ErrorTranslatorImpl()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideOnboardingRepository(
        sharedPreferences: SharedPreferences,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): OnboardingRepository = OnboardingRepositoryImpl(sharedPreferences, dispatcher)


    @Provides
    @Singleton
    fun provideLanguageRepository(
        @ApplicationContext context: Context,
        sharedPreferences: SharedPreferences,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): LanguageRepository = LanguageRepositoryImpl(context, sharedPreferences, dispatcher)

}