package com.bachnn.image.compose.composescreen

import android.app.Activity
import android.os.Build
import android.view.WindowInsets
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.bachnn.image.viewmodels.FullViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.github.panpf.zoomimage.GlideZoomAsyncImage
import com.github.panpf.zoomimage.compose.glide.ExperimentalGlideComposeApi
import com.github.panpf.zoomimage.compose.glide.GlideSubcomposition

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullScreen(
    imageId: String,
    originalUrl: String,
    mediumUrl: String,
    smallUrl: String,
    photographer: String,
    viewModel: FullViewModel = hiltViewModel()
) {

    GlideSubcomposition(originalUrl, Modifier.fillMaxSize(), requestBuilderTransform = {
        it.thumbnail()
    }) { }

    FullPage(
        imageId = imageId,
        originalUrl = originalUrl,
        mediumUrl = mediumUrl,
        smallUrl = smallUrl,
        photographer = photographer
    )
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FullPage(
    imageId: String,
    originalUrl: String,
    mediumUrl: String,
    smallUrl: String,
    photographer: String
) {

    val context = LocalContext.current
    val activity = context as? Activity
    if (activity != null) {
        val window = activity.window

        SideEffect {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, window.decorView).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    window.insetsController?.hide(WindowInsets.Type.statusBars())
                } else {
                    WindowInsetsControllerCompat(window, window.decorView).hide(WindowInsetsCompat.Type.statusBars())
                }
                systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val signature = ObjectKey(imageId)
        val context = LocalContext.current
        val requestManager = remember { Glide.with(context) }
        GlideZoomAsyncImage(
            model = originalUrl,
            contentDescription = "view image",
            modifier = Modifier.fillMaxSize(),
        ) {
            it
                .thumbnail(
                    requestManager
                        .asDrawable()
                        .load(smallUrl)
                        .signature(signature)
                )
                .signature(signature)
        }
    }
}