package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Reason
import com.project.pocketdoctor.network.requests.ReasonListRequest
import com.project.pocketdoctor.repositories.ReasonRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class ReasonsViewModel(private val repository: ReasonRepository, private val role: String) : LazyViewModel<List<Reason>>() {

    override fun loadData() {
        _status.value = Status.Loading
        viewModelScope.launch {
            _status.value = Status.Complete(repository.getFromDb(role))
            val fcmId = repository.getFcm()?.fcmId
            _status.value = if (fcmId == null) Status.Failure(FCM_PROBLEM) else {
                val request = ReasonListRequest(fcmId, role)
                repository.getFromApi(request)
            }
        }
    }
}