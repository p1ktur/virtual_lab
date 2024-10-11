import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "com.virtual.lab"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Main
    implementation(compose.desktop.currentOs)

    // Koin
    implementation("io.insert-koin:koin-core:3.6.0-wasm-alpha2")
    implementation("io.insert-koin:koin-core-coroutines:3.6.0-wasm-alpha2")
    implementation("io.insert-koin:koin-compose:3.6.0-wasm-alpha2")

    // PreCompose
    api("moe.tlaster:precompose:1.6.0-rc05")
    api("moe.tlaster:precompose-molecule:1.6.0-rc05")
    api("moe.tlaster:precompose-viewmodel:1.6.0-rc05")
    api("moe.tlaster:precompose-koin:1.6.0-rc05")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1-Beta")

    // Icons
    implementation(compose.materialIconsExtended)

    // Resources
    implementation(compose.components.resources)

    // Material3
    implementation("org.jetbrains.compose.material3:material3-desktop:1.2.1")

    // File picker
    implementation("com.darkrockstudios:mpfilepicker:3.1.0")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "VirtualLaboratory"
            packageVersion = "1.0.0"
        }
    }
}
