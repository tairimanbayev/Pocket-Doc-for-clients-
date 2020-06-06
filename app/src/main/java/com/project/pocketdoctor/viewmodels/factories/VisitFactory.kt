package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.pocketdoctor.repositories.VisitRepository
import com.project.pocketdoctor.viewmodels.VisitViewModel

@Suppress("UNCHECKED_CAST")
class VisitFactory(private val repository: VisitRepository, private val visitId: Int) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = VisitViewModel(repository, visitId) as T
}