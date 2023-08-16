import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.ryanblignaut.featherfinder"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ryanblignaut.featherfinder"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        manifestPlaceholders.putIfAbsent("MAPS_API_KEY", properties.getProperty("MAPS_API_KEY"))
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
        viewBinding = true
    }
    testOptions {
        unitTests.isReturnDefaultValues = true
    }

}

dependencies {

    implementation(libs.play.services.maps)
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.preference)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    implementation(libs.play.services.location)
    implementation(libs.firebase.auth.ktx)
//    implementation("org.testng:testng:6.9.6")
//    testImplementation(libs.testng)
//    implementation(libs.jetbrains.kotlinx.coroutines.test)
//    implementation(libs.junit)
//    androidTestImplementation(libs.androidx.test.ext.junit)
//    androidTestImplementation(libs.espresso.core)
    implementation(libs.kotlinx.coroutines.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.junit)
    androidTestImplementation(libs.junit)

    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.android.maps.utils)


//    testImplementation(libs.kotlinx.coroutines.test)
//    testImplementation(libs.junit)
//    implementation(libs.kotlinx.coroutines.android)


}