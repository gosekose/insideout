dependencies {
    implementation(project(":domain"))
    implementation("com.google.cloud:google-cloud-storage:2.40.1")
    implementation("io.awspring.cloud:spring-cloud-aws-starter-s3:3.1.1")
    implementation("org.springframework.cloud:spring-cloud-starter-circuitbreaker-resilience4j:3.1.1")
    implementation("io.github.resilience4j:resilience4j-bulkhead:1.7.1")
}