package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.network.requests.FcmRequest
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class CardViewModel(private val repository: CardRepository) : LazyViewModel<List<Card>>() {
    private val _activated = MutableLiveData<Status<Int>>()
    val activated: LiveData<Status<Int>>
        get() = _activated

    private val _deleted = MutableLiveData<Status<Int>>()
    val deleted: LiveData<Status<Int>>
        get() = _deleted

    public override fun loadData() {
        viewModelScope.launch {
            _status.value = repository.getCardList()?.let { Status.Complete(it) } ?: Status.Loading
            _status.value = repository.getFcm()?.fcmId?.let {
                launch {
                    val cardId = repository.getLoggedProfile()?.cardId
                    if (cardId != null && cardId > 0) _activated.value = Status.Complete(cardId)
                }
                repository.getCardList(it)
            } ?: Status.Failure(FCM_PROBLEM)
        }
    }


    fun delete(card: Card, position: Int) {
        viewModelScope.launch {
            _deleted.value = Status.Loading
            _deleted.value = repository.getFcm()?.fcmId?.let {
                val request = FcmRequest(it)
                val result = repository.deleteCard(card.id, request)
                if (result is Status.Failure) result else {
                    Status.Complete(position)
                }
            } ?: Status.Failure(FCM_PROBLEM)
        }
    }

    fun activate(cardId: Int) {
        viewModelScope.launch {
            _activated.value = Status.Loading
            _activated.value = repository.getFcm()?.fcmId?.let {
                val fcmRequest = FcmRequest(it)
                val result = repository.activateCard(cardId, fcmRequest)
                if (result is Status.Failure) result else Status.Complete(cardId)
            } ?: Status.Failure(FCM_PROBLEM)
        }
    }

    fun reload() {
        loadData()
    }
}