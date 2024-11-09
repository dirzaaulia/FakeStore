package com.dirzaaulia.fakestore.repository

import com.dirzaaulia.fakestore.model.Product
import com.dirzaaulia.fakestore.util.ResponseResult
import kotlinx.coroutines.flow.Flow

interface NetworkRepository {
    fun getProducts(): Flow<ResponseResult<List<Product>>>
    fun getProduct(productId: Int): Flow<ResponseResult<Product>>
    fun getCategories(): Flow<ResponseResult<List<String>>>
}