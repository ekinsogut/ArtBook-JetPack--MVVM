package com.ekinsogut.artbook.model

data class ImageResponse(

    //Pixabay'dan dönecek olan response'un yapısı, hits içerisinde dönen liste ImageResult içerisinde tanımlı

    val total: Int,
    val totalHits: Int,
    val hits: List<ImageResult>
)
