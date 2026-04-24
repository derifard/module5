package com.example.kotlinmirea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kotlinmirea.ui.screens.PhotoGalleryScreen
import com.example.kotlinmirea.ui.screens.PhotoViewScreen
import com.example.kotlinmirea.ui.theme.KotlinMIREATheme
import com.example.kotlinmirea.viewmodel.PhotoViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinMIREATheme {
                PhotoApp()
            }
        }
    }
}

@Composable
fun PhotoApp() {
    val navController = rememberNavController()
    val photoViewModel: PhotoViewModel = viewModel()

    NavHost(navController = navController, startDestination = "gallery") {
        composable("gallery") {
            PhotoGalleryScreen(
                onOpenPhoto = { path ->
                    val encoded = URLEncoder.encode(path, StandardCharsets.UTF_8.toString())
                    navController.navigate("photo/$encoded")
                },
                photoViewModel = photoViewModel
            )
        }
        composable(
            route = "photo/{filePath}",
            arguments = listOf(navArgument("filePath") { type = NavType.StringType })
        ) { backStackEntry ->
            val encoded = backStackEntry.arguments?.getString("filePath") ?: ""
            val filePath = URLDecoder.decode(encoded, StandardCharsets.UTF_8.toString())
            PhotoViewScreen(
                filePath = filePath,
                onBack = { navController.popBackStack() },
                photoViewModel = photoViewModel
            )
        }
    }
}