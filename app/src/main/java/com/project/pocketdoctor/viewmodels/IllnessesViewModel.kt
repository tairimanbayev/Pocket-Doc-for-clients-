package com.project.pocketdoctor.viewmodels

import androidx.lifecycle.viewModelScope
import com.project.pocketdoctor.model.tables.Illness
import com.project.pocketdoctor.repositories.IllnessRepository
import com.project.pocketdoctor.util.FCM_PROBLEM
import com.project.pocketdoctor.util.Status
import kotlinx.coroutines.launch

class IllnessesViewModel(private val repository: IllnessRepository, private val id: Int, private val isCard: Boolean) :
    LazyViewModel<List<Illness>>() {

    override fun loadData() {
        _status.value = Status.Loading
        viewModelScope.launch {
            _status.value = repository.getFcm()?.fcmId?.let {
                if (isCard) repository.getIllnessListByCardId(id, it) else repository.getIllnessListByVisitId(id, it)
            } ?: Status.Failure(FCM_PROBLEM)
        }
    }

    fun reload() { loadData() }
}