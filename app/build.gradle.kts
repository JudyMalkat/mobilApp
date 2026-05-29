@file:Suppress("UnstableApiUsage")

import java.io.FileInputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Properties

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm_a")
val currentTime: String = LocalDateTime.now().format(formatter)

plugins {
    alias(libs.plugins.jetpack.application)
    alias(libs.plugins.jetpack.dagger.hilt)
    //alias(libs.plugins.jetpack.firebase)
    alias(libs.plugins.jetpack.dokka)
}

android {
    val majorUpdateVersion = 1
    val minorUpdateVersion = 2
    val patchVersion = 7

    val mVersionCode = majorUpdateVersion.times(10_000)
        .plus(minorUpdateVersion.times(100))
        .plus(patchVersion)

    val mVersionName = "$majorUpdateVersion.$minorUpdateVersion.$patchVersion"

    defaultConfig {
        versionCode = mVersionCode
        versionName = mVersionName
        
        // 🚀 الضربة الأولى: تغيير هوية التطبيق لخداع الهاتف
        applicationId = "com.monep.azkarfinal" 
    }

    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                val keystoreProperties = Properties()
                keystoreProperties.load(FileInputStream(keystorePropertiesFile))
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            isMinifyEnabled = true
            signingConfig = if (keystorePropertiesFile.exists()) {
                signingConfigs.getByName("release")
            } else {
                println("keystore.properties file not found. Using debug key.")
                signingConfigs.getByName("debug")
            }
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }

    androidResources {
        generateLocaleConfig = true
    }

    // أبقينا هذا كما هو حتى لا تخرب مسارات الكود
    namespace = "dev.atick.compose"
}

androidComponents {
    onVariants { variant ->
        variant.outputs.forEach { output ->
            output.versionName.set("${variant.outputs.first().versionName.getOrElse("1.0.0")}")
        }
    }
}

dependencies {
    // ... Core
    implementation(project(":core:ui"))
    implementation(project(":core:network"))
    implementation(project(":core:preferences"))
    implementation("androidx.core:core-splashscreen:1.2.0")
    // ... Features
    //implementation(project(":feature:auth"))
    //implementation(project(":feature:home"))
    //implementation(project(":feature:profile"))
    //implementation(project(":feature:settings"))
// Retrofit & Gson (لجلب أوقات الصلاة من الإنترنت)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // 🛑 الضربة الثانية: إيقاف كل مكاتب الخلفية التي تسبب الانهيار (تم تحويلها لتعليقات)
    // implementation(project(":firebase:analytics"))
    // implementation(project(":sync"))

    // ... Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // ... OSS Licenses
    implementation(libs.google.oss.licenses)

    // 🛑 إيقاف مكتبة تتبع الذاكرة المزعجة
    // debugImplementation(libs.leakcanary.android)
}