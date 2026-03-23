package com.proto.diabetes_aisystem

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun UserDetailsScreen(
    onNext: (name: String, age: String, gender: String) -> Unit
) {
    var patientName by remember { mutableStateOf("") }
    var age         by remember { mutableStateOf("") }
    var gender      by remember { mutableStateOf("") }
    var error       by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Patient Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )

        Text(
            text = "Enter the patient's personal information",
            fontSize = 14.sp,
            color = Color.Gray
        )

        OutlinedTextField(
            value = patientName,
            onValueChange = { patientName = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = age,
            onValueChange = { age = it },
            label = { Text("Age") },
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Gender", fontSize = 14.sp, color = Color.DarkGray)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Male", "Female", "Other").forEach { option ->
                FilterChip(
                    selected = gender == option,
                    onClick  = { gender = option },
                    label    = { Text(option) }
                )
            }
        }

        if (error.isNotEmpty()) {
            Text(text = error, color = Color.Red, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (patientName.isEmpty() || age.isEmpty() || gender.isEmpty()) {
                    error = "Please fill in all fields"
                } else {
                    onNext(patientName, age, gender)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
        ) {
            Text("Next", fontSize = 16.sp, color = Color.White)
        }
    }
}