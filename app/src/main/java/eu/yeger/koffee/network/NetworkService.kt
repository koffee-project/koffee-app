package eu.yeger.koffee.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import eu.yeger.koffee.BuildConfig
import eu.yeger.koffee.network.NetworkService.koffeeApi
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Provides the instance of the [KoffeeApi].
 *
 * @property koffeeApi The [KoffeeApi] instance.
 *
 * @author Jan Müller
 */
object NetworkService {
    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val koffeeApi: KoffeeApi = Retrofit.Builder()
        .baseUrl(BuildConfig.KOFFEE_BACKEND_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(KoffeeApi::class.java)
}
