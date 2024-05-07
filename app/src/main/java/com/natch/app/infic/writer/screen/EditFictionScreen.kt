package com.natch.app.infic.writer.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.natch.app.infic.model.FictionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFictionScreen(viewModel: FictionViewModel) {
    val navController = rememberNavController()
    var navSelectedItem by rememberSaveable { mutableStateOf(0) }
    val routeList = listOf("EditScene", "EditParameter", "EditProfile")
    val iconList = listOf(
        Icons.Filled.List,
        Icons.Filled.Build,
        Icons.Filled.AccountCircle
    )
    val labelList = listOf("scene", "parameter", "profile")

    var fictionTitle by rememberSaveable { mutableStateOf(viewModel.currentFiction.value!!.title) }
    val onUpdateCallback = {
        fictionTitle = viewModel.currentFiction.value!!.title
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("$fictionTitle") })
        },
        bottomBar = {
            NavigationBar {
                routeList.forEachIndexed { index, route ->
                    NavigationBarItem(
                        selected = (index == navSelectedItem),
                        onClick = {
                            navSelectedItem = index
                            navController.navigate(route) {
                                // don't add current screen to backstack
                                popUpTo(navController.currentDestination!!.id) {
                                    inclusive = true
                                }
                            }
                        },
                        icon = { Icon(imageVector = iconList[index], contentDescription = "Icon") },
                        label = { Text(labelList[index]) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "EditScene",
            modifier = Modifier.padding(paddingValues)
        ) {
            // TODO: implement these route and their screens
            // TODO: dynamic top bar icons and actions
            composable("EditScene") {
                EditSceneScreen()
            }
            composable("EditScene/{sceneUUID}", arguments = listOf(navArgument("sceneUUID") { type = NavType.StringType })) {
                EditSceneUUIDScreen()
            }
            composable("EditParameter") {
                EditParameterScreen(viewModel)
            }
            composable("EditProfile") {
                // TODO: (Optional) Change this to proper state handler (not just work around like this)
                EditProfileScreen(viewModel, onUpdateCallback = onUpdateCallback)
            }
        }
    }
}

fun saveFiction() {

}