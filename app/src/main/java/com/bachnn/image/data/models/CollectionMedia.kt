package com.bachnn.image.data.models

data class CollectionMediaResponse(
    val media: List<MediaItem>
)

data class MediaItem(
    val id: Long,
    val type: String,
    val src: PhotoSrc? = null,
    val video_files: List<VideoFile>? = null
)

data class VideoFile(
    val link: String
)
