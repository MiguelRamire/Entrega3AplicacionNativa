plugins {
    alias(libs.plugins.android.application)
<<<<<<< HEAD
    alias(libs.plugins.kotlin.compose)
=======
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.services)
>>>>>>> origin/main
}

android {
    namespace = "com.example.petshield"
<<<<<<< HEAD
    compileSdk {
        version = release(37)
    }
=======
    compileSdk = 35
>>>>>>> origin/main

    defaultConfig {
        applicationId = "com.example.petshield"
        minSdk = 24
<<<<<<< HEAD
        targetSdk = 37
=======
        targetSdk = 35
>>>>>>> origin/main
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
<<<<<<< HEAD
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
=======
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
>>>>>>> origin/main
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
<<<<<<< HEAD
=======
    kotlinOptions {
        jvmTarget = "11"
    }
>>>>>>> origin/main
    buildFeatures {
        compose = true
    }
}

dependencies {
<<<<<<< HEAD
=======
    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.android.gms:play-services-auth:21.2.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    // Compose & Lifecycle
>>>>>>> origin/main
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation("androidx.compose.material:material-icons-extended")
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
<<<<<<< HEAD
=======
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")
>>>>>>> origin/main
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/main
