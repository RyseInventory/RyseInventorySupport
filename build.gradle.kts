import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.github.rysefoxx.inventory.bot.parent"
archivesName.set("Supportbot")

subprojects {
    apply(plugin = "kotlin")
    apply(plugin = "com.github.johnrengelman.shadow")

    tasks.withType<ShadowJar> {
        mergeServiceFiles()
    }
}

allprojects {
    repositories {
        mavenCentral()
    }

    version = "0.1-alpha"

    tasks {
        compileJava {
            options.encoding = "UTF-8"
            options.release.set(17)
        }
        compileKotlin {
            kotlinOptions.jvmTarget = "17"
        }
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":logger"))
    implementation(project(":spring:core"))
    implementation(project(":spring:event"))
    implementation(project(":command-bridge"))
    implementation(project(":tag"))
    implementation(project(":ticket"))
    implementation(project(":punishment"))
}

tasks {
    jar {
        manifest {
            attributes(
                "Main-Class" to "io.github.rysefoxx.inventory.bot.core.BootstrapKt"
            )
        }
    }
    shadowJar {
        mergeServiceFiles()
    }
}