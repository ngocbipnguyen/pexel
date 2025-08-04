package com.bachnn.image.compose.composescreen

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachnn.image.R
import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsPhoto
import com.bachnn.image.data.models.PexelsResponse
import com.bachnn.image.viewmodels.HomeViewModel
import com.bachnn.image.viewmodels.ImageUiState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage


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
                    onClickImage
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
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePagerScreen(
    modifier: Modifier = Modifier,
    images: PexelsResponse,
    scrollBehavior: TopAppBarScrollBehavior,
    onClickImage: (PexelsPhoto) -> Unit
) {
//    LazyVerticalGrid(
//        columns = GridCells.Fixed(2),
//        modifier.imePadding(),
//    ) {
//        items(
//            items = images.photos
//        ) {
//            ItemImage(image = it, onClickImage = onClickImage)
//        }
//    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = {
            items(images.photos) { it ->
                ItemImage(image = it, onClickImage = onClickImage)
            }
        }
    )
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



