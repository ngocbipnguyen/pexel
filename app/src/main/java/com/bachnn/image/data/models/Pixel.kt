package com.bachnn.image.data.models


data class PexelsPhoto(
    val id: Long,
    val photographer: String,
    val src: PhotoSrc
)

data class PhotoSrc(
    val original: String,
    val medium: String
)

data class PexelsResponse(
    val page: Int,
    val per_page: Int,
    val photos: List<PexelsPhoto>
)
