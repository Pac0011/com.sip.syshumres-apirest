package com.sip.syshumres_apirest.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase WebConfig. Mapea archivos subidos a directorios locales con urls
 * 
 * @author Prong
 * @version 2.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{
	
	private final UploadProperties uploadProperties;
	
	public WebConfig(UploadProperties uploadProperties) {
		this.uploadProperties = uploadProperties;
	}
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//pathImages = "/Users/prong/jobs/imgvacants/"
		//System.out.println("Ruta con value: " + this.uploadDocuments)
		registry.addResourceHandler(uploadProperties.getUrlDocumentsEmployees() + "**")
			.addResourceLocations("file:" +  uploadProperties.getPathDocumentsEmployees()); // Linux
		//registry.addResourceHandler("/cv/**").addResourceLocations("file:" + pathCv); // Linux
		
		registry.addResourceHandler("swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");

	    registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
