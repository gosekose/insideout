rootProject.name = "insideout"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }

    plugins {
        id("org.springframework.boot") version "3.3.0"
        id("com.gradle.enterprise") version "3.13"
        id("io.spring.dependency-management") version "1.1.5"
        id("org.jetbrains.kotlin.jvm") version "1.9.24"
        id("org.jetbrains.kotlin.plugin.spring") version "1.9.24"
        id("org.jetbrains.kotlin.plugin.allopen") version "1.9.10"
        kotlin("plugin.noarg") version "1.8.21"
    }
}

include("lib")
include("domain")
include("infrastructure")
include("infrastructure:persistence-mysql")
include("infrastructure:authentication-jwt")
include("infrastructure:redis")
include("application")
include("api")
include("batch")
