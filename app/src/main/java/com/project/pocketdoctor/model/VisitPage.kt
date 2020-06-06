package com.project.pocketdoctor.model

import com.project.pocketdoctor.model.tables.Visit
import com.squareup.moshi.Json

data class VisitPage(
    @field:Json(name = "data")
    var visits: List<Visit> = emptyList(),
    @field:Json(name = "last_page")
    var lastPage: Boolean? = null
)