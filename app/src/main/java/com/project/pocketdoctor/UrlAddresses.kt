package com.project.pocketdoctor

const val BASE_URL = "http://31.131.18.129/api/"
const val LOGIN_URL = "auth/login"
const val PROFILE_URL = "cabinet/profile"
const val ACTIVATE_CARD = "cabinet/set_card/{id}"
const val VISIT_URL = "visit"
const val REASONS_URL = "$VISIT_URL/questions"
const val CARD_URL = "card"
const val CARD_UPDATE_URL = "$CARD_URL/{id}"
const val UPLOAD_IMAGE_URL = "$CARD_UPDATE_URL/photo"
const val ILLNESS_URL = "illnesses"
const val PILL_LIST_URL = "medicine"
const val PILL_ID_URL = "$PILL_LIST_URL/{id}"

fun imageDownloadUrl(cardId: Int, fcmId: String) = "$BASE_URL/$CARD_URL/$cardId/photo?fcm_id=$fcmId"