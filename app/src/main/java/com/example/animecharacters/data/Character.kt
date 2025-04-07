package com.example.animecharacters.data

data class CharacterResponse(
    val data: List<Characters>
)

data class Characters(
    val mal_id: Int,
    val name: String,
    val images: ImageWrapper
)

data class ImageWrapper(
    val jpg: Jpg
)

data class Jpg(
    val image_url: String
)
