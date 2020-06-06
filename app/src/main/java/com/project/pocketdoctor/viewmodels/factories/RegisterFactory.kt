package com.project.pocketdoctor.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.NewInstanceFactory
import com.project.pocketdoctor.repositories.CardRepository
import com.project.pocketdoctor.viewmodels.RegisterViewModel

@Suppress("UNCHECKED_CAST")
class RegisterFactory(private val cardRepository: CardRepository) : NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>) = RegisterViewModel(cardRepository) as T
}