/**********
 This project is free software; you can redistribute it and/or modify it under
 the terms of the GNU General Public License as published by the
 Free Software Foundation; either version 3.0 of the License, or (at your
 option) any later version. (See <https://www.gnu.org/licenses/gpl-3.0.html>.)

 This project is distributed in the hope that it will be useful, but WITHOUT
 ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 more details.

 You should have received a copy of the GNU General Public License
 along with this project; if not, write to the Free Software Foundation, Inc.,
 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 **********/
// Copyright (c) 2024-2025 Sergio Exposito.  All rights reserved.              

package io.oigres.ecomm.service.users.config;

import java.net.URI;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointParams;
import software.amazon.awssdk.services.s3.endpoints.S3EndpointProvider;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@Slf4j
public class AWSClientsConfig {
  private static final String AWS_ENDPOINT_ENV_VAR_NAME = "AWS_ENDPOINT";

  @Bean
  public S3Client getS3Client() {
    S3ClientBuilder builder = S3Client.builder();
    if (!Objects.isNull(System.getenv(AWS_ENDPOINT_ENV_VAR_NAME))) {
      log.warn(
          "Environment variable {} was detected. S3Client will be created using {}",
          AWS_ENDPOINT_ENV_VAR_NAME,
          System.getenv(AWS_ENDPOINT_ENV_VAR_NAME));
      S3EndpointParams endpointParams =
          S3EndpointParams.builder()
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
      log.warn(
          "Environment variable {} was detected. S3Presigner will be created using {}",
          AWS_ENDPOINT_ENV_VAR_NAME,
          System.getenv(AWS_ENDPOINT_ENV_VAR_NAME));
      builder.endpointOverride(URI.create(System.getenv(AWS_ENDPOINT_ENV_VAR_NAME)));
      S3Configuration s3Config = S3Configuration.builder().pathStyleAccessEnabled(true).build();
      builder.serviceConfiguration(s3Config);
    }
    return builder.build();
  }
}
