package eu.yeger.koffee.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import eu.yeger.koffee.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val moshi: Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object NetworkService {

    private val converterFactory = MoshiConverterFactory.create(moshi)

    val koffeeApi: KoffeeApi = Retrofit.Builder()
        .baseUrl(BuildConfig.KOFFEE_BACKEND_URL)
        .addConverterFactory(converterFactory)
        .build()
        .create(KoffeeApi::class.java)
}
