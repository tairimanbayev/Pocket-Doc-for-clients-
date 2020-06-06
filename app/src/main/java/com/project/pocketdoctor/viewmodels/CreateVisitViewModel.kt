package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.network.requests.CreateVisitRequest
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class CreateVisitViewModel(val repository: CardRepository) : ViewModel() {
    private val _cardList = MutableLiveData<Status<List<Card>>>()
    val cardList: LiveData<Status<List<Card>>>
        get() {
            loadCards()
            return _cardList
        }

    private fun loadCards() = viewModelScope.launch {
        val cardsFromDb = repository.getCardList()
        _cardList.value = if (cardsFromDb != null) Status.Complete(cardsFromDb) else Status.Loading
        val fcmId = repository.getFcm()?.fcmId
        _cardList.value = if (fcmId == null) Status.Failure(FCM_PROBLEM) else repository.getCardList(fcmId)
    }

    private val _visitCreated = MutableLiveData<Status<Visit>>()
    val visit: LiveData<Status<Visit>>
        get() = _visitCreated

    fun createVisit(visit: Visit, reasons: List<Int>) = viewModelScope.launch {
        val fcmId = repository.getFcm()?.fcmId
        _visitCreated.value = if (fcmId == null) Status.Failure(FCM_PROBLEM) else {
            val request = CreateVisitRequest.build(visit, reasons)
            repository.createVisit(request, fcmId)
        }
    }
}