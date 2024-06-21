plugins {
    `kotlin-dsl`
    id("com.diffplug.gradle.spotless") version "3.25.0" apply false
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:3.25.0")
}

fun plugin(id: String, version: String) = "$id:$id.gradle.plugin:$version"
