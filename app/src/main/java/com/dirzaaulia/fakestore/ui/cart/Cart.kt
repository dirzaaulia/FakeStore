package com.dirzaaulia.fakestore.ui.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.dirzaaulia.fakestore.model.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Cart(
    viewModel: CartViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
    navigateToCheckout: () -> Unit
) {

    val cart = viewModel.cart.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        floatingActionButton = {
            AnimatedVisibility(visible = cart.value.isNotEmpty()) {
                FloatingActionButton(
                    onClick = {
                        navigateToCheckout.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Payments,
                        contentDescription = "Checkout"
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Cart"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navigateUp.invoke() }
                    ) {
                        Icon(
                            modifier = Modifier.padding(start = 8.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigation Back"
                        )

                    }
                }
            )
        }
    ) { innerPadding ->
        if (cart.value.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cart.value) { item ->
                    ItemCart(
                        viewModel = viewModel,
                        item = item
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(innerPadding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(56.dp),
                    imageVector = Icons.Filled.ShoppingCart,
                    contentDescription = "Empty Cart",
                )
                Text(
                    text = "Cart is empty",
                    style = MaterialTheme.typography.displayLarge
                )
            }
        }
    }
}

@Composable
fun ItemCart(
    viewModel: CartViewModel,
    item: Product
) {
    var count = rememberSaveable { mutableIntStateOf(item.count) }
    val removeIcon = if (count.value == 1) Icons.Filled.Delete else Icons.Filled.Remove

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card {
                AsyncImage(
                    modifier = Modifier.size(height = 120.dp, width = 100.dp),
                    model = item.imageUrl,
                    contentDescription = item.title,
                    contentScale = ContentScale.FillBounds
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = item.title,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "$${item.price}"
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        onClick = {
                            count.intValue = count.intValue.minus(1)

                            if (count.intValue == 0) {
                                viewModel.deleteProductFromCart(item)
                            } else {
                                viewModel.updateProductToCart(item.copy(count = count.intValue))
                            }
                        }
                    ) {
                        Icon(
                            imageVector = removeIcon,
                            contentDescription = "Button Remove"
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .weight(1f),
                        value = count.intValue.toString(),
                        onValueChange = {
                            count.intValue = it.toInt()
                            viewModel.updateProductToCart(item.copy(count = count.intValue))
                        },
                        textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                    )
                    IconButton(
                        onClick = {
                            count.intValue = count.intValue.plus(1)
                            viewModel.updateProductToCart(item.copy(count = count.intValue))
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Button Remove"
                        )
                    }
                }
            }
        }
    }
}