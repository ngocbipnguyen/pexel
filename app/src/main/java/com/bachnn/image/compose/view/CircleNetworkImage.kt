package com.bachnn.image.compose.view

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CircleNetworkImage(modifier: Modifier,url: String) {
    GlideImage(
        model = url,
        contentDescription = "Circle Image",
        modifier = modifier
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}