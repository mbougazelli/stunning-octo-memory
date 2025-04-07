package com.example.animecharacters.data

import android.icu.text.CaseMap.Title

data class CharacterResponse(
    val data: List<Characters>
)

data class Characters(
    val mal_id: Int,
    val name: String,
    val name_kanji: String?,
    val about: String,
    val images: ImageWrapper,
    val anime: List<AnimeAppearence>?
)

data class ImageWrapper(
    val jpg: Jpg
)

data class Jpg(
    val image_url: String
)

data class AnimeAppearence(
    val anime: AnimeInfo
)

data class AnimeInfo(
    val title: String
)
