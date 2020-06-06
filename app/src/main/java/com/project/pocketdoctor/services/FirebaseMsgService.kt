package com.project.pocketdoctor.services

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.project.pocketdoctor.model.AppDatabase
import com.project.pocketdoctor.model.tables.Fcm
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseMsgService : FirebaseMessagingService() {

    private val TAG = "FirebaseServiceLogcat"

    init {
        Log.d(TAG, "init: ")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.e("messageBody", remoteMessage.data.toString())
        Log.e("messageBody", remoteMessage.notification?.body.toString())
        Log.e("messageTitle", remoteMessage.notification?.title.toString())
        Log.e("messageType", remoteMessage.data["type"] ?: "null")
    }

    override fun onNewToken(token: String) {
        saveToken(token)
        super.onNewToken(token)
    }

    private fun saveToken(token: String) = CoroutineScope(Dispatchers.IO).launch {
        val fcmDao = AppDatabase.getInstance(applicationContext).fcmDao
        val fcm = fcmDao.getFcm() ?: Fcm()
        fcm.fcmId = token
        fcmDao.insert(fcm)
    }

    companion object {
        fun getInstance(onSuccess: (String) -> Unit) {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
                Log.d("FirebaseMsgLog", "getInstance: ${it.token}")
                onSuccess(it.token)
            }
        }
    }
}