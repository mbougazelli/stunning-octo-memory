package com.example.animecharacters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.animecharacters.navigation.Screen
import com.example.animecharacters.ui.theme.AnimeCharactersTheme
import com.example.animecharacters.view.CharacterDetailScreen
import com.example.animecharacters.view.CharacterGrid
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = Screen.CharacterList.route) {
                composable(Screen.CharacterList.route) {
                    CharacterGrid(navController)
                }
                composable(
                    route = Screen.CharacterDetail.route,
                    arguments = listOf(navArgument("characterId") { type = NavType.IntType })
                ) { backStackEntry ->
                    val characterId = backStackEntry.arguments?.getInt("characterId")
                    characterId?.let {
                        CharacterDetailScreen(characterId = it)
                    }
                }
            }
        }
    }
}