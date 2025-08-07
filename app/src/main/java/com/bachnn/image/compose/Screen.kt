package com.bachnn.image.compose

import kotlinx.serialization.Serializable


@Serializable
object Splash

@Serializable
object Home

@Serializable
data class Full(
    val imageId: String,
    val originalUrl: String,
    val mediumUrl: String,
    val smallUrl: String,
    val photographer: String
)