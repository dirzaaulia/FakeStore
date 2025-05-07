package com.dirzaaulia.fakestore.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.dirzaaulia.fakestore.model.Product
import com.dirzaaulia.fakestore.ui.common.CommonLoading
import com.dirzaaulia.fakestore.ui.common.ErrorContent
import com.dirzaaulia.fakestore.util.ResponseResult
import com.dirzaaulia.fakestore.util.success
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    viewModel: HomeViewModel,
    navigateToDetail: (Int) -> Unit,
    navigateToCart: () -> Unit,
) {
    val productsState = viewModel.productsState.collectAsStateWithLifecycle()
    val products = viewModel.product.collectAsStateWithLifecycle().value
    val categoriesState = viewModel.categoriesState.collectAsStateWithLifecycle()
    val email = viewModel.email.collectAsStateWithLifecycle()
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Fake Store") },
                actions = {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                navigateToCart.invoke()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ShoppingCart,
                            contentDescription = "Cart"
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                val isBottomSheetOpen = openBottomSheet
                                openBottomSheet = !isBottomSheetOpen
                            }
                        }
                    ) {
                        Icon(
                            modifier = Modifier.padding(start = 8.dp),
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Person"
                        )
                    }

                }
            )
        },
    ) { innerPadding ->
        when (productsState.value) {
            ResponseResult.Loading -> {
                CommonLoading()
            }

            is ResponseResult.Success -> {
                Column(modifier = Modifier.padding(innerPadding)) {
                    categoriesState.value.success { categories ->
                        CategoryChip(
                            viewModel = viewModel,
                            categories = categories.toMutableList().apply {
                                add(0, "All")
                            }
                        )
                    }
                    LazyVerticalStaggeredGrid(
                        modifier = Modifier.padding(top = 8.dp),
                        columns = StaggeredGridCells.Fixed(2),
                        verticalItemSpacing = 8.dp,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = products,
                            key = { item -> item.id }
                        ) { item ->
                            ProductItem(
                                item = item,
                                navigateToDetail = navigateToDetail
                            )
                        }
                    }
                }
            }

            is ResponseResult.Error -> {
                val errorMessage = (productsState.value as ResponseResult.Error).throwable.message
                ErrorContent(
                    text = errorMessage.orEmpty().ifEmpty { "Something went wrong" },
                ) { viewModel.retry() }
            }
        }
    }


    // Sheet content
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
        ) {
            Profile(email.value)
        }
    }
}

@Composable
fun CategoryChip(
    viewModel: HomeViewModel,
    categories: List<String>
) {
    val selectedCategory = viewModel.selectedCategories.collectAsStateWithLifecycle()

    LazyRow(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = if (selectedCategory.value.isNotEmpty()) {
                selectedCategory.value.contains(category)
            } else {
                false
            }
            FilterChip(
                onClick = {
                    if (category == "All") {
                        viewModel.clearSelectedCategories()
                    } else {
                        viewModel.addSelectedCategory(category)
                    }
                },
                label = { Text(category) },
                selected = isSelected,
                leadingIcon = if (isSelected) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
        }
    }
}

@Composable
fun Profile(email: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            modifier = Modifier.size(56.dp),
            imageVector = Icons.Filled.Person,
            contentDescription = "Profile"
        )
        Text(
            text = email
        )
    }
}

@Composable
fun ProductItem(
    item: Product,
    navigateToDetail: (Int) -> Unit,
) {
    Card(
        modifier = Modifier.clickable {
            navigateToDetail(item.id)
        }
    ) {
        Column {
            AsyncImage(
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                model = item.imageUrl,
                contentDescription = item.description,
                contentScale = ContentScale.FillBounds
            )
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.align(Alignment.End),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiary
                )
            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = "$${item.price}",
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onTertiary
                )
            }
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = item.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}