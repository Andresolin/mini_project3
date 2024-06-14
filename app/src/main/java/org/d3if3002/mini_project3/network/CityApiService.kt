package org.d3if3002.mini_project3.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.d3if3002.mini_project3.model.City
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://raw.githubusercontent.com/Andresolin/Static-JSON/main/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CityApiService {
    @GET("static-api.json")
    suspend fun getCity(): List<City>
}

object CityApi {
    val service: CityApiService by lazy {
        retrofit.create(CityApiService::class.java)
    }

    fun getCityUrl(imageId: String): String {
        return "$BASE_URL$imageId.jpg"
    }
}