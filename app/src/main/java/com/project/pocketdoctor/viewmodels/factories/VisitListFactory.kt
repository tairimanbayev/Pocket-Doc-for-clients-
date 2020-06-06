package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoctor.repositories.VisitRepository
import com.project.pocketdoctor.viewmodels.VisitListViewModel

@Suppress("UNCHECKED_CAST")
class VisitListFactory(private val repository: VisitRepository, private val isHistory: Boolean) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = VisitListViewModel(repository, isHistory) as T
}