package com.project.pocketdoctor.repositories

import android.content.Context
import com.project.pocketdoctor.util.apiCallResponse

class PillRepository(context: Context) : FcmRepository(context) {
    suspend fun getIllnessList(fcmId: String) = apiCallResponse({ remoteApi.getIllnessList(fcmId) })
}