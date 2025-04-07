package com.example.animecharacters.view

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animecharacters.data.Characters
import com.example.animecharacters.network.AnimeApiService
import com.example.animecharacters.network.RetrofitInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CharacterViewModel @Inject constructor(
    private val api: AnimeApiService
) : ViewModel() {

    var characterList by mutableStateOf<List<Characters>>(emptyList())
        private set

    init {
        fetchCharacters()
    }

    private fun fetchCharacters() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCharacters()
                characterList = response.data
            } catch (e: Exception) {
                Log.e("CharacterViewModel", "Error: ${e.message}")
            }
        }
    }
}