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