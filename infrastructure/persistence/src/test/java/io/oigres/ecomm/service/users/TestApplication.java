package io.oigres.ecomm.service.users;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.oigres.ecomm.service.users.repository.SearchRepositoryImpl;

@SpringBootApplication
@EnableJpaRepositories( basePackages="io.oigres.ecomm.service.users.repository", repositoryBaseClass = SearchRepositoryImpl.class )
@EntityScan( "io.oigres.ecomm.service.users.domain" )
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@ComponentScan
public class TestApplication {

}
