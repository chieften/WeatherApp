package com.example.weatherapp.di

import com.example.weatherapp.data.remote.ApiNinjasApi
import com.example.weatherapp.data.remote.OpenMeteoApi
import com.example.weatherapp.data.repository.WeatherRepository
import com.example.weatherapp.domain.WeatherUseCase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object NetworkModule {
    private const val OPEN_METEO_BASE_URL = "https://api.open-meteo.com/"
    private const val API_NINJAS_BASE_URL = "https://api.api-ninjas.com/"
    private const val API_NINJAS_KEY = "miBMMuGykyOKTS93kungVuQ82xBnR2N7Yjyr42Fe"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    val sslContext = SSLContext.getInstance("TLS").apply {
        init(null, trustAllCerts, java.security.SecureRandom())
    }

    private val okHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val openMeteoRetrofit = Retrofit.Builder()
        .baseUrl(OPEN_METEO_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val apiNinjasRetrofit = Retrofit.Builder()
        .baseUrl(API_NINJAS_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val openMeteoApi: OpenMeteoApi by lazy {
        openMeteoRetrofit.create(OpenMeteoApi::class.java)
    }

    val apiNinjasApi: ApiNinjasApi by lazy {
        apiNinjasRetrofit.create(ApiNinjasApi::class.java)
    }

    val repository: WeatherRepository by lazy {
        WeatherRepository(openMeteoApi, apiNinjasApi, API_NINJAS_KEY)
    }

    val useCase: WeatherUseCase by lazy {
        WeatherUseCase(repository)
    }
}