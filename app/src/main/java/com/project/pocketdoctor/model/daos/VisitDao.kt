package com.project.pocketdoctor.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoctor.model.tables.Reason
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.model.tables.VisitCard
import com.project.pocketdoctor.model.tables.VisitReason

@Dao
interface VisitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visit: Visit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(visits: List<Visit>)

    @Query("DELETE FROM visit")
    suspend fun clearTable()

    @Query("SELECT * FROM visit WHERE id=:visitId")
    suspend fun getVisit(visitId: Int): Visit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vc: VisitCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReasons(reasons: List<Reason>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitReason(vr: VisitReason)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitReasons(vr: List<VisitReason>)

    @Query("SELECT * FROM reasons WHERE id IN (SELECT reasonId FROM visitreason WHERE visitId = :visitId)")
    suspend fun getReasons(visitId: Int): List<Reason>?
}