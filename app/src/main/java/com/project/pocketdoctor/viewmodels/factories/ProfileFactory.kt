package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoctor.repositories.ProfileRepository
import com.project.pocketdoctor.viewmodels.LoginViewModel
import com.project.pocketdoctor.viewmodels.MainViewModel
import com.project.pocketdoctor.viewmodels.ProfileViewModel

@Suppress("UNCHECKED_CAST")
class ProfileFactory(private val repository: ProfileRepository, private val type: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = when (type) {
        0 -> LoginViewModel(repository)
        1 -> MainViewModel(repository)
        else -> ProfileViewModel(repository)
    } as T
}