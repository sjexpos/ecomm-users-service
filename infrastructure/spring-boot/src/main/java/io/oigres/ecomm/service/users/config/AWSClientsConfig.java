package io.oigres.ecomm.service.users.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointParams;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointProvider;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;
import java.util.Objects;

@Configuration
@Slf4j
public class AWSClientsConfig {
	static private final String AWS_ENDPOINT_ENV_VAR_NAME = "AWS_ENDPOINT";

    @Bean
    public S3Client getS3Client() {
        S3ClientBuilder builder = S3Client.builder();
        if (!Objects.isNull(System.getenv(AWS_ENDPOINT_ENV_VAR_NAME))) {
        	log.warn("Environment variable {} was detected. S3Client will be created using {}", AWS_ENDPOINT_ENV_VAR_NAME, System.getenv(AWS_ENDPOINT_ENV_VAR_NAME));
            S3EndpointParams endpointParams = S3EndpointParams.builder()
            			.endpoint(System.getenv(AWS_ENDPOINT_ENV_VAR_NAME))
            			.forcePathStyle(Boolean.TRUE)
            			.build();
            S3EndpointProvider endpointProvider = S3EndpointProvider.defaultProvider();
            endpointProvider.resolveEndpoint(endpointParams);
            builder = builder.endpointProvider(endpointProvider);
        }
        return builder.build();
    }

    @Bean
    public S3Presigner getS3Presigner() {
    	S3Presigner.Builder builder = S3Presigner.builder(); 
        if (!Objects.isNull(System.getenv(AWS_ENDPOINT_ENV_VAR_NAME))) {
        	log.warn("Environment variable {} was detected. S3Presigner will be created using {}", AWS_ENDPOINT_ENV_VAR_NAME, System.getenv(AWS_ENDPOINT_ENV_VAR_NAME));
        	builder.endpointOverride(URI.create(System.getenv(AWS_ENDPOINT_ENV_VAR_NAME)));
        	S3Configuration s3Config = S3Configuration.builder()
        			.pathStyleAccessEnabled(true)
        			.build();
        	builder.serviceConfiguration(s3Config);
        }
    	return builder.build();
    }

}
