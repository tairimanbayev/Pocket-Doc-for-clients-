package com.project.pocketdoctor.network.requests

import com.squareup.moshi.Json

data class ReasonListRequest(@field:Json(name = "fcm_id") val fcmId: String, val role: String)