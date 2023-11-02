package com.sip.syshumres_apirest.config;

import java.util.Arrays;

import javax.jms.Queue;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@EnableJms
public class SpringActiveMQConfig {
	
	private final AppProperties appProperties;
	
	public SpringActiveMQConfig(AppProperties appProperties) {
		this.appProperties = appProperties;
	}

	@Bean
	public Queue queue() {
		return new ActiveMQQueue(appProperties.getActivemqTopic());
	}

	@Bean
	public ActiveMQConnectionFactory activeMQConnectionFactory() {
		ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
		activeMQConnectionFactory.setBrokerURL(appProperties.getActivemqBrokerUrl());
		activeMQConnectionFactory.setTrustedPackages(Arrays.asList("com.sip.syshumres_entities"));
		return activeMQConnectionFactory;
	}

	@Bean
	public JmsTemplate jmsTemplate() {
		return new JmsTemplate(activeMQConnectionFactory());
	}

}