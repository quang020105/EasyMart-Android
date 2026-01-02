plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id ("kotlin-kapt")               // cần cho annotation processing (kapt)
    id ("com.google.dagger.hilt.android") // áp dụng plugin Hilt cho module app
    //id ("com.google.devtools.ksp")       // <-- bật KSP cho module này
}

android {
    namespace = "com.example.easymart"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.easymart"
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.compose.material)
    //coil
    implementation(libs.coil.compose)

    //navigation
    implementation (libs.androidx.navigation.compose)
    implementation (libs.androidx.hilt.navigation.compose)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //moshi
    implementation(libs.moshi.kotlin)
    implementation(libs.moshi.adapters)
    implementation(libs.converter.moshi)

    // Networking
    implementation(libs.logging.interceptor)

    //hilt
    implementation (libs.hilt.android)
    kapt (libs.dagger.hilt.compiler)
    kapt (libs.androidx.hilt.compiler)   // hoặc version phù hợp bạn đang dùng

    //room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.androidx.room.compiler)

    //tìm kiếm nhanh bằng algolia
    implementation(libs.ktor.client.okhttp)
    implementation(libs.algoliasearch.client.kotlin.jvm)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.cio)

    //foundation
    implementation(libs.androidx.foundation)

    //icons material3
    implementation(libs.androidx.compose.material.icons.extended)
}