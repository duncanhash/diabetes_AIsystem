package com.proto.diabetes_aisystem

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.proto.diabetes_aisystem.ui.theme.Diabetes_AISystemTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Diabetes_AISystemTheme {
                var currentScreen by remember { mutableStateOf("details") }
                var patientName by remember { mutableStateOf("") }
                var patientAge by remember { mutableStateOf("") }
                var patientGender by remember { mutableStateOf("") }

                when (currentScreen) {
                    "details" -> UserDetailsScreen(
                        onNext = { name, age, gender ->
                            patientName = name
                            patientAge = age
                            patientGender = gender
                            currentScreen = "clinical"
                        }
                    )
                    "clinical" -> ClinicalScreen(
                        name = patientName,
                        age = patientAge,
                        gender = patientGender,
                        onBack = { currentScreen = "details" },
                        onHistory = { currentScreen = "history" }
                    )
                    "history" -> HistoryScreen(
                        onBack = { currentScreen = "clinical" }
                    )
                }
            }
        }
    }
}