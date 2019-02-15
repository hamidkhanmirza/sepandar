package com.fanap.sepandar;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.log4j.Logger;

import javax.jms.*;

/**
 * Created by admin123 on 2/13/2019.
 */
public class Test {
    static Logger logger = Logger.getLogger(Test.class);

    public static void main(String[] args) {
        Destination inputQueue;
        Destination outputQueue;
        ConnectionFactory factory;

        factory = new ActiveMQConnectionFactory(
                "root",
                "a@123456A",
                new StringBuilder()
                        .append("failover:(tcp://172.16.0.53:61616,tcp://172.16.0.54:61616)?jms.useAsyncSend=true")
                        .append("&jms.sendTimeout=").append(3000)
                        .append("&jms.prefetchPolicy.all=50")
                        .toString());

        if (factory != null) {
            logger.warn("start to connecting");

            try {
                logger.warn("connecting");
                Connection proConnection = factory.createConnection(
                        "root",
                        "a@123456A");
                proConnection.start();

                Session proSession = proConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                MessageProducer producer = proSession.createProducer(new ActiveMQQueue("queue1"));

            } catch (Exception e) {
                logger.warn("reconnecting exception " + e.toString() + e);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

    }

}
