package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.google.firebase.iid.FirebaseInstanceId
import com.project.pocketdoctor.repositories.ProfileRepository
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: ProfileRepository) : StatusViewModel<Boolean>() {
    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearData()
            FirebaseInstanceId.getInstance().deleteInstanceId()
            _status.postValue(Status.Complete(true))
        }
    }
}