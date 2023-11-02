package com.sip.syshumres_apirest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "upload")
public class UploadProperties {
	private String baseDocumentsEmployees;
	private String urlDocumentsEmployees;
	private String pathDocumentsEmployees;
	private String listFormatsAllow;
	
	public String getBaseDocumentsEmployees() {
		return baseDocumentsEmployees;
	}
	public void setBaseDocumentsEmployees(String baseDocumentsEmployees) {
		this.baseDocumentsEmployees = baseDocumentsEmployees;
	}
	public String getUrlDocumentsEmployees() {
		return urlDocumentsEmployees;
	}
	public void setUrlDocumentsEmployees(String urlDocumentsEmployees) {
		this.urlDocumentsEmployees = urlDocumentsEmployees;
	}
	public String getPathDocumentsEmployees() {
		return pathDocumentsEmployees;
	}
	public void setPathDocumentsEmployees(String pathDocumentsEmployees) {
		this.pathDocumentsEmployees = pathDocumentsEmployees;
	}
	public String getListFormatsAllow() {
		return listFormatsAllow;
	}
	public void setListFormatsAllow(String listFormatsAllow) {
		this.listFormatsAllow = listFormatsAllow;
	}

}
