package com.dirzaaulia.fakestore.ui.checkout

import androidx.lifecycle.ViewModel
import com.dirzaaulia.fakestore.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    databaseRepository: DatabaseRepository
): ViewModel() {

    val cart = databaseRepository.getAllProductInCart()
}