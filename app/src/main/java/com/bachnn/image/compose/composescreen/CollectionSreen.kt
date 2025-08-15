package com.bachnn.image.compose.composescreen

import android.view.RoundedCorner
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachnn.image.data.models.Collection
import com.bachnn.image.data.models.CollectionResponse
import com.bachnn.image.viewmodels.CollectionUiState
import com.bachnn.image.viewmodels.CollectionViewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    onClickCollection: (Collection) -> Unit
) {
    Scaffold(
        topBar = { CollectionTopBar("", onQueryChange = {}) }
    ) { contentPadding ->
        when (viewModel.collectionUiState) {
            is CollectionUiState.Success -> {
                CollectionPage(
                    Modifier.padding(top = contentPadding.calculateTopPadding()),
                    (viewModel.collectionUiState as CollectionUiState.Success).collection
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


@Composable
fun CollectionPage(modifier: Modifier = Modifier, collection: CollectionResponse) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = modifier
            .fillMaxSize()
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