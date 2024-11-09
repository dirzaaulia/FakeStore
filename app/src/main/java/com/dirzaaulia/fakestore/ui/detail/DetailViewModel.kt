package com.dirzaaulia.fakestore.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.fakestore.model.Product
import com.dirzaaulia.fakestore.repository.DatabaseRepository
import com.dirzaaulia.fakestore.repository.NetworkRepository
import com.dirzaaulia.fakestore.util.ResponseResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val databaseRepository: DatabaseRepository
): ViewModel() {

    private val _productState: MutableStateFlow<ResponseResult<Product?>> =
        MutableStateFlow(ResponseResult.Success(null))
    val productState = _productState.asStateFlow()

    fun getProduct(productId: Int) {
        networkRepository.getProduct(productId)
            .onEach { result ->
                _productState.value = result
            }
            .launchIn(viewModelScope)
    }

    fun addProductToCart(product: Product) {
        viewModelScope.launch {
            databaseRepository.addProductToCart(product)
        }
    }
}