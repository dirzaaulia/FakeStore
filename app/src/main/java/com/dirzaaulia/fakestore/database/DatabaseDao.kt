package com.dirzaaulia.fakestore.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dirzaaulia.fakestore.model.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    @Insert(
        entity = Product::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertProduct(product: Product)

    @Delete(entity = Product::class)
    suspend fun deleteProduct(product: Product)

    @Query("SELECT * FROM product_table")
    fun getAllProduct(): Flow<List<Product>>
}