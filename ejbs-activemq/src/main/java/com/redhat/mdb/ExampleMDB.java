package com.redhat.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;



@MessageDriven(messageListenerInterface = javax.jms.MessageListener.class, activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/test")
})
public class ExampleMDB implements MessageListener {
    public void onMessage(Message m) {
        try {
                        System.out.println("Message " + m.getStringProperty("count"));
                } catch (JMSException e) {
                        e.printStackTrace();
                }
    }
}
