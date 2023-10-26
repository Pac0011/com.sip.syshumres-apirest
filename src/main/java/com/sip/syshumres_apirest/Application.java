package com.sip.syshumres_apirest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@PropertySource("classpath:uploadfile.properties")
@EntityScan({"com.sip.syshumres_entities"})
@EnableJpaRepositories(basePackages = {"com.sip.syshumres_repositories"})
@SpringBootApplication(scanBasePackages = {
		"com.sip.syshumres_exceptions", 
		"com.sip.syshumres_utils",
		"com.sip.syshumres_services", 
		"com.sip.syshumres_apirest"})
@EnableAspectJAutoProxy(proxyTargetClass=true)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
