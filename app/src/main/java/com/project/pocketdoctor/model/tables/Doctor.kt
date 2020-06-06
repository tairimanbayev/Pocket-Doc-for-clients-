package com.project.pocketdoctor.model.tables

import androidx.room.*
import com.squareup.moshi.Json

@Entity
data class Doctor(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "user_id") @field:Json(name = "user_id")
    var userId: Int = 0,
    @Ignore @field:Json(name = "user")
    var profile: Profile? = null,
    @ColumnInfo(name = "clinic_id") @field:Json(name = "clinic_id")
    var clinicId: Int = 0,
    @Ignore @field:Json(name = "clinic")
    var clinic: Clinic? = null,
    var role: String = "",
    var description: String = "",
    var latitude: Double = 0.0,
    var longitude: Double = 0.0
)
