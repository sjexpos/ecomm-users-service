package io.oigres.ecomm.service.users.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@ConfigurationProperties(prefix = "ecomm.service.users.assets")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsersAssetsConfig {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Bucket {
        @NotNull
        @NotBlank
        private String name;
        @NotNull
        @NotBlank
        private String rootFolder;
        @DurationUnit(ChronoUnit.DAYS)
        private Duration signatureDuration;
    }

    private Bucket bucket;

}
