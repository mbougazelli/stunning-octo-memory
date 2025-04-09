package com.example.animecharacters.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.animecharacters.navigation.Screen
import androidx.core.graphics.toColorInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterGrid(navController: NavController, viewModel: CharacterViewModel = hiltViewModel()) {
    val characters = viewModel.characterList
    val isLoading = viewModel.isLoading

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color("#82B1FF".toColorInt())),
                title = { Text("Anime Characters") }
            )
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color("#82B1FF".toColorInt()))
                    .padding(innerPadding),
                contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier.background(Color("#82B1FF".toColorInt())),
                columns = GridCells.Fixed(3),
                contentPadding = innerPadding
            ) {
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
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(Color("#BBDEFB".toColorInt()))) {
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
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    characterId: Int,
    navController: NavController,
    viewModel: CharacterViewModel = hiltViewModel()
) {
    val character = viewModel.characterList.find { it.mal_id == characterId }
//    val isLoading = viewModel.isLoading

    Scaffold (
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color("#BBDEFB".toColorInt())),
                title = {
                    Text(text = character?.name ?: "Character Details")
                },
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack()}) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        character?.let {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color("#BBDEFB".toColorInt()))
                    .padding(innerPadding)
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
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color("#BBDEFB".toColorInt()))
                , contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}