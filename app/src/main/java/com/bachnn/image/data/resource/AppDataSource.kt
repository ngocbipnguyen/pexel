package com.bachnn.image.data.resource

import com.bachnn.image.data.models.Image
import com.bachnn.image.data.models.PexelsResponse

interface AppDataSource {

    interface Local {

    }

    interface Remote {

        suspend fun getImages(): PexelsResponse

    }

}