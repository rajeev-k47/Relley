import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "net.runner.relley"
    compileSdk = 35
    buildFeatures{
        buildConfig =true
    }

    defaultConfig {
        applicationId = "net.runner.relley"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        val properties =  Properties()
        properties.load(project.rootProject.file("apikeys.properties").inputStream())
        buildConfigField("String","GITHUB_CLIENT_ID","\"${properties.getProperty("GITHUB_CLIENT_ID","")}\"")
        buildConfigField("String","GITHUB_CLIENT_SECRET","\"${properties.getProperty("GITHUB_CLIENT_SECRET","")}\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val properties =  Properties()
            properties.load(project.rootProject.file("apikeys.properties").inputStream())
            buildConfigField("String","GITHUB_CLIENT_ID","\"${properties.getProperty("GITHUB_CLIENT_ID","")}\"")
            buildConfigField("String","GITHUB_CLIENT_SECRET","\"${properties.getProperty("GITHUB_CLIENT_SECRET","")}\"")

        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.cio) // CIO engine for network operations
    implementation(libs.ktor.client.json) // JSON feature
    implementation(libs.ktor.client.serialization) // Serialization

    implementation(libs.gson)
    implementation(libs.bottombar)
        implementation(libs.coil.compose)


}