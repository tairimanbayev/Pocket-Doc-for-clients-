package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.project.pocketdoctor.repositories.ReasonRepository
import com.project.pocketdoctor.viewmodels.ReasonsViewModel

@Suppress("UNCHECKED_CAST")
class ReasonsFactory(
    private val repository: ReasonRepository,
    private val role: String
) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = ReasonsViewModel(repository, role) as T
}