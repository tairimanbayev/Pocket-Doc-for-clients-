package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.VisitPage
import com.project.pocketdoctor.repositories.VisitRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class VisitListViewModel(private val repository: VisitRepository, private val isHistory: Boolean) :
    LazyViewModel<VisitPage>() {

    private var pageNumber = 0

    override fun loadData() {
        _status.value = Status.Loading
        viewModelScope.launch {
            _status.value = repository.getFcm()?.fcmId?.let {
                repository.getVisits(it, pageNumber++, isHistory, 4)
            } ?: Status.Failure(FCM_PROBLEM)
        }
    }

    fun load(isRefresh: Boolean = false) {
        if (isRefresh)
            pageNumber = 0
        loadData()
    }
}