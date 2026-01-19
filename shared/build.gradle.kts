import co.touchlab.skie.configuration.DefaultArgumentInterop
import co.touchlab.skie.configuration.EnumInterop
import co.touchlab.skie.configuration.FlowInterop
import co.touchlab.skie.configuration.SealedInterop
import co.touchlab.skie.configuration.SuppressSkieWarning
import co.touchlab.skie.configuration.SuspendInterop
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
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
  alias(libs.plugins.buildkonfig)
  alias(libs.plugins.moko.resources)
  alias(libs.plugins.composeMultiplatform)
  alias(libs.plugins.composeCompiler)
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
      export(libs.moko.resources)
      export(libs.kermit)
    }
  }

  tasks.withType<KspAATask>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
      dependsOn("kspCommonMainKotlinMetadata")
    }
  }

  sourceSets {
    androidMain.dependencies {
      implementation(libs.androidx.room.ktx)
      api(libs.androidx.work.runtime.ktx)
      implementation(libs.androidx.core.ktx)
      implementation(libs.androidx.browser)

      api(libs.moko.resources.compose)

      implementation(libs.ktor.client.android)
    }

    commonMain.dependencies {
      implementation(libs.androidx.paging.common)
      implementation(libs.androidx.room.paging)
      implementation(libs.androidx.room.runtime)
      implementation(libs.androidx.sqlite.bundled)

      implementation(libs.compose.components.resources)
      implementation(libs.compose.foundation)
      implementation(libs.compose.material.icons.core)
      implementation(libs.compose.material3)
      implementation(libs.compose.runtime)
      implementation(libs.compose.ui)

      api(libs.coil.compose)
      implementation(libs.coil.network.ktor3)

      api(libs.decompose)
      api(libs.essenty.lifecycle)
      api(libs.essenty.lifecycle.coroutines)
      api(libs.essenty.stateKeeper)
      api(libs.essenty.backHandler)

      api(libs.kermit)

      api(libs.kotlinx.coroutines.core)
      api(libs.kotlinx.datetime)
      api(libs.kotlinx.serialization.json)

      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.contentNegotiation)
      implementation(libs.ktor.client.logging)
      implementation(libs.ktor.serialization.kotlinx.json)
      implementation(libs.ktorfit)

      api(libs.moko.resources)
    }

    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutines.test)
    }

    iosMain.dependencies { implementation(libs.ktor.client.darwin) }
  }
}

buildkonfig {
  packageName = "com.trm.cryptosphere.shared"

  defaultConfigs {
    val localProperties = gradleLocalProperties(rootDir, providers)
    buildConfigField(
      type = FieldSpec.Type.STRING,
      name = "CMC_API_KEY",
      value =
        requireNotNull(localProperties.getProperty("cmc_api_key")) {
          "A property cmc_api_key with CoinMarketCap API key must be set in local.properties."
        },
      const = true,
    )
    buildConfigField(
      type = FieldSpec.Type.STRING,
      name = "COIN_NEWS_API_KEY",
      value =
        requireNotNull(localProperties.getProperty("coin_news_api_key")) {
          "A property coin_news_api_key with CoinNews API key must be set in local.properties."
        },
      const = true,
    )
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

ktorfit { compilerPluginVersion.set("2.3.3") }

multiplatformResources { resourcesPackage.set("com.trm.cryptosphere.shared") }
