package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Profile
import com.project.pocketdoctor.network.requests.LoginRequest
import com.project.pocketdoctor.repositories.ProfileRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: ProfileRepository) : LazyViewModel<Boolean>() {

    override fun loadData() {
        viewModelScope.launch {
            if (repository.getLoggedProfileId() != -1) _status.value = Status.Complete(true)
        }
    }

    fun login(number: String, password: String) = viewModelScope.launch {
        val fcm = repository.getFcm()
        if (fcm == null) {
            _status.value = Status.Failure(FCM_PROBLEM)
            return@launch
        }
        val loginRequest = LoginRequest(number, password, fcm.fcmId)
        val result = repository.login(loginRequest)
        _status.value = if (result is Status.Failure) result else {
            Status.Complete(((result as? Status.Complete)?.result ?: Profile()).card != null)
        }
    }
}