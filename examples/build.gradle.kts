plugins {
    application
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(projects.main)
    implementation(libs.kotlinx.serialization.json)
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

application {
    mainClass = "bpavuk.gemini.examples.MainKt"
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "bpavuk.gemini.examples.MainKt"
        )
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.shadowJar {
    manifest {
        attributes(
            "Main-Class" to "bpavuk.gemini.examples.MainKt"
        )
    }
    archiveBaseName = "gemini-cli"
}