/*
 * Copyright (c)  2023 Rysefoxx
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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