plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.chunbae.mypetdiary"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chunbae.mypetdiary"
        minSdk = 24
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
    viewBinding{
        enable = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    testImplementation("junit:junit:4.13.2")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1")
    testImplementation ("com.google.truth:truth:1.0.1")
    testImplementation ("androidx.test.ext:junit:1.1.2")
    testImplementation ("org.robolectric:robolectric:4.4")

    androidTestImplementation ("junit:junit:4.13")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.2.1")
    androidTestImplementation ("androidx.room:room-testing:2.2.5")
    androidTestImplementation ("com.google.truth:truth:1.0.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    /*room database*/
    var room_version = "2.5.0"
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    var coroutineVersion = "1.1.0"
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion")

    /*photo picker*/
     implementation("androidx.activity:activity-ktx:1.8.0")


    var calendarVersion = "1.0.4"
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:<latest-version>")
    //캘린더 라이브러리 관련. https://github.com/kizitonwose/Calendar
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs_nio:2.0.2")

    // The view calendar library
    implementation("com.github.prolificinteractive:material-calendarview:2.0.1")


    //동그란 이미지를 위한 lib
    implementation("de.hdodenhof:circleimageview:3.1.0")
    //이미지 crop
    implementation("com.theartofdev.edmodo:android-image-cropper:2.8.+")
//    api ("com.theartofdev.edmodo:android-image-cropper:2.8.0")

    //자동 개행 layout
    // https://mvnrepository.com/artifact/com.google.android/flexbox
//    implementation("com.google.android:flexbox:1.0.0")
        implementation("com.google.android.flexbox:flexbox:3.0.0")
}