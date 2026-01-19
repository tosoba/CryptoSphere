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

      export(libs.common.decompose)
      export(libs.common.essenty.backHandler)
      export(libs.common.essenty.lifecycle)
      export(libs.common.essenty.stateKeeper)
      export(libs.common.moko.resources)
      export(libs.common.kermit)
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

      implementation(libs.android.ktor.client.android)

      api(libs.common.moko.resources.compose)
    }

    commonMain.dependencies {
      implementation(libs.common.compose.components.resources)
      implementation(libs.common.compose.foundation)
      implementation(libs.common.compose.material.icons.core)
      implementation(libs.common.compose.material3)
      implementation(libs.common.compose.runtime)
      implementation(libs.common.compose.ui)

      api(libs.common.coil.compose)
      implementation(libs.common.coil.network.ktor3)

      api(libs.common.decompose)
      api(libs.common.essenty.lifecycle)
      api(libs.common.essenty.lifecycle.coroutines)
      api(libs.common.essenty.stateKeeper)
      api(libs.common.essenty.backHandler)

      api(libs.common.kermit)

      api(libs.common.kotlinx.coroutines.core)
      api(libs.common.kotlinx.datetime)
      api(libs.common.kotlinx.serialization.json)

      implementation(libs.common.ktor.client.core)
      implementation(libs.common.ktor.client.contentNegotiation)
      implementation(libs.common.ktor.client.logging)
      implementation(libs.common.ktor.serialization.kotlinx.json)
      implementation(libs.common.ktorfit)

      api(libs.common.moko.resources)

      implementation(libs.common.paging.common)

      implementation(libs.common.room.paging)
      implementation(libs.common.room.runtime)
      implementation(libs.common.sqlite.bundled)
    }

    commonTest.dependencies {
      implementation(libs.common.kotlin.test)
      implementation(libs.common.kotlinx.coroutines.test)
    }

    iosMain.dependencies { implementation(libs.ios.ktor.client.darwin) }
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
