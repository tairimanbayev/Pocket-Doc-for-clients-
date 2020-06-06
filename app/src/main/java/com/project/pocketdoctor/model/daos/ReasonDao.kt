package com.project.pocketdoctor.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoctor.model.tables.Reason

@Dao
interface ReasonDao {
    @Query("SELECT * FROM reasons WHERE role = :role")
    suspend fun getReasons(role: String): List<Reason>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reason: Reason)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<Reason>)
}