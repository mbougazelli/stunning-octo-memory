package com.example.animecharacters.view

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.animecharacters.R
import com.example.animecharacters.navigation.Screen
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@SuppressLint("QueryPermissionsNeeded")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterGrid(navController: NavController, viewModel: CharacterViewModel = hiltViewModel()) {
    val characters = viewModel.characterList
    val isLoading = viewModel.isLoading

    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var isSheetOpen by remember { mutableStateOf(false) }

    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sheetHeight = screenHeight * 0.8f

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color("#82B1FF".toColorInt())),
                title = { Text("Anime Characters") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                Log.d("FAB", "FAB clicked")
                isSheetOpen = true
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email"
                )
            }
        }
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color("#82B1FF".toColorInt()))
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
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
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.background(Color("#BBDEFB".toColorInt()))
                        ) {
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

    if (isSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val emailViewModel: EmailViewModel = hiltViewModel()
            val context = LocalContext.current

            val subject by emailViewModel.subject.collectAsState()
            val recipient by emailViewModel.recipient.collectAsState()
            val body by emailViewModel.body.collectAsState()
            val isFormValid by emailViewModel.isFormValid.collectAsState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Send FeedBack", style = MaterialTheme.typography.titleLarge)
                    Spacer(modifier = Modifier.height(16.dp))

                    // Recipient Field
                    OutlinedTextField(
                        value = recipient,
                        onValueChange = { emailViewModel.updateRecipient(it) },
                        label = { Text("Recipient") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Subject Field
                    OutlinedTextField(
                        value = subject,
                        onValueChange = { emailViewModel.updateSubject(it) },
                        label = { Text("Subject") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Message Field
                    OutlinedTextField(
                        value = body,
                        onValueChange = { emailViewModel.updateBody(it) },
                        label = { Text("Message Body") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        enabled = isFormValid,
                        onClick = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:".toUri()
                                putExtra(Intent.EXTRA_EMAIL, recipient)
                                putExtra(Intent.EXTRA_SUBJECT, subject)
                                putExtra(Intent.EXTRA_TEXT, body)
                            }

                            if (intent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(intent)
                                Toast.makeText(context, "Email intent launched!", Toast.LENGTH_SHORT).show()
                                emailViewModel.clearFields() // Clear ONLY after successful submission
                            } else {
                                Toast.makeText(context, "No email app found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Text("Submit")
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

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(Color("#BBDEFB".toColorInt())),
                title = {
                    Text(text = character?.name ?: "Character Details")
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
                        .height(300.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Name: ${it.name}", style = MaterialTheme.typography.titleLarge)
                it.name_kanji?.let { kanji ->
                    Text(text = "Kanji: $kanji", style = MaterialTheme.typography.bodyLarge)
                }

                Spacer(modifier = Modifier.height(8.dp))

                it.about.let { aboutText ->
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
                    .background(Color("#BBDEFB".toColorInt())),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}