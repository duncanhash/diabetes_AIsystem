package com.proto.diabetes_aisystem



import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

@Composable
fun ClinicalScreen(
    name: String,
    age: String,
    gender: String,
    onBack: () -> Unit,
    onHistory: () -> Unit
) {
    val context = LocalContext.current

    var bmi         by remember { mutableStateOf("") }
    var glucose     by remember { mutableStateOf("") }
    var bp          by remember { mutableStateOf("") }
    var cholesterol by remember { mutableStateOf("") }
    var activity    by remember { mutableStateOf("light") }
    var diet        by remember { mutableStateOf("average") }
    var smoking     by remember { mutableStateOf(false) }
    var familyHist  by remember { mutableStateOf(false) }
    var gestational by remember { mutableStateOf("na") }

    var isLoading   by remember { mutableStateOf(false) }
    var error       by remember { mutableStateOf("") }
    var result      by remember { mutableStateOf<JSONObject?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Clinical Assessment", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A237E))
        Text("Patient: $name  |  Age: $age  |  Gender: $gender", fontSize = 13.sp, color = Color.Gray)

        HorizontalDivider()

        Text("Clinical Measurements", fontSize = 16.sp, fontWeight = FontWeight.Medium)

        OutlinedTextField(value = bmi, onValueChange = { bmi = it },
            label = { Text("BMI (e.g. 26.5)") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = glucose, onValueChange = { glucose = it },
            label = { Text("Fasting glucose (mg/dL)") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = bp, onValueChange = { bp = it },
            label = { Text("Blood pressure systolic (mmHg) — optional") }, modifier = Modifier.fillMaxWidth())

        OutlinedTextField(value = cholesterol, onValueChange = { cholesterol = it },
            label = { Text("Total cholesterol (mg/dL) — optional") }, modifier = Modifier.fillMaxWidth())

        HorizontalDivider()

        Text("Lifestyle", fontSize = 16.sp, fontWeight = FontWeight.Medium)

        Text("Physical activity", fontSize = 14.sp, color = Color.DarkGray)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("sedentary", "light", "active").forEach { option ->
                FilterChip(selected = activity == option, onClick = { activity = option },
                    label = { Text(option) })
            }
        }

        Text("Diet quality", fontSize = 14.sp, color = Color.DarkGray)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("poor", "average", "good").forEach { option ->
                FilterChip(selected = diet == option, onClick = { diet = option },
                    label = { Text(option) })
            }
        }

        Text("Gestational diabetes history", fontSize = 14.sp, color = Color.DarkGray)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("na", "no", "yes").forEach { option ->
                FilterChip(selected = gestational == option, onClick = { gestational = option },
                    label = { Text(option) })
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Checkbox(checked = familyHist, onCheckedChange = { familyHist = it })
                Text("Family history of diabetes")
            }
        }

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Checkbox(checked = smoking, onCheckedChange = { smoking = it })
            Text("Smoker")
        }

        if (error.isNotEmpty()) {
            Text(error, color = Color.Red, fontSize = 13.sp)
        }

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(8.dp))
        }

        result?.let { res ->
            val riskLevel = res.optString("risk_level", "")
            val bgColor = when (riskLevel) {
                "high"   -> Color(0xFFFFCDD2)
                "medium" -> Color(0xFFFFF9C4)
                else     -> Color(0xFFC8E6C9)
            }

            Card(modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = bgColor)) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Text(res.optString("label", ""), fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Score: ${res.optInt("score")} / ${res.optInt("max_score")}", fontSize = 14.sp)

                    Text("Risk Factors:", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    val factors = res.optJSONArray("factors")
                    if (factors != null) {
                        for (i in 0 until factors.length()) {
                            Text("• ${factors.getString(i)}", fontSize = 13.sp)
                        }
                    }

                    Text("Recommendations:", fontWeight = FontWeight.Medium, fontSize = 14.sp)
                    val recs = res.optJSONArray("recommendations")
                    if (recs != null) {
                        for (i in 0 until recs.length()) {
                            Text("• ${recs.getString(i)}", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (bmi.isEmpty() || glucose.isEmpty()) {
                    error = "Please fill in BMI and Glucose"
                    return@Button
                }
                error = ""
                isLoading = true
                sendPrediction(
                    context = context,
                    name = name, age = age, gender = gender,
                    bmi = bmi, glucose = glucose, bp = bp,
                    cholesterol = cholesterol, activity = activity,
                    diet = diet, smoking = smoking,
                    familyHist = familyHist, gestational = gestational,
                    onResult = { json -> result = json; isLoading = false },
                    onError  = { msg -> error = msg; isLoading = false }
                )
            },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E))
        ) {
            Text("Predict Risk", fontSize = 16.sp, color = Color.White)
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text("Back")
            }
            OutlinedButton(onClick = onHistory, modifier = Modifier.weight(1f).padding(start = 8.dp)) {
                Text("View History")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

fun sendPrediction(
    context: Context,
    name: String, age: String, gender: String,
    bmi: String, glucose: String, bp: String,
    cholesterol: String, activity: String, diet: String,
    smoking: Boolean, familyHist: Boolean, gestational: String,
    onResult: (JSONObject) -> Unit,
    onError: (String) -> Unit
) {
    val url = "http://10.0.2.2:3000/predict"

    val body = JSONObject().apply {
        put("name",           name)
        put("gender",         gender)
        put("age",            age.toIntOrNull() ?: 0)
        put("bmi",            bmi.toDoubleOrNull() ?: 0.0)
        put("glucose",        glucose.toIntOrNull() ?: 0)
        put("bp",             bp.toIntOrNull() ?: 120)
        put("cholesterol",    cholesterol.toIntOrNull() ?: 180)
        put("family_history", if (familyHist) "yes" else "no")
        put("activity",       activity)
        put("diet",           diet)
        put("smoking",        if (smoking) "yes" else "no")
        put("gestational",    gestational)
    }

    val request = JsonObjectRequest(Request.Method.POST, url, body,
        { response -> onResult(response) },
        { error    -> onError("Server error: ${error.message}") }
    )

    Volley.newRequestQueue(context).add(request)
}