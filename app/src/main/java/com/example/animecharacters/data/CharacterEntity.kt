package com.example.animecharacters.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey(autoGenerate = true)
    val mal_id: Int,
    val name: String,
    val name_kanji: String?,
    val image_url: String,
    val about: String?
)