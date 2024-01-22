package com.umanji.umanjiapp.data.network

import com.umanji.umanjiapp.data.entity.*
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * Rest api retrieving data from the network.
 */
interface RestApi {

    @FormUrlEncoded
    @POST("auth/signin")
    fun signIn(
            @Field("email") email: String,
            @Field("password") password: String
    ): Single<ApiResponse<AuthInfoEntity>>

    @FormUrlEncoded
    @POST("auth/signup")
    fun signUp(
            @Field("email") email: String,
            @Field("password") password: String,
            @Field("phone") phone: String,
            @Field("user_name") userName: String
    ): Single<ApiResponse<AuthInfoEntity>>

    @FormUrlEncoded
    @POST("token/check")
    fun checkToken(
            @Field("access_token") accessToken: String
    ): Single<ApiResponse<AuthInfoEntity>>

    @DELETE("auth/logout")
    fun logout(): Completable

    @GET("main/posts")
    fun posts(
            @Query("page") page: Int? = null,
            @Query("portalName") portalName: String? = null,
            @Query("portalType") portalType: String? = null,
            @Query("limit") limit: Int? = null
    ): Single<ApiResponse<List<PostEntity>>>

    @GET("geo/findMarkers")
    fun posts(
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double,
            @Query("zoom") zoom: Int,
            @Query("limit") limit: Int? = null
    ): Single<ApiResponse<List<PostEntity>>>

    @GET("pannel/getPlacePost")
    fun posts(
            @Query("class") type: String,
            @Query("rid") id: String,
            @Query("limit") limit: Int? = null
    ): Single<ApiResponse<List<PostEntity>>>

    @GET("pannel/getProfilePost")
    fun posts(
            @Query("rid") userId: String
    ): Single<ApiResponse<List<PostEntity>>>

    @GET("getCreateGeoType")
    fun getGeoType(
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double,
            @Query("countryCode") countryCode: String = "KR"
    ): Single<ApiResponse<PostEntity>>

    @Multipart
    @POST("geo/createPost")
    fun createPost(
            @PartMap post: MutableMap<String, RequestBody>,
            @Part image: MultipartBody.Part? = null
    ): Completable

    @GET("geo/getPortalInfo")
    fun portal(
            @Query("latitude") latitude: Double,
            @Query("longitude") longitude: Double,
            @Query("user_id") userId: String? = null
    ): Single<ApiResponse<PortalEntity>>

    @GET("pannel/getPannelData?class=portal")
    fun portal(
            @Query("rid") id: String
    ): Single<ApiResponse<PortalEntity>>

    @FormUrlEncoded
    @POST("group/create")
    fun createGroup(
            @PartMap group: MutableMap<String, RequestBody>
    ): Completable

    @GET("bank/userData")
    fun getUser(
            @Query("phoneNumber") phoneNumber: String
    ): Single<ApiResponse<UserEntity>>

    @FormUrlEncoded
    @POST("bank/trade")
    fun sendMoney(
            @Field("send_user_id") sender: String,
            @Field("receive_user_id") receiver: String,
            @Field("amount") amount: Double,
            @Field("description") description: String? = null
    ): Completable

    @GET("bank/ledger")
    fun getLedger(
            @Query("user_id") userId: String
    ): Single<ApiResponse<List<BankTransactionEntity>>>
}
