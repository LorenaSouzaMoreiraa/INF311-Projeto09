import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.inf311_projeto09"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.inf311_projeto09"
        minSdk = 29
        targetSdk = 35
        versionCode = 2
        versionName = "1.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { input ->
                localProperties.load(input)
            }
        }

        buildConfigField(
            "String",
            "RUBEUS_API_BASE_URL",
            "\"${localProperties.getProperty("rubeus.api.base-url")}\""
        )
        buildConfigField(
            "String",
            "RUBEUS_API_ORIGIN",
            "\"${localProperties.getProperty("rubeus.api.origin")}\""
        )
        buildConfigField(
            "String",
            "RUBEUS_API_TOKEN",
            "\"${localProperties.getProperty("rubeus.api.token")}\""
        )
        buildConfigField(
            "String",
            "IMGBB_API_KEY",
            "\"${localProperties.getProperty("imgbb.api.key")}\""
        )
        buildConfigField(
            "String",
            "MAPS_API_KEY",
            "\"${localProperties.getProperty("google.maps.api.key")}\""
        )
        resValue("string", "google_maps_key", localProperties.getProperty("google.maps.api.key"))
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

    buildFeatures {
        compose = true
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.ui)
    implementation(libs.ui.tooling)
    implementation(libs.material3)
    implementation(libs.runtime)
    implementation(libs.activity.compose)
    implementation(libs.core.ktx)
    implementation(libs.material.icons.extended)
    implementation(libs.lucide.icons)
    implementation(libs.navigation.compose)
    implementation(libs.camera.camera2)
    implementation(libs.camera.lifecycle)
    implementation(libs.camera.view)
    implementation(libs.camera.extensions)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.barcode.scanning)
    implementation(libs.camera.core)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.core)
    implementation(libs.zxing.android.embedded)
    implementation(libs.bcrypt)
    implementation(libs.coil.compose)
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}