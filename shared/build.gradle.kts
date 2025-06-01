import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.androidLibrary)
}

kotlin {
  androidTarget { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }

  listOf(iosX64(), iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "Shared"
      isStatic = true

      export(libs.decompose)
      export(libs.essenty.backHandler)
      export(libs.essenty.lifecycle)
      export(libs.essenty.stateKeeper)
    }
  }

  sourceSets {
    commonMain.dependencies {
      api(libs.decompose)
      api(libs.essenty.lifecycle)
      api(libs.essenty.stateKeeper)
      api(libs.essenty.backHandler)

      implementation(libs.kotlinx.serialization.json)
    }
  }
}

android {
  namespace = "com.trm.cryptosphere.shared"
  compileSdk = libs.versions.android.compileSdk.get().toInt()

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  defaultConfig { minSdk = libs.versions.android.minSdk.get().toInt() }
}
