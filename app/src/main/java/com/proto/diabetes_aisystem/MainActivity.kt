package com.proto.diabetes_aisystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.proto.diabetes_aisystem.ui.theme.Diabetes_AISystemTheme

/**
 * MainActivity is the "Front Door" of your app.
 * When the user clicks the app icon, Android starts here.
 */
class MainActivity : ComponentActivity() {
    
    // onCreate is the first function that runs when the activity is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // This makes the app content go behind the status bar and navigation bar (full screen).
        enableEdgeToEdge()
        
        // setContent is where we define what the UI looks like using Compose.
        setContent {
            // Apply your app's custom theme (colors, fonts, etc.)
            Diabetes_AISystemTheme {
                
                // Scaffold provides a basic screen structure (like a slot for a TopBar or BottomBar).
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    
                    // We call the UserDetailsScreen and pass the padding from the Scaffold.
                    // This ensures our content doesn't get cut off by the system bars.
                    UserDetailsScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
