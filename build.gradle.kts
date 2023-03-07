import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.archivesName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "io.github.rysefoxx.inventory.bot"
version = "0.1-alpha"
archivesName.set("Supportbot")

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>{
    kotlinOptions.jvmTarget = "17"
}

repositories {
    mavenCentral()
    maven("https://jitpack.io/")
}

dependencies {
    //JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.5")
    implementation("com.github.minndevelopment:jda-ktx:0.10.0-beta.1")

    //LOGGING
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("ch.qos.logback:logback-classic:1.2.9")

    //JACKSON FOR DOCUMENT
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.3")

    //SPRING FOR DATABASE
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.7.2")
    implementation("org.mariadb.jdbc:mariadb-java-client:2.7.2")
    implementation("org.springframework.boot:spring-boot-starter-cache:2.7.2")
    implementation("org.hibernate:hibernate-core:5.6.5.Final")
    implementation("org.hibernate:hibernate-envers:5.6.5.Final")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    compileOnly("commons-io:commons-io:2.11.0")
}

tasks {
    jar {
        manifest {
            attributes(
                "Main-Class" to "io.github.rysefoxx.inventory.bot.MainKt"
            )
        }
    }
    shadowJar {
        mergeServiceFiles()
    }
}