package com.project.pocketdoctor.repositories

import android.content.Context
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.model.tables.Profile
import com.project.pocketdoctor.network.requests.LoginRequest
import com.project.pocketdoctor.util.apiCall
import com.project.pocketdoctor.util.apiCallResponse
import com.project.pocketdoctor.util.dbCall
import okhttp3.MultipartBody

class ProfileRepository(context: Context) : FcmRepository(context) {
    private val profileDao = database.profileDao
    private val doctorDao = database.doctorDao
    private val cardDao = database.cardDao
    private val preferences = context.getSharedPreferences("PREF", 0)

    suspend fun login(request: LoginRequest) = apiCallResponse({ remoteApi.login(request) }, { saveProfile(it) })

    suspend fun getProfileFromApi(fcmId: String) =
        apiCallResponse({ remoteApi.getProfile(fcmId) }, { profile ->
            profile.card?.fcmId = fcmId
            profile.doctor?.let { profile.doctorId = it.id }
            saveProfile(profile)
        })

    suspend fun createCard(fcmId: String, card: Card) = apiCallResponse({ remoteApi.createCard(fcmId, card) })

    suspend fun editCard(cardId: Int, card: Card) = apiCallResponse({ remoteApi.editCard(cardId, card) })

    suspend fun uploadPhoto(cardId: Int, photo: MultipartBody.Part?, fcm: MultipartBody.Part) =
        apiCall({ remoteApi.uploadImage(cardId, photo, fcm) })

    fun getLoggedProfileId() = preferences.getInt("LOGGED_USER_ID", -1)

    suspend fun getLoggedProfile() = dbCall { profileDao.getProfile(getLoggedProfileId()) }

    private suspend fun saveProfile(profile: Profile) = dbCall {
        preferences.edit().putInt("LOGGED_USER_ID", profile.id).apply()
        profileDao.insert(profile)
        profile.card?.let { cardDao.insert(it) }
        profile.doctor?.let {
            profile.doctorId = it.id
            doctorDao.insert(it)
        }
    }

    suspend fun getDoctor(id: Int) = dbCall { doctorDao.getDoctor(id) }

    suspend fun getCard(id: Int) = dbCall {
        val card = cardDao.getCard(id)
        card?.let { it.profile = profileDao.getProfile(it.userId) }
        card
    }

    suspend fun saveCard(card: Card) = dbCall { cardDao.insert(card) }

    suspend fun clearData() = dbCall {
        database.clearAllTables()
        preferences.edit().remove("LOGGED_USER_ID").apply()
    }
}