package com.example.animecharacters.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.content.MediaType.Companion.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.animecharacters.navigation.Screen

@Composable
fun CharacterGrid(navController: NavController, viewModel: CharacterViewModel = hiltViewModel()) {
    val characters = viewModel.characterList


    LazyVerticalGrid(columns = GridCells.Fixed(3), contentPadding = PaddingValues(8.dp)) {
        items(characters) { character ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.CharacterDetail.createRoute(character.mal_id))
                    },
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AsyncImage(
                        model = character.images.jpg.image_url,
                        contentDescription = character.name,
                        modifier = Modifier
                            .height(120.dp)
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Text(character.name, modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun CharacterDetailScreen(
    characterId: Int,
    viewModel: CharacterViewModel = hiltViewModel()
) {
    val character = viewModel.characterList.find { it.mal_id == characterId }

    character?.let {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = it.images.jpg.image_url,
                contentDescription = it.name,
                modifier = Modifier
                    .height(250.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Name: ${it.name}", style = MaterialTheme.typography.titleLarge)
            it.name_kanji?.let { kanji ->
                Text(text = "Kanji: $kanji", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Anime Appearances:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
            it.anime?.forEach { anime ->
                Text(text = "• ${anime.anime.title}")
            }

            Spacer(modifier = Modifier.height(16.dp))

            it.about?.let { aboutText ->
                Text(
                    text = "About:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Text(
                    text = aboutText,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    } ?: run {
        Text("Character not found", modifier = Modifier.padding(16.dp))
    }
}