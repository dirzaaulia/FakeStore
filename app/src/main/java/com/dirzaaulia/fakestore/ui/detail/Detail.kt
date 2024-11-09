package com.dirzaaulia.fakestore.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.dirzaaulia.fakestore.ui.common.CommonLoading
import com.dirzaaulia.fakestore.ui.common.ErrorContent
import com.dirzaaulia.fakestore.util.ResponseResult
import com.dirzaaulia.fakestore.util.success
import kotlinx.coroutines.launch

@Composable
fun Detail(
    viewModel: DetailViewModel = hiltViewModel(),
    productId: Int,
) {

    val productState = viewModel.productState.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val snackbarState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.getProduct(productId)
    }

    when (productState.value) {
        ResponseResult.Loading -> { CommonLoading() }
        is ResponseResult.Success -> {
            productState.value.success { item ->
                Scaffold(
                    modifier = Modifier.systemBarsPadding(),
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                coroutineScope.launch {
                                    viewModel.addProductToCart(item!!)
                                }.invokeOnCompletion {
                                    coroutineScope.launch {
                                        snackbarState.showSnackbar("Product added to cart")
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.AddShoppingCart,
                                contentDescription = "Add to cart"
                            )
                        }
                    }
                ) { innerPadding ->
                    Column(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            model = item?.imageUrl,
                            contentDescription = item?.title,
                            contentScale = ContentScale.FillBounds
                        )
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 16.dp),
                                text = item?.title.orEmpty(),
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                text = "$${item?.price}",
                                style = MaterialTheme.typography.headlineSmall
                            )
                            Text(
                                text = item?.description.orEmpty(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
        is ResponseResult.Error -> {
            val errorMessage = (productState.value as ResponseResult.Error).throwable.message
            ErrorContent(text = errorMessage.orEmpty()) {
                viewModel.getProduct(productId)
            }
        }
    }
}