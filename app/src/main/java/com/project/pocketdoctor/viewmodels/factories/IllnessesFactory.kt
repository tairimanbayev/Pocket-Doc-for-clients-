package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.project.pocketdoctor.repositories.IllnessRepository
import com.project.pocketdoctor.viewmodels.IllnessesViewModel

@Suppress("UNCHECKED_CAST")
class IllnessesFactory(private val repository: IllnessRepository, private val id: Int, private val isCard: Boolean) :
    NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = IllnessesViewModel(repository, id, isCard) as T
}