plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
    id("androidx.room") version "2.7.0-alpha04"
    kotlin("plugin.serialization") version "1.5.31"
}

android {
    namespace = "com.squalec.liftingtracker.android"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.squalec.liftingtracker.android"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}
dependencies {
    implementation(libs.androidx.room.runtime)

    implementation(projects.shared)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.androidx.activity.compose)

    implementation(libs.koin.android)
    implementation(libs.koin.core)

    implementation(libs.timber)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")
    implementation("com.rickclephas.kmm:kmm-viewmodel-core:1.0.0-ALPHA-17")

    implementation("com.google.accompanist:accompanist-flowlayout:0.26.2-beta")
    implementation(libs.androidx.compose.material)

    debugImplementation(libs.compose.ui.tooling)



    implementation(project(":shared"))

}
//Room step3: path where we want to generate the schemas
room {
    schemaDirectory("$projectDir/schemas")
}
