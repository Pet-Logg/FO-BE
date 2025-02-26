package com.petlog.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AmazonS3Client {

    private final S3Config s3Config;

    @Bean
    public AmazonS3 amazonS3 () {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(
                s3Config.getCredentials().getAccessKey(),
                s3Config.getCredentials().getSecretKey()
        );

        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(s3Config.getRegion()))
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
