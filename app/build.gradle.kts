plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.mariabuliga.softpaws"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.mariabuliga.softpaws"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "API_KEY",
            "\"live_VFOBdY44p96tpzjopdUcyK1Ucl15kVWAoFEzDn0d7C3mFVTTejzUU6QJvlzJfwZd\""
        )
        buildConfigField("String", "BASE_URL", "\"https://thecatapi.com/\"")
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
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Lifecycle
    val lifecycle_version = "2.9.4"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${lifecycle_version}")
    // LiveData
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:${lifecycle_version}")
    // Saved state module for ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:${lifecycle_version}")
    // Annotation processor
    ksp("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")

    // Room
    val room_version = "2.8.2"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:${room_version}")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")

    // Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.57.1")
    ksp("com.google.dagger:hilt-android-compiler:2.57.1")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // Navigation
    val nav_version = "2.9.5"
    implementation("androidx.navigation:navigation-fragment:${nav_version}")
    implementation("androidx.navigation:navigation-ui:${nav_version}")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")

}