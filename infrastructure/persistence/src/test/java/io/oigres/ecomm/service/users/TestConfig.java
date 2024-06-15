package io.oigres.ecomm.service.users;

import java.util.Optional;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

@TestConfiguration
public class TestConfig {
	public static final String AUDITOR_NAME = "jpa_tests";

    @Bean
	AuditorAware<String> auditorProvider() {
		return new AuditorAware<String>() {

			@Override
			public Optional<String> getCurrentAuditor() {
				return Optional.of(AUDITOR_NAME);
			}

		};
	}

}
