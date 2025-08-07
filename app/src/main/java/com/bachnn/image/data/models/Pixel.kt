package com.bachnn.image.data.models

import kotlinx.serialization.Serializable


@Serializable
data class PexelsPhoto(
    val id: Long,
    val photographer: String,
    val src: PhotoSrc
)

@Serializable
data class PhotoSrc(
    val original: String,
    val medium: String,
    val small: String
)

data class PexelsResponse(
    val page: Int,
    val per_page: Int,
    val photos: List<PexelsPhoto>
)
