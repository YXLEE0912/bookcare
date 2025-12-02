plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.bookcare"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.bookcare"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Basic Android - COMPATIBLE WITH SDK 33
    implementation("androidx.core:core-ktx:1.10.1")        // ← COMPATIBLE VERSION
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // RecyclerView for displaying posts
    implementation("androidx.recyclerview:recyclerview:1.3.1")

    // Glide for image loading
    implementation("com.github.bumptech.glide:glide:4.15.1")

    // Firebase BOM (COMPATIBLE VERSION)
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))  // ← OLDER BUT STABLE

    // Firebase dependencies
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-database-ktx")
    implementation(libs.firebase.database)
}