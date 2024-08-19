package io.oigres.ecomm.service.users.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;
import org.springframework.context.annotation.Configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

@Configuration
@ConfigurationProperties(prefix = "ecomm.service.users.assets.cleanup")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UsersAssetsCleanUpConfig {
    private Cards cards;

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Cards {

        @NotNull
        @DurationUnit(value = ChronoUnit.HOURS)
        private Duration imagesTimeAfterOffset;
    }
}
