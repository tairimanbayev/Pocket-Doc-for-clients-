package com.project.pocketdoctor.repositories

import android.content.Context
import com.project.pocketdoctor.model.AppDatabase
import com.project.pocketdoctor.model.daos.FcmDao
import com.project.pocketdoctor.network.ApiFactory
import com.project.pocketdoctor.util.dbCall

open class FcmRepository(context: Context) {
    protected val remoteApi = ApiFactory.apiService
    protected val database: AppDatabase = AppDatabase.getInstance(context)
    private val fcmDao: FcmDao = database.fcmDao

    suspend fun getFcm() = dbCall { fcmDao.getFcm() }
}