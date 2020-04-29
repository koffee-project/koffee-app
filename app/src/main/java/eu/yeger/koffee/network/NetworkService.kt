package eu.yeger.koffee.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import eu.yeger.koffee.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

interface KoffeeApi {

    @GET("users")
    suspend fun getUsers(): ApiResponse<List<ApiUserEntry>>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): ApiResponse<ApiUserProfile?>

    @GET("users/{id}/transactions")
    suspend fun getTransactionForUser(@Path("id") id: String): ApiResponse<List<ApiTransaction>?>

    @GET("items")
    suspend fun getItems(): ApiResponse<List<ApiItem>>

    @GET("items/{id}")
    suspend fun getItemById(@Path("id") id: String): ApiResponse<ApiItem?>
}

object NetworkService {

    private val converterFactory = MoshiConverterFactory.create(moshi)

    val koffeeApi: KoffeeApi = Retrofit.Builder()
        .baseUrl(BuildConfig.KOFFEE_BACKEND_URL)
        .addConverterFactory(converterFactory)
        .build()
        .create(KoffeeApi::class.java)
}
