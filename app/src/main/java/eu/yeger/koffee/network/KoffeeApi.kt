package eu.yeger.koffee.network

import retrofit2.http.*

interface KoffeeApi {

    @GET("users")
    suspend fun getUsers(): List<ApiUserEntry>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): ApiUserProfile

    @GET("users/{id}/transactions")
    suspend fun getTransactionForUser(@Path("id") id: String): List<ApiTransaction>

    @GET("items")
    suspend fun getItems(): List<ApiItem>

    @GET("items/{id}")
    suspend fun getItemById(@Path("id") id: String): ApiItem?

    @POST("users/{userId}/purchases")
    suspend fun purchaseItem(@Path("userId") userId: String, @Body purchaseRequest: ApiPurchaseRequest)

    @POST("users/{userId}/purchases/refund")
    suspend fun refundPurchase(@Path("userId") userId: String)

    @POST("login")
    suspend fun login(@Body credentials: ApiCredentials): String

    @POST("users")
    suspend fun createUser(@Body userDTO: ApiUserDTO, @Header("Authorization") token: String)

    @PUT("users")
    suspend fun updateUser(@Body userDTO: ApiUserDTO, @Header("Authorization") token: String)

    @POST("users/{id}/funding")
    suspend fun creditUser(@Path("id") id: String, @Body fundingRequest: ApiFundingRequest, @Header("Authorization") token: String)

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String, @Header("Authorization") token: String)

    @POST("items")
    suspend fun createItem(@Body itemDTO: ApiItemDTO, @Header("Authorization") token: String)

    @PUT("items")
    suspend fun updateItem(@Body itemDTO: ApiItemDTO, @Header("Authorization") token: String)

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: String, @Header("Authorization") token: String)
}
