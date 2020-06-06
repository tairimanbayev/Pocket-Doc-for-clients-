package com.project.pocketdoctor.model.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.project.pocketdoctor.model.tables.Card

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cards: List<Card>)

    @Query("SELECT * FROM card WHERE id = :id")
    suspend fun getCard(id: Int): Card?

    @Query("SELECT * FROM card WHERE user_id = :userId")
    suspend fun getCards(userId: Int): List<Card>?

    @Query("SELECT COUNT(*) FROM card WHERE user_id = :id")
    suspend fun getCardsCount(id: Int): Int

    @Query("SELECT * FROM card WHERE id in (SELECT cardId FROM VisitCard WHERE visitId=:visitId)")
    suspend fun getCardsOfVisit(visitId: Int): List<Card>?

    @Query("DELETE FROM card WHERE id = :id")
    suspend fun delete(id: Int)
}