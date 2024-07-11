

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("org.jetbrains.kotlin.native.cocoapods") // For CocoaPods support
    id("androidx.room") version "2.7.0-alpha04"
    id("com.google.devtools.ksp") version "2.0.0-1.0.21"
    kotlin("plugin.serialization") version "1.5.31"
}

kotlin {
    androidTarget {
    }


    iosArm64()
    iosX64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Shared module for KMP"
        homepage = "Link to a Kotlin/Native module homepage"
        ios.deploymentTarget = "14.1"
        framework {
            baseName = "shared"
        }
        podfile = project.file("../iosApp/Podfile")

    }
    sourceSets.commonMain{
        kotlin.srcDirs("build/generated/ksp/metadata")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.androidx.room.runtime)
            implementation("androidx.sqlite:sqlite-bundled:2.5.0-SNAPSHOT")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
            implementation(libs.koin.core)
            implementation(libs.koin.test)
            //put your multiplatform dependencies here
        }
        iosMain.dependencies {
            //put your multiplatform dependencies here
        }
        androidMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0")
            implementation("androidx.room:room-ktx:2.7.0-alpha04")
//            implementation(project(":workoutmanagement"))
            //put your multiplatform dependencies here
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.squalec.liftingtracker"
    compileSdk = 34
    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

//Room step3: path where we want to generate the schemas
room {
    schemaDirectory("$projectDir/schemas")
}

//Room step5  KSP For processing Room annotations , Otherwise we will get Is Room annotation processor correctly configured? error
dependencies {

    // Update: https://issuetracker.google.com/u/0/issues/342905180
    add("kspCommonMainMetadata", "androidx.room:room-compiler:2.7.0-alpha04")

}

//Room step6 part 2 make all source sets to depend on kspCommonMainKotlinMetadata:  Update: https://issuetracker.google.com/u/0/issues/342905180
tasks.withType<org.jetbrains.kotlin.gradle.dsl.KotlinCompile<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
