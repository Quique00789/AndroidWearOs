plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.devicesynccounter.mobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.devicesynccounter"
        minSdk = 30
        targetSdk = 34
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Dependencias básicas de Android usando el catálogo disponible
    implementation(libs.androidx.core)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.constraintlayout)
    
    // Data Layer API - Esencial para comunicación con Wear OS
    implementation(libs.play.services.wearable)
    
    // Dependencias adicionales necesarias para mobile app
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.activity:activity:1.8.0")
    
    // Testing usando el catálogo disponible
    testImplementation(libs.junit)    
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.espresso.core)
}