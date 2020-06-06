package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.viewmodels.CreateVisitViewModel

@Suppress("UNCHECKED_CAST")
class CreateVisitFactory(private val repository: CardRepository) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = CreateVisitViewModel(repository) as T
}