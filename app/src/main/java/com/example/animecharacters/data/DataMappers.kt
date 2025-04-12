package com.example.animecharacters.data

fun CharacterEntity.toCharacters(): Characters {
    return Characters(
        mal_id = mal_id,
        name = name,
        name_kanji = name_kanji,
        about = about.toString(),
        images = ImageWrapper(Jpg(image_url)),
    )
}

fun Characters.toEntity(): CharacterEntity {
    return CharacterEntity(
        mal_id = mal_id,
        name = name,
        name_kanji = name_kanji,
        about = about,
        image_url = images.jpg.image_url
    )
}