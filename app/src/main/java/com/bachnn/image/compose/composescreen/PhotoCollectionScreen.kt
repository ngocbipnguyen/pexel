package com.bachnn.image.compose.composescreen

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachnn.image.compose.view.CircleNetworkImage
import com.bachnn.image.data.models.PhotoCollectionResponse
import com.bachnn.image.data.models.PhotoMediaItem
import com.bachnn.image.viewmodels.PhotoCollectionUiState
import com.bachnn.image.viewmodels.PhotoCollectionViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.signature.ObjectKey
import com.bumptech.glide.load.engine.DiskCacheStrategy


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoCollectionScreen(
    id: String,
    title: String,
    viewModel: PhotoCollectionViewModel = hiltViewModel(),
    mediaOnClick: () -> Unit
) {

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    viewModel.loadPhotoCollection(id)

    Scaffold(
        topBar = {
            TopBarPage(
                title = title,
                onBackEvent = {},
                shareEvent = {},
                scrollBehavior = scrollBehavior
            )
        }
    ) { contentPadding ->
        when (viewModel.photoCollectionUiState) {
            is PhotoCollectionUiState.Success -> {
                PhotoCollectionPage(
                    modifier = Modifier.padding(top = contentPadding.calculateTopPadding()),
                    (viewModel.photoCollectionUiState as PhotoCollectionUiState.Success).collection,
                    scrollBehavior,
                    viewModel,
                    mediaOnClick
                )
            }

            is PhotoCollectionUiState.Loading -> {
                LoadingPage()
            }

            is PhotoCollectionUiState.Error -> {
                val errorMessage =
                    (viewModel.photoCollectionUiState as PhotoCollectionUiState.Error).error

                ErrorPage(errorMessage)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoCollectionPage(
    modifier: Modifier = Modifier,
    collection: PhotoCollectionResponse,
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: PhotoCollectionViewModel,
    mediaOnClick: () -> Unit
) {

    val listState = rememberLazyStaggeredGridState()

    val context = LocalContext.current
    val requestManager = remember { Glide.with(context) }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(1),
        verticalItemSpacing = 0.dp,
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        content = {
            items(collection.media, key = { it.id }) { it ->
                ItemPhotoCollection(it, mediaOnClick, requestManager)
            }
        }
    )

    LaunchedEffect(listState, collection.media) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo }
            .collect { visibleItem ->
                if (visibleItem.isNotEmpty()) {
                    val lastVisibilityIndex = visibleItem.last().index
                    val totalItemCount = listState.layoutInfo.totalItemsCount

                    if (lastVisibilityIndex >= totalItemCount - 2) {
                        viewModel.loadMore()
                    }
                }
            }
        collection.media.take(20).forEach { url ->
            requestManager.load(url).preload()
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ItemPhotoCollection(
    media: PhotoMediaItem,
    mediaOnClick: () -> Unit,
    requestManager: RequestManager
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 20.dp)
    ) {

        val signature = ObjectKey(media.id.toString())
        val configuration = LocalConfiguration.current
        val screenWidthDp = configuration.screenWidthDp.dp
        val widthRateDp: Float =
            pxToDp(media.width).toFloat() / configuration.screenWidthDp.toFloat()
        val height = media.height / widthRateDp

        val colorCode = Color(android.graphics.Color.parseColor(media.avg_color))
        Row(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleNetworkImage(
                modifier = Modifier
                    .size(46.dp),
                media.src?.small!!
            )

            Text(
                text = media.photographer,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 12.dp)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )

            OutlinedButton(
                onClick = {},
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Follow")
            }

        }

        Box(modifier = Modifier.background(colorCode)) {
            GlideImage(
                model = media.src?.original,
                contentDescription = "",
                Modifier
                    .size(screenWidthDp, pxToDp(height.toInt()).dp)
                    .fillMaxWidth()
                    .clickable {
                        mediaOnClick()
                    },
                contentScale = ContentScale.Fit,
            ) {
                it
                    .thumbnail(
                        requestManager
                            .asDrawable()
                            .load(media.src?.small)
                            .signature(signature)
                            .dontAnimate()
                            .skipMemoryCache(false)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                    )
                    .signature(signature)
                    .dontAnimate()
                    .skipMemoryCache(false)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .onlyRetrieveFromCache(false)
            }
        }

        Row(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, top = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = "favorite",
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "favorite",
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1F)
            )

            Button(
                onClick = {},
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(text = "Download")
            }
        }

    }
}

fun pxToDp(px: Int): Int {
    val dp = px / Resources.getSystem().displayMetrics.density
    return dp.toInt()
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarPage(
    modifier: Modifier = Modifier,
    title: String,
    onBackEvent: () -> Unit,
    shareEvent: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    CenterAlignedTopAppBar(
        title = { Text(text = title) },
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = { onBackEvent }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back"
                )
            }
        },
        actions = {
            IconButton(onClick = { shareEvent }) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share"
                )
            }
        }
    )
}

