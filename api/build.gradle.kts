import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    kotlin("kapt") version "1.9.21"
    kotlin("plugin.noarg") version "1.8.21"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")

    implementation("com.querydsl:querydsl-jpa:5.0.0:jakarta")
    kapt("com.querydsl:querydsl-apt:5.0.0:jakarta")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")

    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("com.google.code.gson:gson:2.8.9")

    testImplementation("com.h2database:h2")

    implementation(project(":lib"))
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    implementation(project(":infrastructure:persistence-mysql"))
    implementation(project(":infrastructure:authentication-jwt"))
    implementation(project(":application"))
}

tasks.withType<BootJar> {
    enabled = true
}

tasks.withType<Jar> {
    enabled = true
}

noArg {
    invokeInitializers = true
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
