package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.models.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val interceptor = Interceptor { chain ->
    val url = chain.request().url().newBuilder().addQueryParameter("api_key", API_KEY).build()
    val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
    chain.proceed(request)
}

private val client = OkHttpClient().newBuilder().addInterceptor(interceptor).build()

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

private val retrofitMoshi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

interface AsteroidsApiService {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroidList(
            @Query("start_date") startDate: String,
            @Query("end_date") endDate: String
    ) : String

    @GET("planetary/apod")
    suspend fun getPictureOfTheDay() : PictureOfDay

}

object NasaApi {
    val retrofitService : AsteroidsApiService by lazy { retrofit.create(AsteroidsApiService::class.java) }

    val retrofitMoshiService : AsteroidsApiService by lazy { retrofitMoshi.create(AsteroidsApiService::class.java) }
}