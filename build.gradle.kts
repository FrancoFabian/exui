import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version "2.0.0" // Asegúrate de que sea la versión compatible de Kotlin
    id("com.netflix.nebula.maven-publish") version "21.1.0"// Ajusta según la versión más reciente
    id ("com.netflix.nebula.source-jar") version "21.1.0"   // Ajusta según la versión más reciente
    id("com.bybutter.sisyphus.project") version "2.1.10"    // Ajusta según la versión más reciente
    id("org.jetbrains.compose") version "1.6.10"            // Ajusta según la versión de Compose que estés utilizando
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    `java-library`
}

group = "com.mx.dashboard"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    google()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://plugins.gradle.org/m2/")

}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(compose.desktop.common) {
        exclude("org.jetbrains.compose.material")
    }
    // Si necesitas funcionalidades específicas del sistema operativo actual
    // implementation(compose.desktop.currentOs)
}


compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "exui"
            packageVersion = "1.0.0"
        }
    }
}
