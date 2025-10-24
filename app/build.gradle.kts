plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.botoninterfaz"
    compileSdk = 34  // Corregido: 36 no está disponible aún

    defaultConfig {
        applicationId = "com.example.botoninterfaz"
        minSdk = 30  // Wear OS 3.0+
        targetSdk = 34  // Corregido para compatibilidad
        versionCode = 1
        versionName = "1.0"

        // Configuración específica para Wear OS
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    buildFeatures {
        viewBinding = true
    }
    
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Dependencias principales de Wear OS
    implementation(libs.play.services.wearable)
    
    // Librerías de soporte para Android
    implementation("androidx.core:core:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime:2.7.0")
    
    // Wear OS UI Components
    implementation("androidx.wear:wear:1.3.0")
    
    // Utilidades adicionales
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    
    // Testing (opcionales)
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}