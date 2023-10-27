package com.sip.syshumres_apirest.producer;

import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.sip.syshumres_entities.ProspectProfile;


@Component
//@Slf4j
public class JmsProducer {
	
	private static final Logger logger = LoggerFactory.getLogger(JmsProducer.class);
    
    @Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Queue queue;

	public void sendMessage(ProspectProfile prospect) {

		try {
			if (logger.isInfoEnabled()) {
			    logger.info("Attempting Send message to Topic: {} ", prospect);
	   		}
			jmsTemplate.convertAndSend(queue, prospect);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
        	   logger.error("Error: {} ", e.getMessage());
	   	    }
		}
	}
}
