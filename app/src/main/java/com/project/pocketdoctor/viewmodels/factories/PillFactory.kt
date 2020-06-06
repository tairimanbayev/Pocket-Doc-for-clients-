package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.project.pocketdoctor.repositories.PillRepository
import com.project.pocketdoctor.viewmodels.PillViewModel

@Suppress("UNCHECKED_CAST")
class PillFactory(private val repository: PillRepository) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = PillViewModel(repository) as T
}