
buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath(libs.google.services)
        classpath(libs.navigation.safe.args.gradle.plugin)
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.com.android.application) apply false
    alias(libs.plugins.org.jetbrains.kotlin.android) apply false
    alias(libs.plugins.com.google.android.libraries.mapsplatform.secrets.gradle.plugin) apply false

//    alias(libs.plugins.com.google.android.gradle) apply false
//    id 'com.google.android.libraries.mapsplatform.secrets-gradle-plugin' version '2.0.1' apply false
}