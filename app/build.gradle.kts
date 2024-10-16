import com.android.build.api.dsl.Packaging
import org.jetbrains.kotlin.util.capitalizeDecapitalize.toLowerCaseAsciiOnly
import java.io.FileInputStream
import java.util.Properties
import kotlin.script.experimental.api.ScriptCompilationConfiguration.Default.properties

plugins {
    jacoco
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.ktfmt)
    alias(libs.plugins.gms)
    alias(libs.plugins.sonar)
}





android {
    namespace = "com.github.se.signify"
    compileSdk = 34


    // Load the API key from local.properties
    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localProperties.load(FileInputStream(localPropertiesFile))
    }

    //val mapsApiKey: String = localProperties.getProperty("MAPS_API_KEY") ?: ""


    defaultConfig {
        applicationId = "com.github.se.signify"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        //manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a"))
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            enableUnitTestCoverage = true
            enableAndroidTestCoverage = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
        }
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true

            isReturnDefaultValues = true
        }
        packagingOptions {
            jniLibs {
                useLegacyPackaging = true
            }
        }
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    // Robolectric needs to be run only in debug. But its tests are placed in the shared source set (test)
    // The next lines transfers the src/test/* from shared to the testDebug one
    //
    // This prevent errors from occurring during unit tests
    sourceSets.getByName("testDebug") {
        val test = sourceSets.getByName("test")

        java.setSrcDirs(test.java.srcDirs)
        res.setSrcDirs(test.res.srcDirs)
        resources.setSrcDirs(test.resources.srcDirs)
    }

    sourceSets.getByName("test") {
        java.setSrcDirs(emptyList<File>())
        res.setSrcDirs(emptyList<File>())
        resources.setSrcDirs(emptyList<File>())
    }


}


dependencies {

    // Core
    implementation(libs.core.ktx)
    implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.lifecycle.runtime.ktx)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
            implementation(libs.androidx.constraintlayout)
            implementation(libs.androidx.fragment.ktx)
            implementation(libs.kotlinx.serialization.json)

            // Jetpack Compose UI
            implementation(libs.androidx.ui)
            implementation(libs.androidx.ui.tooling.preview)
            implementation(libs.androidx.ui.graphics)
            implementation(libs.androidx.material)
            implementation(libs.androidx.material3)
            implementation(libs.androidx.navigation.compose)
            implementation(platform(libs.androidx.compose.bom))
            testImplementation(libs.test.core.ktx)
            debugImplementation(libs.androidx.ui.tooling)
            debugImplementation(libs.androidx.ui.test.manifest)
            implementation(libs.material)
            implementation(libs.coil.compose)


            // Navigation
            implementation(libs.androidx.navigation.compose)
            implementation(libs.androidx.navigation.fragment.ktx)
            implementation(libs.androidx.navigation.ui.ktx)

            // Google Service and Maps
            implementation(libs.play.services.maps)
            implementation(libs.maps.compose)
            implementation(libs.maps.compose.utils)
            implementation(libs.play.services.auth)

            // Firebase
            implementation(libs.firebase.database.ktx)
            implementation(libs.firebase.firestore)
            implementation(libs.firebase.ui.auth)
            implementation(libs.firebase.auth.ktx)
            implementation(libs.firebase.auth)

            // Networking with OkHttp
            implementation(libs.okhttp)
            // MediaPipe Hands solution
            implementation("com.google.mediapipe:tasks-vision:0.20230731")
            // CameraX dependencies for accessing the phone's camera
            implementation("androidx.camera:camera-core:1.3.0")
            implementation("androidx.camera:camera-camera2:1.3.0")
            implementation("androidx.camera:camera-lifecycle:1.3.0")
            implementation("androidx.camera:camera-view:1.3.0")
    // Microsoft onnx
    implementation ("com.microsoft.onnxruntime:onnxruntime-android:1.15.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
            // Testing Unit
            testImplementation(libs.junit)
            androidTestImplementation(libs.mockk)
            androidTestImplementation(libs.mockk.android)
            androidTestImplementation(libs.mockk.agent)
            testImplementation(libs.json)

            // Test UI
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
            androidTestImplementation(libs.androidx.espresso.intents)
            androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(platform(libs.androidx.compose.bom))
            testImplementation(libs.mockito.core)
            testImplementation(libs.mockito.inline)
            testImplementation(libs.mockito.kotlin)
            androidTestImplementation(libs.mockito.android)
            androidTestImplementation(libs.mockito.kotlin)
            testImplementation(libs.robolectric)
    androidTestImplementation(libs.kaspresso)
    androidTestImplementation(libs.kaspresso.allure.support)
    androidTestImplementation(libs.kaspresso.compose.support)

            testImplementation(libs.kotlinx.coroutines.test)

}



tasks.withType<Test> {
    // Configure Jacoco for each tests
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register("jacocoTestReport", JacocoReport::class) {
    mustRunAfter("testDebugUnitTest", "connectedDebugAndroidTest")

    reports {
        xml.required = true
        html.required = true
    }

    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/sigchecks/**",
    )
    val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(project.buildDir) {
        include("outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec")
        include("outputs/code_coverage/debugAndroidTest/connected/*/coverage.ec")
    })

}

sonar {
    properties {
        property ("sonar.projectKey", "Signify-epfl_signify-app")
        property ("sonar.organization", "signify-epfl")
        property ("sonar.projectName", "signify-app")
        property ("sonar.host.url", "https://sonarcloud.io")
        property ("sonar.androidLint.reportPaths", "${project.layout.buildDirectory.get()}/reports/lint-results-debug.xml")
        property ("sonar.jacoco.reportPaths", "build/reports/jacoco/jacocoTestReport.xml")
        property ("sonar.coverage.jacoco.xmlReportPaths", "${project.layout.buildDirectory.get()}/reports/lint-results-debug.xml$")
        property ("sonar.junit.ReportPaths", "${project.layout.buildDirectory.get()}/test-results/testDebugUnitTest/")
    }
}







