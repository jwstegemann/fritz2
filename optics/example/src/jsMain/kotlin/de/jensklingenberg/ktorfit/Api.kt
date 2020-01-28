package io,fritz2.optics.optics

import io,fritz2.optics.ktofitAnnotations.GET

interface Api {
    @GET("users/{value}")
    suspend fun getName(value: String): String

    @GET("users/Foso/image")
    suspend fun loadImage(): String

    @GET("users/Foso/images")
    suspend fun loadPictures(): Int
}