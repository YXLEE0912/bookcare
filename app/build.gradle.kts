plugins {
<<<<<<< HEAD
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.bookcare_marketplace"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.bookcare_marketplace"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
=======
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.bookcare"
    compileSdk = 33  // ⬇️ DOWNGRADE to 33 (more stable)

    defaultConfig {
        applicationId = "com.example.bookcare"
        minSdk = 24
        targetSdk = 33  // ⬇️ DOWNGRADE to 33
        versionCode = 1
        versionName = "1.0"
>>>>>>> 63228a2d8bbe1d61bbf25d77e157d602bc5a0005
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
<<<<<<< HEAD
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
=======

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8  // ⬇️ Use Java 8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
>>>>>>> 63228a2d8bbe1d61bbf25d77e157d602bc5a0005
    }
}

dependencies {
<<<<<<< HEAD
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.5")
// Optional, but good practice
=======
    // Basic Android
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Firebase BOM (LATEST STABLE)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
>>>>>>> 63228a2d8bbe1d61bbf25d77e157d602bc5a0005
}