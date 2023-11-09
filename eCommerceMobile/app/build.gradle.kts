plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("androidx.navigation.safeargs")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.ecommercemobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ecommercemobile"
        minSdk = 26
        targetSdk = 33
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //MATERIAL
    implementation ("com.google.android.material:material:1.10.0")

    //RETROFIT
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //NAVIGATION
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.4")

    //SERIALITZATION
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")

    //DAGGER HILT
    implementation("com.google.dagger:hilt-android:2.48")
    kapt("com.google.dagger:hilt-android-compiler:2.48")

    // Circular ImageView
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    // Loading button
    implementation("com.github.leandroborgesferreira:loading-button-android:2.3.0")
    // Resend
    implementation ("com.resend:resend-java:2.1.0")
    //GLIDE
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    kapt ("com.github.bumptech.glide:compiler:4.12.0")

}
// Allow references to generated code
kapt {
    correctErrorTypes = true
}