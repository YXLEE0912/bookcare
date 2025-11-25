// Top-level build.gradle file
buildscript {
    repositories {
        google()  // ✅ ADD THIS LINE
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.1.0") // Use stable version
        classpath("com.google.gms:google-services:4.4.0") // Use stable version
        classpath(kotlin("gradle-plugin", version = "1.9.10"))
    }
}

allprojects {
    repositories {
        google()  // ✅ ADD THIS LINE
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}