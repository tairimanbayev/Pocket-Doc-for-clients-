package com.project.pocketdoctor.repositories

import android.content.Context
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.model.tables.VisitCard
import com.project.pocketdoctor.model.tables.VisitReason
import com.project.pocketdoctor.util.dbCall
import com.project.pocketdoctor.util.apiCallResponse


class VisitRepository(context: Context) : FcmRepository(context) {
    private val visitDao = database.visitDao
    private val cardDao = database.cardDao
    private val doctorDao = database.doctorDao
    private val profileDao = database.profileDao

    suspend fun getVisits(fcmId: String, pageNumber: Int, isHistory: Boolean, limit: Int) =
        apiCallResponse({ remoteApi.getVisitPage(fcmId, limit, pageNumber * limit, if (isHistory) 1 else 0) }, {
            insert(it.visits, fcmId)
        })

    suspend fun getVisitWithCards(visitId: Int) = dbCall {
        visitDao.getVisit(visitId)?.also { visit ->
            visit.cards = cardDao.getCardsOfVisit(visitId) ?: emptyList()
            doctorDao.getDoctor(visit.doctorId ?: 0)?.let {
                it.profile = profileDao.getProfile(it.userId)?.apply {
                    if (cardId != null)
                        card = cardDao.getCard(cardId!!)
                }
                visit.doctor = it
            }
            visit.reasons = visitDao.getReasons(visitId) ?: emptyList()
        } ?: Visit()
    }

    suspend fun insert(visit: Visit, fcmId: String) = dbCall {
        visitDao.insert(visit)
        insertVisitExtra(visit, fcmId)
    }

    suspend fun insert(visits: List<Visit>, fcmId: String) = dbCall {
        visitDao.insert(visits)
        for (visit in visits) {
            insertVisitExtra(visit, fcmId)
        }
    }

    private suspend fun insertVisitExtra(visit: Visit, fcmId: String) {
        for (card in visit.cards) {
            card.fcmId = fcmId
            visitDao.insert(VisitCard(visitId = visit.id, cardId = card.id))
        }
        cardDao.insert(visit.cards)
        val doctor = visit.doctor
        if (doctor != null) {
            doctorDao.insert(doctor)
            doctor.clinic?.let { doctorDao.addClinic(it) }
            doctor.profile?.let {
                doctor.userId = it.id
                profileDao.insert(it)
            }
        }
        visitDao.insertReasons(visit.reasons)
        visitDao.insertVisitReasons(visit.reasons.map { VisitReason(visitId = visit.id, reasonId = it.id) })
    }
}