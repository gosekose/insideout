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

    testImplementation("com.h2database:h2")

    implementation(project(":lib"))
    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    implementation(project(":infrastructure:persistence-mysql"))
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
