package com.project.pocketdoctor.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoctor.model.tables.Clinic
import com.project.pocketdoctor.model.tables.Doctor

@Dao
interface DoctorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(doctor: Doctor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addClinic(clinic: Clinic)

    @Query("SELECT * FROM doctor WHERE id = :id")
    suspend fun getDoctor(id: Int): Doctor?
}