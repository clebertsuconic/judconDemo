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

   @Resource
   private MessageDrivenContext context;

   @Resource(mappedName = "java:/JmsXA")
   private ConnectionFactory cf;

   private Queue queue = null;

   public void onMessage(Message m) {
      Connection con = null;
      Session session = null;
      try {
         Thread.sleep(500);
         System.out.println("Message " + m.getStringProperty("count"));



         con = cf.createConnection();

         session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);

         if (queue == null)  {
            queue = session.createQueue("OutQueue");
         }
         MessageProducer sender = session.createProducer(queue);
         TextMessage newMessage = session.createTextMessage("received message from " + m.getStringProperty("count"));
         newMessage.setStringProperty("inMessageId", m.getJMSMessageID());
         sender.send(newMessage);

      } catch (Exception t) {
         t.printStackTrace();
         this.context.setRollbackOnly();
      } finally {
         if (con != null) {
            try {
               con.close();
            } catch (JMSException e) {
               e.printStackTrace();
            }
         }
      }
   }
}
