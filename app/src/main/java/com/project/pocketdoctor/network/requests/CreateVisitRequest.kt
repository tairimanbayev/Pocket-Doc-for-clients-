package com.project.pocketdoctor.network.requests

import com.project.pocketdoctor.model.tables.Visit
import com.squareup.moshi.Json

class CreateVisitRequest(
    @field:Json(name = "address_id")
    var addressId: Int = 106,

    @field:Json(name = "card_id")
    var cards: List<Int>,
    var comment: String,

    @field:Json(name = "question_id")
    var questions: List<Int>,
    var role: String,

    @field:Json(name = "visit_at")
    var visitAt: String
) {
    companion object {
        fun build(visit: Visit, reasons: List<Int>) = CreateVisitRequest(
            cards = visit.cards.map { it.id },
            comment = visit.comment ?: "",
            questions = reasons,
            role = visit.role,
            visitAt = visit.date ?: ""
        )
    }
}