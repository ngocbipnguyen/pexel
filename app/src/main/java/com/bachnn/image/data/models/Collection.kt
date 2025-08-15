package com.bachnn.image.data.models


data class Collection(
    val id: String,
    val title: String,
    val description: String,
    val media_count: Int,
    val photos_count: Int,
    val videos_count: Int,
    var media: List<MediaItem>? = null
)

data class CollectionResponse(
    val page: Int,
    val per_page: Int,
    val collections: List<Collection>
)


