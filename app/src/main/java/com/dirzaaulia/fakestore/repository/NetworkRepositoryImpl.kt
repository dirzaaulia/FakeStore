package com.dirzaaulia.fakestore.repository

import androidx.annotation.WorkerThread
import com.dirzaaulia.fakestore.model.Product
import com.dirzaaulia.fakestore.network.KtorClient
import com.dirzaaulia.fakestore.network.resources.Products
import com.dirzaaulia.fakestore.util.ResponseResult
import com.dirzaaulia.fakestore.util.executeWithResponse
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val ktor: KtorClient
): NetworkRepository {

    @WorkerThread
    override fun getProducts() = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                ktor.get<Products, List<Product>>(Products())
            }
        )
    }

    override fun getProduct(productId: Int) = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                ktor.get<Products.ProductId, Product>(
                    Products.ProductId(productId = productId)
                )
            }
        )
    }

    override fun getCategories() = flow {
        emit(ResponseResult.Loading)
        emit(
            executeWithResponse {
                ktor.get<Products.Categories, List<String>>(Products.Categories())
            }
        )
    }
}