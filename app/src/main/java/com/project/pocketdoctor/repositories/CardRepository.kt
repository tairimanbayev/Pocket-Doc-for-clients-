package com.project.pocketdoctor.repositories

import android.content.Context
import com.project.pocketdoctor.model.tables.*
import com.project.pocketdoctor.network.requests.CreateVisitRequest
import com.project.pocketdoctor.network.requests.FcmRequest
import com.project.pocketdoctor.util.apiCall
import com.project.pocketdoctor.util.apiCallResponse
import com.project.pocketdoctor.util.dbCall

class CardRepository(context: Context) : FcmRepository(context) {

    private val cardDao = database.cardDao
    private val profileDao = database.profileDao
    private val visitDao = database.visitDao
    private val pref = context.getSharedPreferences("PREF", 0)

    suspend fun createCard(fcmId: String, card: Card) = apiCallResponse({ remoteApi.createCard(fcmId, card) }, {
        saveCard(it, fcmId)
    })

    suspend fun deleteCard(cardId: Int, fcmRequest: FcmRequest) = apiCall({ remoteApi.deleteCard(cardId, fcmRequest) }, {
        deleteCardFromDb(cardId)
    })

    suspend fun getCardList(fcmId: String) = apiCallResponse({ remoteApi.getCardList(fcmId) }, { saveCards(it, fcmId) })

    suspend fun createVisit(request: CreateVisitRequest, fcmId: String) =
        apiCallResponse({ remoteApi.createVisit(fcmId, request) }, {
            saveVisit(it)
        })

    suspend fun activateCard(cardId: Int, fcmRequest: FcmRequest) = apiCall({ remoteApi.activateCard(cardId, fcmRequest) })

    suspend fun updateProfile(fcmId: String, profile: Profile) =
        apiCall({ remoteApi.updateProfile("application/x-www-form-urlencoded", fcmId, profile) })

    suspend fun getCardList() = dbCall { cardDao.getCards(getLoggedProfileId()) }

    suspend fun getCardsCount() = dbCall { cardDao.getCardsCount(getLoggedProfileId()) }

    private suspend fun saveCard(card: Card, fcmId: String) = dbCall { cardDao.insert(card.also { it.fcmId = fcmId }) }

    private suspend fun saveCards(cards: List<Card>, fcmId: String) = dbCall {
        cards.forEach { it.fcmId = fcmId }
        cardDao.insert(cards)
    }

    private suspend fun deleteCardFromDb(cardId: Int) = dbCall { cardDao.delete(cardId) }

    private fun getLoggedProfileId() = pref.getInt("LOGGED_USER_ID", -1)

    suspend fun getLoggedProfile() = dbCall { profileDao.getProfile(getLoggedProfileId()) }

    private suspend fun saveVisit(visit: Visit) = dbCall {
        visitDao.insert(visit)
        visit.cards.forEach { visitDao.insert(VisitCard(visitId = visit.id, cardId = it.id)) }
        visitDao.insertReasons(visit.reasons)
        visit.reasons.forEach {
            visitDao.insertVisitReason(VisitReason(visitId = visit.id, reasonId = it.id))
        }
    }
}