package com.dirzaaulia.fakestore.network.resources

import io.ktor.resources.Resource

@Resource("/products")
class Products {
    @Resource("categories")
    class Categories(val parent: Products = Products())

    @Resource("{productId}")
    class ProductId(val parent: Products = Products(), val productId: Int)
}