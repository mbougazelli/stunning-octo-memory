package com.example.animecharacters.view

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animecharacters.data.CharacterDao
import com.example.animecharacters.data.Characters
import com.example.animecharacters.data.toCharacters
import com.example.animecharacters.data.toEntity
import com.example.animecharacters.network.AnimeApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val api: AnimeApiService,
    private val dao: CharacterDao
) : ViewModel() {

    var characterList by mutableStateOf<List<Characters>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            isLoading = true

            val cached = dao.getAllCharacters()
            if (cached.isNotEmpty()) {
                characterList = cached.map { it.toCharacters() }
                isLoading = false
                return@launch
            }

            try {
                val response = api.getCharacters()
                characterList = response.data

                dao.insertCharacters(response.data.map { it.toEntity() })
            } catch (e: Exception) {
                Log.e("ViewModel", "API error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }
}