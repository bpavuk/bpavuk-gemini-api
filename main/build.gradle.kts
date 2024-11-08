plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("module.publication")
}

kotlin {
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    linuxX64()
    linuxArm64()
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs()

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.ktor.client)
                implementation(libs.ktor.contentNegotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.ktor.logging)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.io)
                api(project(":models"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        appleMain.dependencies {
            implementation(libs.napier)
            implementation(libs.ktor.engine.darwin)
        }
        linuxMain.dependencies {
            implementation(libs.ktor.engine.linux)
        }
        jvmMain.dependencies {
            implementation(libs.napier)
            implementation(libs.ktor.engine.jvm)
        }
        wasmJsMain.dependencies {
            implementation(libs.ktor.engine.wasmJs)
        }
    }

    explicitApi()
}
