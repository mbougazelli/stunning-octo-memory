package com.example.animecharacters.network

import com.example.animecharacters.data.CharacterResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface AnimeApiService {
    @GET("characters")
    suspend fun getCharacters(): CharacterResponse
}

object RetrofitInstance {
    val api: AnimeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.jikan.moe/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeApiService::class.java)
    }
}