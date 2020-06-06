package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Visit
import com.project.pocketdoctor.repositories.VisitRepository
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class VisitViewModel(private val repository: VisitRepository, private val visitId: Int) : LazyViewModel<Visit>() {

    override fun loadData() {
        viewModelScope.launch {
            _status.value = Status.Complete(repository.getVisitWithCards(visitId))
        }
    }
}