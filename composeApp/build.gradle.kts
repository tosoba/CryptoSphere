import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
}

kotlin {
  androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }

  sourceSets {
    androidMain.dependencies {
      implementation(compose.preview)

      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.constraintLayout.compose)
      implementation(libs.coil.compose)
      implementation(libs.decompose.extensions.compose)
      implementation(libs.decompose.extensions.compose.experimental)
      implementation(libs.kotlinx.coroutines.android)
    }

    commonMain.dependencies {
      implementation(projects.shared)

      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.materialIconsExtended)
      implementation(compose.runtime)
      implementation(compose.ui)

      implementation(libs.androidx.lifecycle.runtimeCompose)
    }

    commonTest.dependencies { implementation(libs.kotlin.test) }
  }
}

android {
  namespace = "com.trm.cryptosphere"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.trm.cryptosphere"
    minSdk = libs.versions.android.minSdk.get().toInt()
    targetSdk = libs.versions.android.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
  }

  packaging { resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" } }
  buildTypes { getByName("release") { isMinifyEnabled = false } }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
}

dependencies { debugImplementation(compose.uiTooling) }
