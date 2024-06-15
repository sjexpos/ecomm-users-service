package io.oigres.ecomm.service.users.config;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.nio.charset.Charset;
import java.time.ZonedDateTime;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.redisson.config.ConfigPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import io.oigres.ecomm.service.users.jobs.CardImagesCleanUpJobConfiguration;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(0)
@Slf4j
public class ApplicationReadyInitializer implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private Environment environment;
	@Autowired
	private CardImagesCleanUpJobConfiguration cardImagesCleanUpJobConfiguration;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("");
        log.info("********************************************************************");
		log.info(" USERS SERVICE STARTED");
        log.info("********************************************************************");
		log.info("Default Charset: {}", Charset.defaultCharset());
		log.info("File Encoding:   {}", System.getProperty("file.encoding"));
		log.info("Server time:     {}", ZonedDateTime.now());
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		log.info("----------------------- JVM memory ----------------------");
		log.info("Max Heap size: {}", FileUtils.byteCountToDisplaySize(memoryBean.getHeapMemoryUsage().getMax()));
		log.info("Initial Heap size: {}", FileUtils.byteCountToDisplaySize(memoryBean.getHeapMemoryUsage().getInit()));
		log.info("Heap usage: {}", FileUtils.byteCountToDisplaySize(memoryBean.getHeapMemoryUsage().getUsed()));
		log.info("------------------------ Postgres -----------------------");
        log.info("Host:     {}", environment.getProperty("spring.datasource.host"));
        log.info("Port:     {}", environment.getProperty("spring.datasource.port"));
        log.info("Schema:   {}", environment.getProperty("spring.datasource.schemaName"));
        log.info("Username: {}", environment.getProperty("spring.datasource.username"));
        String password = environment.getProperty("spring.datasource.password", "");
        password = password.substring(0, 1) + StringUtils.repeat("*", password.length()-2) + password.substring(password.length()-1);
        log.info("Password: {}", password);
		log.info("-------------------- Elasticsearch ----------------------");
		log.info("Type: {}", environment.getProperty("spring.jpa.properties.hibernate.search.backend.type"));
        log.info("Hosts: {}", environment.getProperty("spring.jpa.properties.hibernate.search.backend.hosts"));
        log.info("Protocol: {}", environment.getProperty("spring.jpa.properties.hibernate.search.backend.protocol"));
		log.info("------------------------ Redis --------------------------");
        log.info("Redisson config: {}", environment.getProperty("spring.jpa.properties.hibernate.cache.redisson.config"));
		new ConfigPrinter().print(log, environment.getProperty("spring.jpa.properties.hibernate.cache.redisson.config"));
		log.info("-------------------- Assets Bucket --------------------------");
		log.info("Name:               {}", environment.getProperty("ecomm.service.users.assets.bucket.name"));
		log.info("Root :              {}", environment.getProperty("ecomm.service.users.assets.bucket.rootFolder"));
		log.info("Signature Duration: {}", environment.getProperty("ecomm.service.users.assets.bucket.signatureDuration"));
		log.info("-------------------- Cards resources clean up job --------------------------");
		log.info("Initial delay:      {}m", cardImagesCleanUpJobConfiguration.getInitialDelay());
		log.info("Fixed rate:         {}m", cardImagesCleanUpJobConfiguration.getFixedRate());
        log.info("********************************************************************");
        log.info("");
	}

}