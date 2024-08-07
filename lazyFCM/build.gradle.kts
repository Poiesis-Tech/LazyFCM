/*
 * **************************************************************
 *  * @Author: Tariq Hussain
 *  * @Date: 3/7/24, 1:37 AM.
 *  * @Accounts
 *  *      -> https://github.com/Tariq2518
 *  *      -> https://www.linkedin.com/in/tariqhpk/
 *  *
 *  * All rights reserved.
 *  * Copying and redistributing are not allowed.
 *  **************************************************************
 */
import org.gradle.api.tasks.bundling.Jar

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    `maven-publish`
}



android {
    namespace = "com.poiesistech.lazyfcm"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
    implementation("com.google.firebase:firebase-messaging")

    // Glide Library for imageview
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

}


//publishing {
//    publications {
//        create<MavenPublication>("release") {
//            groupId = "com.poiesistech.lazyfcm"
//            artifactId = "lazyFcm"
//            version = "1.3"
//            from(components.find { it.name == "release" || it.name == "java" })
//        }
//    }
//}

afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            create<MavenPublication>("release") {
                groupId = "com.poiesistech.lazyfcm"
                artifactId = "lazyFcm"
                version = "1.3"
                from(components.find { it.name == "release" || it.name == "java" })
            }
        }
    }
}

