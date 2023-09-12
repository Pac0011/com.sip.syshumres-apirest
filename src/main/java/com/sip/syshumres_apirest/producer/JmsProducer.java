package com.sip.syshumres_apirest.producer;

import javax.jms.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.sip.syshumres_entities.ProspectProfile;


@Component
//@Slf4j
public class JmsProducer {
    
    @Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Queue queue;

	public void sendMessage(ProspectProfile prospect) {

		try {
            //log.info("Attempting Send message to Topic: "+ topic);
			jmsTemplate.convertAndSend(queue, prospect);
		} catch (Exception e) {
			//log.error("Recieved Exception during send Message: ", e);
			System.out.println("Error " + e.getMessage());
		}
	}
}
