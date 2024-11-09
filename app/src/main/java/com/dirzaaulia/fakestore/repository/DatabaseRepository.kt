package com.dirzaaulia.fakestore.repository

import com.dirzaaulia.fakestore.database.DatabaseDao
import com.dirzaaulia.fakestore.model.Product
import javax.inject.Inject

class DatabaseRepository @Inject constructor(
    private val databaseDao: DatabaseDao
) {

    suspend fun addProductToCart(product: Product) = databaseDao.insertProduct(product)
    suspend fun deleteProductFromCart(product: Product) = databaseDao.deleteProduct(product)
    fun getAllProductInCart() = databaseDao.getAllProduct()
}