package com.project.pocketdoctor.repositories

import android.content.Context
import com.project.pocketdoctor.util.apiCallResponse
import com.project.pocketdoctor.util.dbCall

class IllnessRepository(context: Context) : FcmRepository(context) {
    private val cardDao = database.cardDao

    suspend fun getIllnessListByCardId(cardId: Int, fcmId: String) =
        apiCallResponse({ remoteApi.getIllnessListByCardId(cardId, fcmId) })

    suspend fun getIllnessListByVisitId(visitId: Int, fcmId: String) =
        apiCallResponse({ remoteApi.getIllnessListByVisitId(visitId, fcmId) })

}