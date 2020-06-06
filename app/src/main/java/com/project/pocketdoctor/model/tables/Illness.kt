package com.project.pocketdoctor.model.tables

import com.squareup.moshi.Json

data class Illness(
    var id: Int = 0,
    @field:Json(name = "card_id")
    var cardId: Int = 0,
    @field:Json(name = "title")
    var diagnosis: String? = null,
    @field:Json(name = "description")
    var inspection: String? = null,
    var complaint: String? = null,
    var appointment: String? = null,
    var result: String? = null,
    @field:Json(name = "fcm_id")
    var fcmId: String? = null,
    @field:Json(name = "medicines")
    var pills: List<Pill>? = null,
    var card: Card? = null,
    var visit: Visit? = null
)