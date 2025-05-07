package com.dirzaaulia.fakestore.ui.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.fakestore.model.Product
import com.dirzaaulia.fakestore.repository.DatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val databaseRepository: DatabaseRepository
): ViewModel() {

    val cart = databaseRepository.getAllProductInCart()

    fun updateProductToCart(product: Product) {
        viewModelScope.launch {
            databaseRepository.addProductToCart(product)
        }
    }

    fun deleteProductFromCart(product: Product) {
        viewModelScope.launch {
            databaseRepository.deleteProductFromCart(product)
        }
    }
}