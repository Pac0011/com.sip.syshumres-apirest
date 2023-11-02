package com.sip.syshumres_apirest.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("syshumres") // Prefijo para las propiedades
public class AppProperties {
	private String sessionUserName;
	private long accessTokenValiditySeconds;
	private int sizeEmployeeNumber;
	private int sizeHashDirUploadEmployee;
	private String urlAccessLogin;
	private String urlPasswordRecovery;
	private String linkEmailPasswordRecovery;
	private String emailFrom;
	private String activemqBrokerUrl;
	private String activemqTopic;
	
	public String getSessionUserName() {
		return sessionUserName;
	}
	public void setSessionUserName(String sessionUserName) {
		this.sessionUserName = sessionUserName;
	}
	public long getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}
	public void setAccessTokenValiditySeconds(long accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}
	public int getSizeEmployeeNumber() {
		return sizeEmployeeNumber;
	}
	public void setSizeEmployeeNumber(int sizeEmployeeNumber) {
		this.sizeEmployeeNumber = sizeEmployeeNumber;
	}
	public int getSizeHashDirUploadEmployee() {
		return sizeHashDirUploadEmployee;
	}
	public void setSizeHashDirUploadEmployee(int sizeHashDirUploadEmployee) {
		this.sizeHashDirUploadEmployee = sizeHashDirUploadEmployee;
	}
	public String getUrlAccessLogin() {
		return urlAccessLogin;
	}
	public void setUrlAccessLogin(String urlAccessLogin) {
		this.urlAccessLogin = urlAccessLogin;
	}
	public String getUrlPasswordRecovery() {
		return urlPasswordRecovery;
	}
	public void setUrlPasswordRecovery(String urlPasswordRecovery) {
		this.urlPasswordRecovery = urlPasswordRecovery;
	}
	public String getLinkEmailPasswordRecovery() {
		return linkEmailPasswordRecovery;
	}
	public void setLinkEmailPasswordRecovery(String linkEmailPasswordRecovery) {
		this.linkEmailPasswordRecovery = linkEmailPasswordRecovery;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public String getActivemqBrokerUrl() {
		return activemqBrokerUrl;
	}
	public void setActivemqBrokerUrl(String activemqBrokerUrl) {
		this.activemqBrokerUrl = activemqBrokerUrl;
	}
	public String getActivemqTopic() {
		return activemqTopic;
	}
	public void setActivemqTopic(String activemqTopic) {
		this.activemqTopic = activemqTopic;
	}	
}
