package com.bachnn.image.compose.composescreen

import android.view.RoundedCorner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachnn.image.data.models.Collection
import com.bachnn.image.data.models.CollectionResponse
import com.bachnn.image.viewmodels.CollectionUiState
import com.bachnn.image.viewmodels.CollectionViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    onClickCollection: (Collection) -> Unit
) {

    var collections: List<Collection> = ArrayList<Collection>()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            CustomizableSearchBar(
                "",
                onQueryChange = {},
                onSearch = {},
                searchResults = collections,
                onResultClick = {},
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        when (viewModel.collectionUiState) {
            is CollectionUiState.Success -> {
                collections =
                    (viewModel.collectionUiState as CollectionUiState.Success).collection.collections;
                CollectionPage(
                    Modifier.padding(top = contentPadding.calculateTopPadding()),
                    (viewModel.collectionUiState as CollectionUiState.Success).collection,
                    scrollBehavior
                )
            }

            is CollectionUiState.Error -> ErrorPage("")
            CollectionUiState.Loading -> LoadingPage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionTopBar(query: String, onQueryChange: (String) -> Unit) {
    CenterAlignedTopAppBar(
        title = {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Search...", style = MaterialTheme.typography.bodyLarge) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionPage(
    modifier: Modifier = Modifier,
    collection: CollectionResponse,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),

        ) {
        items(items = collection.collections) {
            if (it.media?.size!! > 0) {
                CollectionItem(it)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CollectionItem(collection: Collection) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Row {
            Card(
                Modifier
                    .weight(1f)
                    .height(140.dp)
                    .clickable {
                    }
                    .padding(4.dp),
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
            ) {
                GlideImage(
                    model = collection.media?.get(0)?.src?.medium,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }

            GlideImage(
                model = collection.media?.get(1)?.src?.medium,
                contentDescription = "",
                Modifier
                    .weight(1f)
                    .height(140.dp)
                    .clickable {
                    }
                    .padding(4.dp),
                contentScale = ContentScale.Crop,
            )

            Card(
                Modifier
                    .weight(1f)
                    .height(140.dp)
                    .clickable {
                    }
                    .padding(4.dp),
                shape = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)
            ) {
                GlideImage(
                    model = collection.media?.get(2)?.src?.medium,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        }
        Text(
            text = collection.title,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 4.dp)
        )
        Text(
            text = "${collection.media_count} Videos & Photos",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
    }
}

@Composable
fun LoadingPage() {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()

    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(48.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun ErrorPage(errorMessage: String) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(errorMessage)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizableSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    searchResults: List<Collection>,
    onResultClick: (String) -> Unit,
    // Customization options
    placeholder: @Composable () -> Unit = { Text("Search...") },
    leadingIcon: @Composable (() -> Unit)? = {
        Icon(
            Icons.Default.Search,
            contentDescription = "Search"
        )
    },
    trailingIcon: @Composable (() -> Unit)? = null,
    supportingContent: (@Composable (String) -> Unit)? = null,
    leadingContent: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    // Track expanded state of search bar
    var expanded by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier
            .fillMaxWidth()
            .semantics { isTraversalGroup = true }
    ) {
        SearchBar(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(bottom = 4.dp)
                .semantics { traversalIndex = 0f },
            inputField = {
                // Customizable input field implementation
                SearchBarDefaults.InputField(
                    query = query,
                    onQueryChange = onQueryChange,
                    onSearch = {
                        onSearch(query)
                        expanded = false
                    },
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    placeholder = placeholder,
                    leadingIcon = leadingIcon,
                    trailingIcon = trailingIcon
                )
            },
            expanded = expanded,
            onExpandedChange = { expanded = it },
        ) {
            // Show search results in a lazy column for better performance
            LazyColumn(modifier = modifier) {
                items(count = searchResults.size) { index ->
                    val resultText = searchResults[index]
                    CollectionItem(resultText)
//                    ListItem(
//                        headlineContent = { Text(resultText) },
//                        supportingContent = supportingContent?.let { { it(resultText) } },
//                        leadingContent = leadingContent,
//                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
//                        modifier = Modifier
//                            .clickable {
//                                onResultClick(resultText)
//                                expanded = false
//                            }
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp, vertical = 4.dp)
//                    )
                }
            }
        }
    }
}
