package com.petlog.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "aws")
public class S3Config {

    private S3 s3;
    private Credentials credentials;
    private String region;

    @Data
    public static class S3 {
        private String bucket;
    }

    @Data
    public static class Credentials {
        private String accessKey;
        private String secretKey;
    }
}
