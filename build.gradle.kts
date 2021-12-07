val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_sql_version: String by project

plugins {
    application
    kotlin("jvm") version "1.5.32"
    kotlin("plugin.serialization") version "1.5.32"
}

group = "com.emirhan"
version = "0.0.1"
application {
    mainClass.set("com.emirhan.ApplicationKt")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

tasks.create("stage") {
    dependsOn("installDist")
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("io.ktor:ktor-auth:$ktor_version")
    implementation("io.ktor:ktor-auth-jwt:$ktor_version")
    implementation("io.ktor:ktor-serialization:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("org.jetbrains.exposed:exposed-core:$exposed_sql_version")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$exposed_sql_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_sql_version")
    implementation("org.postgresql:postgresql:42.3.1")

    implementation("org.mindrot:jbcrypt:0.4")

    implementation("com.github.papsign:Ktor-OpenAPI-Generator:0.3-beta.2")
    implementation("io.insert-koin:koin-ktor:3.1.4")
    implementation("io.insert-koin:koin-logger-slf4j:3.1.4")

    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")

    implementation("org.mpierce.ktor.csrf:ktor-csrf:0.6.3")

}