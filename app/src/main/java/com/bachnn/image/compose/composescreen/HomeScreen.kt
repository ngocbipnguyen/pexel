package com.bachnn.image.compose.composescreen

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachnn.image.R
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsPhoto
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.viewmodels.HomeViewModel
import com.bachnn.image.viewmodels.ImageUiState
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey


// API = KCkFgOwhsqzLSX9PQTm0JaclX5BXr9gtyPJbiDzjZ0o7NoeV0vsIYeTx
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickImage: (PexelsPhoto) -> Unit, viewModel: HomeViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            HomeTopBar(Modifier, scrollBehavior)
        }
    ) { contentPadding ->
        when (viewModel.imageUiState) {
            is ImageUiState.Success -> {
                HomePagerScreen(
                    Modifier.padding(top = contentPadding.calculateTopPadding()),
                    (viewModel.imageUiState as ImageUiState.Success).images,
                    scrollBehavior,
                    onClickImage,
                    viewModel
                )
            }

            is ImageUiState.Loading -> {
                HomePagerLoading()
            }

            else -> {
                HomePagerError("")
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = { Text(text = stringResource(id = R.string.home_screen)) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePagerScreen(
    modifier: Modifier = Modifier,
    images: PexelsResponse,
    scrollBehavior: TopAppBarScrollBehavior,
    onClickImage: (PexelsPhoto) -> Unit,
    viewModel: HomeViewModel
) {
    val listState = rememberLazyStaggeredGridState()
    val isLoading = viewModel.isLoading.collectAsState()

    val context = LocalContext.current
    val requestManager = remember { Glide.with(context) }
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        contentPadding = PaddingValues(8.dp),
        content = {
            items(images.photos, key = {it.id}) { it ->
                ItemImageThumbnail(
                    image = it,
                    onClickImage = onClickImage,
                    requestManager = requestManager
                )
            }

            // Show loading indicator at the bottom when loading more photos
            if (isLoading.value) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(32.dp),
                            color = MaterialTheme.colorScheme.secondary,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        )
                    }
                }
            }
        }
    )
    LaunchedEffect(listState, images.photos) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItemsIndex = visibleItems.last().index
                    val totalItemCount = listState.layoutInfo.totalItemsCount

                    if (lastVisibleItemsIndex >= totalItemCount - 6) {
                        viewModel.loadMorePhotos()
                    }
                }
            }

        images.photos.take(20).forEach { url ->
            requestManager.load(url).preload()
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemImage(image: PexelsPhoto, onClickImage: (PexelsPhoto) -> Unit) {
    GlideImage(
        model = image.src.medium,
        contentDescription = "",
        Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        contentScale = ContentScale.Crop
    )
}

@Composable
@OptIn(ExperimentalGlideComposeApi::class)
fun ItemImageThumbnail(
    image: PexelsPhoto,
    onClickImage: (PexelsPhoto) -> Unit,
    requestManager: RequestManager
) {

    val signature = ObjectKey(image.id.toString()) // thay bằng giá trị phù hợp
    GlideImage(
        model = image.src.medium,
        contentDescription = "",
        Modifier
            .fillMaxWidth()
            .height(270.dp),
        contentScale = ContentScale.Crop,
    ) {
        it
            .thumbnail(
                requestManager
                    .asDrawable()
                    .load(image.src.small)
                    .signature(signature)
            )
            .signature(signature)
    }
}


@Composable
fun HomePagerLoading(
    modifier: Modifier = Modifier
) {

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
fun HomePagerError(
    message: String
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(message)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun HomePagerLoadingPreview() {
    HomePagerLoading()
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
private fun HomePagerErrorPreview() {
    HomePagerError(stringResource(id = R.string.home_loading_error))
}



