plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.priyanshu.resumegenerator"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.priyanshu.resumegenerator"
        minSdk = 29
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.appcompat.v161)
    implementation(libs.material.v1110)
    implementation(libs.constraintlayout.v214)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.play.services.location)
    implementation(libs.colorpickerview)
    implementation (libs.colorpickerview.v224)
    implementation(libs.colorpickerview)
    implementation(libs.ambilwarna)
}