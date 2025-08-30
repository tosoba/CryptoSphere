import co.touchlab.skie.configuration.DefaultArgumentInterop
import co.touchlab.skie.configuration.EnumInterop
import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuppressSkieWarning
import co.touchlab.skie.configuration.SuspendInterop
import com.google.devtools.ksp.gradle.KspAATask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.kotlinSerialization)
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.skie)
  alias(libs.plugins.kotlinKsp)
  alias(libs.plugins.ktorfit)
  alias(libs.plugins.room)
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

  tasks.withType<KspAATask>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
      dependsOn("kspCommonMainKotlinMetadata")
    }
  }

  sourceSets {
    androidMain.dependencies { implementation(libs.androidx.room.ktx) }

    commonMain.dependencies {
      implementation(libs.androidx.room.runtime)

      api(libs.decompose)
      api(libs.essenty.lifecycle)
      api(libs.essenty.stateKeeper)
      api(libs.essenty.backHandler)

      api(libs.kotlinx.coroutines.core)
      api(libs.kotlinx.datetime)
      api(libs.kotlinx.serialization.json)

      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.contentNegotiation)
      implementation(libs.ktor.client.logging)
      implementation(libs.ktor.serialization.kotlinx.json)
      implementation(libs.ktorfit)

      implementation(libs.store)
    }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutines.test)
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

dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
  add("kspIosSimulatorArm64", libs.androidx.room.compiler)
  add("kspIosX64", libs.androidx.room.compiler)
  add("kspIosArm64", libs.androidx.room.compiler)
}

room { schemaDirectory("$projectDir/schemas") }

skie {
  analytics { disableUpload.set(true) }

  features {
    group {
      DefaultArgumentInterop.Enabled(false)
      SuspendInterop.Enabled(true)
      FlowInterop.Enabled(true)
      EnumInterop.Enabled(true)
      SealedInterop.Enabled(true)
      SuppressSkieWarning.NameCollision(true)
    }
  }
}
