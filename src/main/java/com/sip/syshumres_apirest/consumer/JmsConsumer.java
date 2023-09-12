package com.sip.syshumres_apirest.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
//@Slf4j
public class JmsConsumer {

	@JmsListener(destination = "${activemq.topic}")
	public void consumeMessage(String message) {
		try{
            //do additional processing
           //log.info("Received Message: "+ prospect.toString());
           System.out.println("Received Message: "+ message);
        } catch(Exception e) {
           //log.error("Received Exception : "+ e);
           System.out.println("Error " + e.getMessage());
        }
	}
}
