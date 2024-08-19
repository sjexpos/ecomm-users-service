package io.oigres.ecomm.service.users;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import io.oigres.ecomm.service.users.config.mapper.ModelMapperConfiguration;
import io.oigres.ecomm.service.users.config.WebConfiguration;

@SpringBootApplication
@Import({ModelMapperConfiguration.class, WebConfiguration.class})
public class TestApplication {

}
