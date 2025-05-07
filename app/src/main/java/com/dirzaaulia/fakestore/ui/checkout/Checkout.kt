package com.dirzaaulia.fakestore.ui.checkout

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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.dirzaaulia.fakestore.model.Product
import com.dirzaaulia.fakestore.ui.cart.CartViewModel
import com.dirzaaulia.fakestore.ui.cart.ItemCart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Checkout(
    viewModel: CheckoutViewModel = hiltViewModel(),
    navigateUp: () -> Unit
) {

    val cart = viewModel.cart.collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        modifier = Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 16.dp),
                        text = "Checkout"
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
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = "You are buying there items"
                )
            }
            items(cart.value) { item ->
                ItemCheckout(item = item)
            }
        }
    }
}

@Composable
fun ItemCheckout(item: Product) {

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
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "$${item.price}",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "x ${item.count}",
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}