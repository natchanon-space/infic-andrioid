package com.natch.app.infic.writer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.natch.app.infic.model.FictionViewModel
import com.natch.app.infic.ui.theme.InficTheme
import com.natch.app.infic.writer.screen.EditFictionScreen
import com.natch.app.infic.writer.screen.SelectFictionScreen

class WriterActivity: ComponentActivity() {

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
                        navController,
                        startDestination = "SelectFictionScreen",
                        modifier = Modifier
                    ) {
                        composable("SelectFictionScreen") {
                            SelectFictionScreen(
                                viewModel,
                                selectFictionCallback = {
                                    navController.navigate("EditFictionScreen")
                                }
                            )
                        }
                        composable("EditFictionScreen") {
                            EditFictionScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}