package com.backend.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class AwsS3Config {
    private static final Logger logger = LoggerFactory.getLogger(AwsS3Config.class);

    @Value("${aws.access.key.id}")
    private String accessKeyId;

    @Value("${aws.secret.access.key}")
    private String secretAccessKey;

    @Value("${aws.region:eu-central-1}")
    private String region;

    @Bean
    public S3Client s3Client() {
        logger.info("Initializing S3Client with region: {}", region);
        logger.info("AWS Access Key ID (masked): {}***", 
            accessKeyId != null && accessKeyId.length() > 4 ? accessKeyId.substring(0, 4) : "NULL");
        
        try {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            StaticCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(awsCredentials);

            S3Client client = S3Client.builder()
                    .region(Region.of(region))
                    .credentialsProvider(credentialsProvider)
                    .forcePathStyle(false)
                    .build();
            
            logger.info("S3Client successfully initialized for region: {}", region);
            return client;

        } catch (Exception e) {
            logger.error("Failed to initialize S3Client for region '{}': {}", region, e.getMessage(), e);
            throw e;
        }
    }
}
