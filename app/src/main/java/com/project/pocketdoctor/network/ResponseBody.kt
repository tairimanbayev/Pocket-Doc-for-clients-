package com.project.pocketdoctor.network

import com.squareup.moshi.Json

data class ResponseBody<T>(
    @field:Json(name = "body")
    var body: T
)