package com.dirzaaulia.fakestore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dirzaaulia.fakestore.model.Product
import com.dirzaaulia.fakestore.repository.NetworkRepository
import com.dirzaaulia.fakestore.util.ResponseResult
import com.dirzaaulia.fakestore.util.success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NetworkRepository
): ViewModel() {
    private val _productsState: MutableStateFlow<ResponseResult<List<Product>>> =
        MutableStateFlow(ResponseResult.Success(emptyList()))
    val productsState = _productsState.asStateFlow()

    private var initialProducts = emptyList<Product>()

    private val _products: MutableStateFlow<List<Product>> =
        MutableStateFlow(emptyList())
    val product = _products.asStateFlow()

    private val _categoriesState: MutableStateFlow<ResponseResult<List<String>>> =
        MutableStateFlow(ResponseResult.Success(emptyList()))
    val categoriesState = _categoriesState.asStateFlow()

    private val _selectedCategories: MutableStateFlow<List<String>> =
        MutableStateFlow(emptyList())
    val selectedCategories = _selectedCategories.asStateFlow()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password = _password.asStateFlow()


    init {
        getProducts()
        getCategories()
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setPassword(password: String) {
        _password.value = password
    }

    fun retry() {
        getProducts()
        getCategories()
    }

    fun addSelectedCategory(category: String) {
        if (category in _selectedCategories.value) {
            _selectedCategories.value = _selectedCategories.value.toMutableList().apply {
                remove(category)
            }
        } else {
            _selectedCategories.value = _selectedCategories.value.toMutableList().apply {
                add(category)
            }
        }
        _products.value = initialProducts.filter {
            it.category in _selectedCategories.value

        }
    }

    fun clearSelectedCategories() {
        _selectedCategories.value = emptyList()
        _products.value = initialProducts
    }

    private fun getProducts() {
        repository.getProducts()
            .onEach { result ->
                result.success {
                    initialProducts = it
                    _products.value = it
                }
                _productsState.value = result
            }
            .launchIn(viewModelScope)
    }

    private fun getCategories() {
        repository.getCategories()
            .onEach { result ->
                _categoriesState.value = result
            }
            .launchIn(viewModelScope)
    }
}