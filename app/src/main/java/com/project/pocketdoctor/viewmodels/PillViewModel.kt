package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Illness
import com.project.pocketdoctor.repositories.PillRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class PillViewModel(private val repository: PillRepository) : LazyViewModel<List<Illness>>() {
    override fun loadData() {
        _status.value = Status.Loading
        viewModelScope.launch {
            _status.value = repository.getFcm()?.fcmId?.let {
                repository.getIllnessList(it)
            } ?: Status.Failure(FCM_PROBLEM)
        }
    }

    fun reload() {
        loadData()
    }
}