package com.example.kotlinmirea

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.kotlinmirea.ui.screens.PhotoGalleryScreen
import com.example.kotlinmirea.ui.theme.KotlinMIREATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KotlinMIREATheme {
                PhotoGalleryScreen()
            }
        }
    }
}