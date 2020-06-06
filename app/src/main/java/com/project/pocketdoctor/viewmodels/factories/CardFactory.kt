package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.viewmodels.CardViewModel

@Suppress("UNCHECKED_CAST")
class CardFactory(private val repository: CardRepository) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = CardViewModel(repository) as T
}