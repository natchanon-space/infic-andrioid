package com.natch.app.infic.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.player.screen.PlaySceneScreen
import com.natch.app.infic.player.screen.PlaySelectionScreen
import com.natch.app.infic.player.screen.PlayTitleScreen
import com.natch.app.infic.ui.theme.InficTheme

class PlayerActivity : ComponentActivity() {

    private val viewModel: FictionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InficTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "PlaySelection",
                        modifier = Modifier
                    ) {
                        composable("PlaySelection") {
                            PlaySelectionScreen(viewModel, onSelectFiction = {
                                navController.navigate("PlayTitle")
                            })
                        }
                        composable("PlayTitle") {
                            PlayTitleScreen(viewModel, onStartFiction = {
                                navController.navigate("PlayScene/${viewModel.currentFiction.value?.firstSceneUUID}")
                            })
                        }
                        composable(
                            "PlayScene/{sceneUUID}",
                            arguments = listOf(navArgument("sceneUUID") {
                                type = NavType.StringType
                            })
                        ) {
                            PlaySceneScreen(
                                viewModel,
                                it.arguments?.getString("sceneUUID")!!,
                                onSelectChoice = { uuid ->
                                    navController.navigate("PlayScene/${uuid}")
                                },
                                onGameOver = {
                                    navController.navigate("PlayTitle") {
                                        popUpTo(navController.graph.id)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}