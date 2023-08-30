package com.sip.syshumres_apirest.config;

import org.springframework.beans.factory.annotation.Value;
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
	
	@Value("${UPLOAD.PATH.DOCUMENTS.EMPLOYEES}")
	private String uploadDocuments;
	
	@Value("${URL.DOCUMENTS.EMPLOYEES}")
	private String urlDocuments;
	
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		//pathImages = "/Users/prong/jobs/imgvacants/";
		//System.out.println("Ruta con value: " + this.uploadDocuments);
		registry.addResourceHandler(urlDocuments + "**").addResourceLocations("file:" + this.uploadDocuments); // Linux
		//registry.addResourceHandler("/cv/**").addResourceLocations("file:" + pathCv); // Linux
		
		registry.addResourceHandler("swagger-ui.html")
	      .addResourceLocations("classpath:/META-INF/resources/");

	    registry.addResourceHandler("/webjars/**")
	      .addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}
