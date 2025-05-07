plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "hu.unideb.inf.smartrecipe"
    compileSdk = 35

    defaultConfig {
        applicationId = "hu.unideb.inf.smartrecipe"
        minSdk = 27
        targetSdk = 35
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
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.github.glide)
    implementation(libs.room.runtime)
    implementation(libs.material.v150)
    implementation(libs.coordinatorlayout)
    implementation(libs.cardview)

    annotationProcessor(libs.room.compiler)

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

}