package org.d3if3002.mini_project3.network


import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.d3if3002.mini_project3.model.City
import org.d3if3002.mini_project3.model.OpStatus
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://unspoken.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface CityApiService {
    @GET("api_andre.php")
    suspend fun getCity(
        @Header("Authorization") userId: String
    ): List<City>

    @Multipart
    @POST("api_andre.php")
    suspend fun postCity(
        @Header("Authorization") userId: String,
        @Part("city") city: RequestBody,
        @Part("country") country: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus


    @DELETE("api_andre.php")
    suspend fun deleteCity(
        @Header("Authorization") userId: String,
        @Query("id") id: String
    ): OpStatus

}

object CityApi {
    val service: CityApiService by lazy {
        retrofit.create(CityApiService::class.java)
    }

    fun getCityUrl(imageId: String): String {
        return "${BASE_URL}image.php?id=$imageId"
    }
}

enum class ApiStatus {LOADING, SUCCESS, FAILED}