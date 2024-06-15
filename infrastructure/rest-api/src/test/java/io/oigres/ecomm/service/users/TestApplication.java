package io.oigres.ecomm.service.users;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.oigres.ecomm.service.users.config.mapper.ModelMapperConfiguration;

@SpringBootApplication
@Import(ModelMapperConfiguration.class)
public class TestApplication {

}
