package com.project.pocketdoctor.network

import com.project.pocketdoctor.*
import com.project.pocketdoctor.model.VisitPage
import com.project.pocketdoctor.model.tables.*
import com.project.pocketdoctor.network.requests.CreateVisitRequest
import com.project.pocketdoctor.network.requests.FcmRequest
import com.project.pocketdoctor.network.requests.LoginRequest
import com.project.pocketdoctor.network.requests.ReasonListRequest
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @POST(LOGIN_URL)
    suspend fun login(@Body request: LoginRequest): ResponseBody<Profile>

    @GET(PROFILE_URL)
    suspend fun getProfile(@Query("fcm_id") fcmId: String): ResponseBody<Profile>

    @PUT(PROFILE_URL)
    suspend fun updateProfile(@Header("Content-Type") header: String, @Query("fcm_id") fcmId: String, @Body profile: Profile)

    @POST(CARD_URL)
    suspend fun createCard(@Query("fcm_id") fcmId: String, @Body card: Card): ResponseBody<Card>

    @POST(ACTIVATE_CARD)
    suspend fun activateCard(@Path("id") id: Int, @Body fcmRequest: FcmRequest)

    @PUT(CARD_UPDATE_URL)
    suspend fun editCard(@Path("id") cardId: Int, @Body card: Card): ResponseBody<Card>

    @GET(CARD_URL)
    suspend fun getCardList(@Query("fcm_id") fcmId: String): ResponseBody<List<Card>>

    @HTTP(hasBody = true, method = "DELETE", path = CARD_UPDATE_URL)
    suspend fun deleteCard(@Path("id") id: Int, @Body fcmRequest: FcmRequest)

    @GET(ILLNESS_URL)
    suspend fun getIllnessListByCardId(
        @Query("card_id") cardId: Int,
        @Query("fcm_id") fcmId: String
    ): ResponseBody<List<Illness>>

    @GET(ILLNESS_URL)
    suspend fun getIllnessListByVisitId(
        @Query("visit_id") visitId: Int,
        @Query("fcm_id") fcmId: String
    ): ResponseBody<List<Illness>>

    @Multipart
    @POST(UPLOAD_IMAGE_URL)
    suspend fun uploadImage(@Path("id") cardId: Int, @Part photo: MultipartBody.Part?, @Part fcm: MultipartBody.Part)

    @POST(VISIT_URL)
    suspend fun createVisit(@Query("fcm_id") fcmId: String, @Body request: CreateVisitRequest): ResponseBody<Visit>

    @GET(VISIT_URL)
    suspend fun getVisitPage(
        @Query("fcm_id") fcmId: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("history") history: Int
    ): ResponseBody<VisitPage>

    @POST(REASONS_URL)
    suspend fun getReasons(@Body request: ReasonListRequest): ResponseBody<List<Reason>>

    @GET(ILLNESS_URL)
    suspend fun getIllnessList(@Query("fcm_id") fcmId: String): ResponseBody<List<Illness>>

}