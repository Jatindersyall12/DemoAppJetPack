plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.demoapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.demoapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.compose.ui:ui:1.5.0") // Jetpack Compose UI
    implementation ("androidx.compose.material:material:1.5.0") // Jetpack Compose Material Components
    implementation ("androidx.compose.foundation:foundation:1.5.0") // Jetpack Compose Foundation
    implementation ("androidx.compose.runtime:runtime:1.5.0") // Jetpack Compose Runtime
    implementation ("androidx.compose.material:material-icons-extended:1.5.0") // Jetpack Compose Material Icons
    implementation ("androidx.navigation:navigation-compose:2.6.0") // Jetpack Compose Navigation

    // Accompanist Pager for Image Carousel
    implementation ("com.google.accompanist:accompanist-pager:0.28.0") // Accompanist Pager
    implementation ("com.google.accompanist:accompanist-pager-indicators:0.28.0") // Accompanist Pager Indicators

}