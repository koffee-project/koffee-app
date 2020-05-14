package eu.yeger.koffee.network

import retrofit2.http.*

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

    // TODO improve return
    @POST("users/{userId}/purchases")
    suspend fun purchaseItem(@Path("userId") userId: String, @Body purchaseRequest: ApiPurchaseRequest): ApiResponse<Any>

    // TODO improve return
    @POST("users/{userId}/purchases/refund")
    suspend fun refundPurchase(@Path("userId") userId: String): ApiResponse<Any>

    @POST("login")
    suspend fun login(@Body credentials: ApiCredentials): ApiResponse<String>

    @POST("users")
    suspend fun createUser(@Body userDTO: ApiUserDTO, @Header("Authorization") token: String): ApiResponse<String>

    @PUT("users")
    suspend fun updateUser(@Body userDTO: ApiUserDTO, @Header("Authorization") token: String)

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String, @Header("Authorization") token: String)

    @POST("items")
    suspend fun createItem(@Body itemDTO: ApiItemDTO, @Header("Authorization") token: String): ApiResponse<String>

    @PUT("items")
    suspend fun updateItem(@Body itemDTO: ApiItemDTO, @Header("Authorization") token: String): ApiResponse<String>

    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: String, @Header("Authorization") token: String): ApiResponse<String>
}
