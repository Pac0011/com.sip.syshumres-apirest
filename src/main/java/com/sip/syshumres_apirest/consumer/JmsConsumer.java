package com.sip.syshumres_apirest.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
//@Slf4j
public class JmsConsumer {
	
	private static final Logger logger = LoggerFactory.getLogger(JmsConsumer.class);

	@JmsListener(destination = "${activemq.topic}")
	public void consumeMessage(String message) {
		try{
            //do additional processing
            //log.info("Received Message: "+ prospect.toString())           
	   		if (logger.isInfoEnabled()) {
			    logger.info("Received Message: {}", message);
	   		}
           
        } catch(Exception e) {
            if (logger.isErrorEnabled()) {
        	   logger.error("Error: {} ", e.getMessage());
	   	    }
        }
	}
}
