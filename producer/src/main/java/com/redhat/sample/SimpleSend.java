package com.redhat.sample;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.api.jms.*;
import javax.jms.*;
import java.util.HashMap;

public class SimpleSend
{
   public static void main(String arg[])
   {

      try
      {
         HashMap map = new HashMap();

         map.put("port", 5445);

         TransportConfiguration transportConfiguration =
                              new TransportConfiguration(NettyConnectorFactory.class.getName());

         ConnectionFactory cf = (ConnectionFactory) HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF,transportConfiguration);

         // Use ./addUser on the Jboss, role=guest with this
         Connection conn = cf.createConnection("guest", "h0rnet0@");

         Session session = conn.createSession(true, Session.SESSION_TRANSACTED);

         // create the queue with
         // jboss-cli
         // jms-queue add --queue-address=test --entries=queue/test
         Queue  queue = session.createQueue("test");

         MessageProducer prod = session.createProducer(queue);

         prod.setDeliveryMode(DeliveryMode.PERSISTENT);
         for (int i = 0; i < 1000; i++)
         {
            TextMessage msg = session.createTextMessage("hello!");
            msg.setIntProperty("count", i);
            prod.send(msg);
         }


         conn.start();

         session.commit();


         MessageConsumer cons = session.createConsumer(queue);

         for (int i = 0 ; i < 5000; i++)
         {
            TextMessage txt = (TextMessage)cons.receive(5000);
            if (txt == null)
            {
               break;
            }
            System.out.println("Received " + txt.getText());
         }


         session.commit();


         conn.close();


         System.out.println("Done!");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
