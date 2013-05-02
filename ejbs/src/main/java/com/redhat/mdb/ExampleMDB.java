package com.redhat.mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.jms.*;
import java.util.concurrent.atomic.AtomicInteger;


@MessageDriven(name = "mdb1",
      activationConfig = {
            @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
            @ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/queue/InQueue")})
@TransactionManagement(value = TransactionManagementType.CONTAINER)
@TransactionAttribute(value = TransactionAttributeType.REQUIRED)
public class ExampleMDB implements MessageListener {
    public void onMessage(Message m) {
        try {
                        System.out.println("Message " + m.getStringProperty("count"));
                } catch (JMSException e) {
                        e.printStackTrace();
                }
    }
}
