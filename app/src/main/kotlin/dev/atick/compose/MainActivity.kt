package dev.atick.compose

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import dev.atick.compose.ui.AuthScreen
import dev.atick.compose.ui.JetpackApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // 1. تشغيل شاشة الترحيب قبل أي شيء آخر
        installSplashScreen()
        
        super.onCreate(savedInstanceState)
        
        setContent {
            val context = LocalContext.current
            val sharedPreferences = remember { context.getSharedPreferences("UserData", Context.MODE_PRIVATE) }
            var loginState by remember { mutableStateOf(sharedPreferences.getBoolean("isLoggedIn", false)) }

            val colorScheme = if (isSystemInDarkTheme()) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = colorScheme) {
                if (!loginState) {
                    AuthScreen(onLoginSuccess = { loginState = true })
                } else {
                    JetpackApp()
                }
            }
        }
    }
}