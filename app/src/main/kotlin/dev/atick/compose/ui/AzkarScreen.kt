package dev.atick.compose // تأكد أن هذا يطابق مسار حزمتك

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.atick.compose.R

@Composable
fun AzkarScreen() {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.azkar_title),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            ZikrCard(
                title = stringResource(R.string.azkar_morning_title),
                text = stringResource(R.string.azkar_morning_text),
                targetCount = 3
            )
        }
        
        item {
            ZikrCard(
                title = stringResource(R.string.azkar_evening_title),
                text = stringResource(R.string.azkar_evening_text),
                targetCount = 3
            )
        }

        item {
            ZikrCard(
                title = stringResource(R.string.azkar_friday_title),
                text = stringResource(R.string.azkar_friday_text),
                targetCount = 1
            )
        }
    }
}

@Composable
fun ZikrCard(title: String, text: String, targetCount: Int) {
    var count by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { if (count < targetCount) count++ },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = text, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(onClick = { if (count < targetCount) count++ }) {
                Text(
                    text = if (count < targetCount) 
                        stringResource(R.string.azkar_button_progress, count, targetCount)
                    else 
                        stringResource(R.string.azkar_button_done)
                )
            }
        }
    }
}