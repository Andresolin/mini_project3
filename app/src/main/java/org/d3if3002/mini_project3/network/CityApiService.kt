package org.d3if3002.mini_project3.network


import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/Andresolin/Static-JSON/main/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface CityApiService {
    @GET("static-api.json")
    suspend fun getCity(): String
}

object CityApi {
    val service: CityApiService by lazy {
        retrofit.create(CityApiService::class.java)
    }
}