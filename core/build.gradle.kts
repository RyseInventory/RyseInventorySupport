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

import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName

group = "io.github.rysefoxx.inventory.bot.core"
archivesName.set("Supportbot-Core")

repositories {
    maven("https://jitpack.io/")
}

dependencies {
    //JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.5")
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")

    //LOGGING
    implementation(project(":logger"))

    //DATABASE
    implementation(project(":spring:core"))
    implementation(project(":spring:event"))

    //COMMAND-BRIDGE
    implementation(project(":command-bridge"))

    //PUNISHMENT SYSTEM
    implementation(project(":punishment"))

    //TAG SYSTEM
    implementation(project(":tag"))

    //CLASS EXTENSION
    implementation(project(":extension"))

    //DOCUMENTS
    implementation(project(":document"))

    //CLASS EXTENSION
    implementation(project(":spring:model"))

    //SPRING FOR DATABASE
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter-cache:2.7.2")
    implementation("org.hibernate:hibernate-core:5.6.5.Final")
    implementation("org.hibernate:hibernate-envers:5.6.5.Final")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    //YML
    implementation("org.yaml:snakeyaml:1.29")
}