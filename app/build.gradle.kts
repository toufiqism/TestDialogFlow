plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.testdialogflow"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.testdialogflow"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += listOf(
                "META-INF/INDEX.LIST",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt"
            )
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- Networking (The Data Layer) ---
    // Retrofit: Type-safe HTTP client
    implementation(libs.retrofit)
    // GSON Converter: For serializing JSON to Data Classes
    implementation(libs.converter.gson)
    // OkHttp Logging Interceptor: Crucial for debugging network calls
    implementation(libs.logging.interceptor)

    // ViewModel integration with Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

    // --- Concurrency (The Execution Model) ---
    // Coroutines for async operations
    implementation(libs.kotlinx.coroutines.android)

    // Google Auth Library for service account authentication
    implementation(libs.google.auth.library.oauth2.http)

    //more icons
    implementation (libs.androidx.material.icons.extended)

    // DataStore for preferences
    implementation(libs.androidx.datastore.preferences)

    // Navigation 3
    implementation(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
    implementation("androidx.lifecycle:lifecycle-viewmodel-navigation3:2.10.0")
    
    // Kotlin Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0")
}