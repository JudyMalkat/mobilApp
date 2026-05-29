package dev.atick.compose.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource // إضافة مهمة لاستدعاء النصوص
import dev.atick.compose.AzkarScreen
import dev.atick.compose.PrayerTimesScreen
import dev.atick.compose.R // استدعاء ملف الموارد الخاص بمشروعك

@Composable
fun JetpackApp() {
    var currentScreen by remember { mutableStateOf("azkar") }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                NavigationBarItem(
                    // تم جلب النص من strings.xml
                    icon = { Icon(Icons.Default.List, contentDescription = stringResource(R.string.nav_azkar)) },
                    label = { Text(stringResource(R.string.nav_azkar)) },
                    selected = currentScreen == "azkar",
                    onClick = { currentScreen = "azkar" }
                )
                NavigationBarItem(
                    // تم جلب النص من strings.xml
                    icon = { Icon(Icons.Default.Notifications, contentDescription = stringResource(R.string.nav_prayer)) },
                    label = { Text(stringResource(R.string.nav_prayer)) },
                    selected = currentScreen == "prayer",
                    onClick = { currentScreen = "prayer" }
                )
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (currentScreen) {
                "azkar" -> AzkarScreen()
                "prayer" -> PrayerTimesScreen()
            }
        }
    }
}