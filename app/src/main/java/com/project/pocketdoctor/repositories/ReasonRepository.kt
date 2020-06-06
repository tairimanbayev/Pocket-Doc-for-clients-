package com.project.pocketdoctor.repositories

import android.content.Context
import com.project.pocketdoctor.model.tables.Reason
import com.project.pocketdoctor.network.requests.ReasonListRequest
import com.project.pocketdoctor.util.apiCallResponse
import com.project.pocketdoctor.util.dbCall

class ReasonRepository(context: Context) : FcmRepository(context) {
    private val reasonDao = database.reasonDao

    suspend fun getFromApi(request: ReasonListRequest) =
        apiCallResponse({ remoteApi.getReasons(request) }, { insert(it) })

    suspend fun getFromDb(role: String) = dbCall { reasonDao.getReasons(role) }

    private suspend fun insert(reasons: List<Reason>) = dbCall { reasonDao.insert(reasons) }
}