package com.proto.diabetes_aisystem



import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    var patients by remember { mutableStateOf<List<JSONObject>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val url = "http://10.0.2.2:3000/patients"
        val request = JsonArrayRequest(url,
            { response ->
                val list = mutableListOf<JSONObject>()
                for (i in 0 until response.length()) {
                    list.add(response.getJSONObject(i))
                }
                patients = list
                isLoading = false
            },
            { err ->
                error = "Could not load history: ${err.message}"
                isLoading = false
            }
        )
        Volley.newRequestQueue(context).add(request)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        Text(
            "Patient History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A237E)
        )
        Text(
            "${patients.size} records found",
            fontSize = 13.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(12.dp))

        when {
            isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            error.isNotEmpty() -> {
                Text(error, color = Color.Red, fontSize = 13.sp)
            }
            patients.isEmpty() -> {
                Text("No patients saved yet.", color = Color.Gray)
            }
            else -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(patients) { patient ->
                        val riskLevel = patient.optString("risk_level", "")
                        val bgColor = when (riskLevel) {
                            "high"   -> Color(0xFFFFCDD2)
                            "medium" -> Color(0xFFFFF9C4)
                            else     -> Color(0xFFC8E6C9)
                        }
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = bgColor)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    patient.optString("name", "Unknown"),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    "Age: ${patient.optString("age")}  |  Gender: ${patient.optString("gender")}",
                                    fontSize = 13.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    patient.optString("label", ""),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    "Score: ${patient.optInt("score")} / 21",
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                                Text(
                                    "Saved: ${patient.optString("saved_at")}",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back")
        }
    }
}