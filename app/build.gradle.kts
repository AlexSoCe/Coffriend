import org.jetbrains.dokka.gradle.DokkaTask

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.dokka)
}

android {
    namespace = "com.alexsotocepas.coffriend"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.alexsotocepas.coffriend"
        minSdk = 24
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
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.runtime)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.core.splashscreen)
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")
    implementation("com.google.android.material:material:1.13.0")
}

tasks.withType<DokkaTask>().configureEach {
    // Definim la sortida en una carpeta que vegis clarament
    outputDirectory.set(file("${projectDir}/docs/javadoc"))

    dokkaSourceSets {
        // En lloc de configurar per nom (que falla), configurem el set de "main" manualment
        register("main") {
            // Ruta directa a la teva carpeta de codi
            sourceRoots.from(file("src/main/java"))

            // Si tens fitxers a src/main/kotlin, afegeix aquesta línia també:
            // sourceRoots.from(file("src/main/kotlin"))

            // Configuració bàsica per evitar errors de contenidor
            includeNonPublic.set(false)
            skipEmptyPackages.set(false)
            reportUndocumented.set(true)
        }
    }
}