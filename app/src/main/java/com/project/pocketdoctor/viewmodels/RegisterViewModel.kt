package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Card
import com.project.pocketdoctor.model.tables.Profile
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: CardRepository) : LazyViewModel<Boolean>() {
    public override fun loadData() {
        viewModelScope.launch {
            if (repository.getCardsCount() > 0) _status.value = Status.Idle
        }
    }

    fun saveCard(card: Card) {
        viewModelScope.launch {
            _status.value = repository.getFcm()?.fcmId?.let {
                val created = repository.createCard(it, card)
                if (created is Status.Failure) created else Status.Complete(_status.value is Status.Complete)
            } ?: Status.Failure(FCM_PROBLEM)
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch {
            _status.value = repository.getFcm()?.fcmId?.let {
                val updated = repository.updateProfile(it, profile)
                if (updated is Status.Failure) updated else Status.Complete(_status.value is Status.Complete)
            }
        }
    }
}