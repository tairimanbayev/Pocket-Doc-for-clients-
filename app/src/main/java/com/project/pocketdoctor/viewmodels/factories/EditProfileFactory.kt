package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoctor.repositories.ProfileRepository
import com.project.pocketdoctor.viewmodels.EditProfileViewModel

@Suppress("UNCHECKED_CAST")
class EditProfileFactory(private val repository: ProfileRepository, private val cardId: Int) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditProfileViewModel(repository, cardId) as T
    }
}