dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation(project(":domain"))
    implementation(project(":infrastructure"))
    implementation(project(":infrastructure:persistence-mysql"))
    implementation(project(":infrastructure:file-management"))

    testImplementation("com.h2database:h2")
}
