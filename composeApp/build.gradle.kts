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
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.compose.material3.adaptiveNavigationSuite)
      implementation(libs.androidx.constraintLayout.compose)
      implementation(libs.androidx.lifecycle.process)
      implementation(libs.androidx.paging.compose)
      implementation(libs.androidx.startup.runtime)

      implementation(libs.compose.material.icons.extended)
      implementation(libs.compose.ui.tooling.preview)

      implementation(libs.decompose.extensions.compose)
      implementation(libs.decompose.extensions.compose.experimental)

      implementation(libs.kotlinx.coroutines.android)

      implementation(libs.material.kolor)
    }

    commonMain.dependencies {
      implementation(projects.shared)

      implementation(libs.androidx.lifecycle.runtimeCompose)

      implementation(libs.compose.components.resources)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material.icons.core)
      implementation(libs.compose.material3)
      implementation(libs.compose.runtime)
      implementation(libs.compose.ui)
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

dependencies { debugImplementation(libs.compose.ui.tooling) }
