package com.insideout.s3

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.presigner.S3Presigner

@Configuration
@EnableConfigurationProperties(AwsS3Properties::class)
class S3Configuration(
    private val awsS3Properties: AwsS3Properties,
) {
    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        awsS3Properties.credentials.accessKey,
                        awsS3Properties.credentials.secretKey,
                    ),
                ),
            )
            .build()
    }

    @Bean
    fun s3Presigner(): S3Presigner {
        return S3Presigner.builder()
            .region(Region.AP_NORTHEAST_2)
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(
                        awsS3Properties.credentials.accessKey,
                        awsS3Properties.credentials.secretKey,
                    ),
                ),
            )
            .build()
    }

    @Qualifier("fileManagementS3Adapter")
    @Bean
    fun fileManagementS3Adapter(): FileManagementS3Adapter {
        return FileManagementS3Adapter(
            s3Presigner = s3Presigner(),
            awsS3Properties = awsS3Properties,
        )
    }
}
