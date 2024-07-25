plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.projectbewise"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.projectbewise"
        minSdk = 28
        targetSdk = 34
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("com.google.android.gms:play-services-maps:18.0.2")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")
    implementation("com.google.android.material:material:1.3.0")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("com.google.firebase:firebase-auth-ktx:21.0.1")
    implementation("com.google.firebase:firebase-storage-ktx:19.2.2")
    implementation("com.google.firebase:firebase-firestore-ktx:24.0.0") // Firestore KTX dependency
    implementation("com.github.bumptech.glide:glide:4.12.0") // Glide for image loading
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0") // Glide compiler
    implementation ("de.hdodenhof:circleimageview:3.1.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.play.services.maps)
    implementation(libs.androidx.coordinatorlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
