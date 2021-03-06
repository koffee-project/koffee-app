package eu.yeger.koffee.network

import okhttp3.MultipartBody
import retrofit2.http.*

/**
 * The Koffee API interface.
 *
 * @author Jan Müller
 */
interface KoffeeApi {

    /**
     * Requests metadata of all users.
     *
     * @return The [List] of [ApiUserEntry]s.
     */
    @GET("users")
    suspend fun getUsers(): List<ApiUserEntry>

    /**
     * Requests profile data of the user with the given id.
     *
     * @param id The id of the user.
     * @return The [ApiUserProfile].
     */
    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): ApiUserProfile

    /**
     * Requests all transactions of a user.
     *
     * @param id The id of the user.
     * @return The [List] of this users [ApiTransaction]s.
     */
    @GET("users/{id}/transactions")
    suspend fun getTransactionForUser(@Path("id") id: String): List<ApiTransaction>

    /**
     * Requests all items.
     *
     * @return The [List] of [ApiItem]s.
     */
    @GET("items")
    suspend fun getItems(): List<ApiItem>

    /**
     * Requests a single item with the given id.
     *
     * @param id The id of the item.
     * @return The [ApiItem].
     */
    @GET("items/{id}")
    suspend fun getItemById(@Path("id") id: String): ApiItem

    /**
     * Requests a purchase for the given user.
     *
     * @param userId The id of the user making the purchase.
     * @param purchaseRequest The [ApiPurchaseRequest] containing the transaction information.
     */
    @POST("users/{userId}/purchases")
    suspend fun purchaseItem(@Path("userId") userId: String, @Body purchaseRequest: ApiPurchaseRequest)

    /**
     * Requests a refund for the given user.
     *
     * @param userId The id of the user making the purchase.
     */
    @POST("users/{userId}/purchases/refund")
    suspend fun refundPurchase(@Path("userId") userId: String)

    /**
     * Requests a login using the provided [ApiCredentials].
     *
     * @param credentials The [ApiCredentials] used for the login.
     * @return The [ApiToken].
     */
    @POST("login")
    suspend fun login(@Body credentials: ApiCredentials): ApiToken

    /**
     * Requests the creation of a user using the provided [ApiUserDTO].
     *
     * @param userDTO The data used for user creation.
     * @param token The authorization token.
     * @return The id of the user. May be generated by the server.
     */
    @POST("users")
    suspend fun createUser(@Body userDTO: ApiUserDTO, @Header("Authorization") token: String): String

    /**
     * Request for updating a user using the provided [ApiUserDTO].
     *
     * @param userDTO The data used for the update.
     * @param token The authorization token.
     */
    @PUT("users")
    suspend fun updateUser(@Body userDTO: ApiUserDTO, @Header("Authorization") token: String)

    /**
     * Requests the crediting of the user with the given id.
     *
     * @param id The id of the user to be credited.
     * @param fundingRequest The data used for the crediting.
     * @param token The authorization token.
     */
    @POST("users/{id}/funding")
    suspend fun creditUser(@Path("id") id: String, @Body fundingRequest: ApiFundingRequest, @Header("Authorization") token: String)

    /**
     * Requests the deletion of the user with the given id.
     *
     * @param id The id of the user to be deleted.
     * @param token The authorization token.
     */
    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String, @Header("Authorization") token: String)

    /**
     * Requests the creation of an item using the provided [ApiItemDTO].
     *
     * @param itemDTO The data used for item creation.
     * @param token The authorization token.
     * @return The id of the item. May be generated by the server.
     */
    @POST("items")
    suspend fun createItem(@Body itemDTO: ApiItemDTO, @Header("Authorization") token: String): String

    /**
     * Request for updating an item using the provided [ApiItemDTO].
     *
     * @param itemDTO The data used for the update.
     * @param token The authorization token.
     */
    @PUT("items")
    suspend fun updateItem(@Body itemDTO: ApiItemDTO, @Header("Authorization") token: String)

    /**
     * Requests the deletion of the item with the given id.
     *
     * @param id The id of the item to be deleted.
     * @param token The authorization token.
     */
    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: String, @Header("Authorization") token: String)

    /**
     * Requests the profile image of the user with the given id.
     *
     * @param id The id of the user.
     * @return The [ApiProfileImage] belonging to this user.
     */
    @GET("users/{id}/image")
    suspend fun getProfileImage(@Path("id") id: String): ApiProfileImage

    /**
     * Requests the profile image timestamp of the user with the given id.
     *
     * @param id The id of the user.
     * @return The timestamp of the profile image belonging to this user.
     */
    @GET("users/{id}/image/timestamp")
    suspend fun getProfileImageTimestamp(@Path("id") id: String): Long

    /**
     * Uploads an image to the server to be used as the profile image for the user with he given id.
     *
     * @param id The id of the user.
     * @param image The image.
     */
    @Multipart
    @POST("users/{id}/image")
    suspend fun uploadProfileImage(@Path("id") id: String, @Part image: MultipartBody.Part)

    /**
     * Requests the deletion of the profile image whose user has the given id.
     *
     * @param id The id of the user whose profile image will be deleted.
     */
    @DELETE("users/{id}/image")
    suspend fun deleteProfilePicture(@Path("id") id: String)
}
