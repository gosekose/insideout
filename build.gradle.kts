import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "3.3.0" apply false
    id("io.spring.dependency-management") version "1.1.5" apply false
    id("org.jlleitschuh.gradle.ktlint") version "12.1.1" apply false
    kotlin("jvm") version "1.9.24" apply false
    kotlin("plugin.spring") version "1.9.24" apply false
    kotlin("plugin.noarg") version "1.8.21" apply false
    kotlin("kapt") version "1.9.21" apply false
}

group = "com.insideout"
version = "0.0.1-SNAPSHOT"

allprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
    apply(plugin = "kotlin")
    apply(plugin = "java")

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "org.jetbrains.kotlin.plugin.noarg")
    apply(plugin = "kotlin")
    apply(plugin = "java")

    extensions.configure<JavaPluginExtension>("java") {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    val implementation by configurations
    val runtimeOnly by configurations
    val testImplementation by configurations
    val testRuntimeOnly by configurations

    dependencies {
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("org.springframework.boot:spring-boot-starter")

        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
    tasks.register("prepareKotlinBuildScriptModel") {}

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    tasks.withType<BootJar> {
        enabled = false
        mainClass.set("com.insideout.ApiApplicationKt")
    }

    tasks.withType<Jar> {
        enabled = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += "-Xjsr305=strict"
    }
}

tasks.withType<BootJar> {
    mainClass.set("com.insideout.ApiApplicationKt")
}

if (hasProperty("buildScan")) {
    extensions.findByName("buildScan")?.withGroovyBuilder {
        setProperty("termsOfServiceUrl", "https://gradle.com/terms-of-service")
        setProperty("termsOfServiceAgree", "yes")
    }
}

// 기본 빌드 모듈
val apiModules =
    listOf(
        ":api",
        ":application",
        ":infra",
        ":infra:persistence-mysql",
        ":infra:authentication-jwt",
        ":infra:redis",
        ":domain",
        ":lib",
    )

val batchModules =
    listOf(
        ":batch",
        ":application",
        ":infra",
        ":infra:persistence-mysql",
        ":infra:authentication-jwt",
        ":infra:redis",
        ":domain",
        ":lib",
    )

tasks.register("buildApi") {
    group = "build"
    description = "Builds the API modules"
    dependsOn(apiModules.map { project(it).tasks.getByName("build") })
}

tasks.register("buildBatch") {
    group = "build"
    description = "Builds the Batch modules"
    dependsOn(batchModules.map { project(it).tasks.getByName("build") })
}
