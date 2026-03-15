package com.proto.diabetes_aisystem

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * 1. THE DATA MODEL:
 * This is the "Package" we will send to the AI Brain.
 * It groups all the patient information together.
 */
data class PatientData(
    val name: String,
    val age: String,
    val gender: String,
    val weight: String
)

/**
 * 2. THE SCREEN:
 * This is what the user sees and interacts with.
 */
@Composable
fun UserDetailsScreen(modifier: Modifier = Modifier) {
    // These variables hold the text while the user is typing
    var patientName by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Enter Patient Details",
            style = MaterialTheme.typography.headlineMedium
        )

        // Patient Name Input
        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Patient Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Age Input
        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        // Gender Input
        OutlinedTextField(
            value = gender,
            onValueChange = { gender = it },
            label = { Text("Gender") },
            modifier = Modifier.fillMaxWidth()
        )

        // Weight Input
        OutlinedTextField(
            value = weight,
            onValueChange = { weight = it },
            label = { Text("Weight (kg)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        /**
         * 3. THE HANDOFF:
         * When "Next" is clicked, we pack the data and send it.
         */
        Button(
            onClick = { 
                // We create the 'Package' of data
                val currentPatient = PatientData(
                    name = patientName,
                    age = age,
                    gender = gender,
                    weight = weight
                )
                
                // For now, we print it to the logs (the "Console")
                // This is where the AI team will plug in their "AI Brain" function
                println("SYSTEM: Sending data to AI Brain -> $currentPatient")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Send to AI Brain")
        }
    }
}
