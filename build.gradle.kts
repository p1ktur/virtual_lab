import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    kotlin("plugin.serialization") version "2.0.20"
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

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Testing
    implementation(kotlin("test"))
    implementation(kotlin("test-junit"))
    implementation("junit:junit:4.13.2")

    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
    implementation(compose.uiTest)

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
