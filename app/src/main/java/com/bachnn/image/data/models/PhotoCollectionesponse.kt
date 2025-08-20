package com.bachnn.image.data.models

data class PhotoCollectionResponse(
    val id: String,
    val media: List<PhotoMediaItem>
)

data class PhotoMediaItem(
    val id: Long,
    val type: String,
    val photographer_id: Long,
    val photographer: String,
    val photographer_url: String,
    val url: String,
    val width: Int,
    val height: Int,
    val avg_color: String,
    val src: PhotoSrc? = null,
    val video_files: List<PhotoVideoFile>? = null
)

data class PhotoVideoFile(
    val link: String
)
