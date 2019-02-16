package com.fanap.sepandar.message.requestHandler;

import com.fanap.sepandar.config.ConfigUtil;
import com.fanap.sepandar.message.messageParsers.PhotoBlobItemParser;
import com.fanap.sepandar.message.messageParsers.PhotoTextItemParser;
import com.fanap.sepandar.message.messageParsers.TagTextItemParser;
import com.fanap.sepandar.persistence.*;
import com.fanap.sepandar.sharedEntities.PhotoBlobMessage;
import com.fanap.sepandar.sharedEntities.PhotoTextMessage;
import com.fanap.sepandar.sharedEntities.TagTextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.hibernate.Transaction;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.IOException;
import java.util.List;

/**
 * Created by admin123 on 2/13/2019.
 */
@Component
public class AsyncRequestHandler implements IRequestHandler {
    public final static Logger logger = LogManager.getLogger();
    public final static Logger messageLogger = LogManager.getLogger("messageLogger");

    private Destination photoTextInputQueue;
    private Destination tagTextInputQueue;
    private Destination photoBlobQueue;

    ConnectionFactory factory;

    @Override
    public void init() throws Exception {
        photoTextInputQueue = new ActiveMQQueue(ConfigUtil.getConfig().getQueue().getPhotoTextQueue());
        tagTextInputQueue = new ActiveMQQueue(ConfigUtil.getConfig().getQueue().getTagTextQueue());
        photoBlobQueue = new ActiveMQQueue(ConfigUtil.getConfig().getQueue().getPhotoBlobQueue());

        String host = ConfigUtil.getConfig().getAsync().getHost();
        String userName = ConfigUtil.getConfig().getAsync().getUsername();
        String password = ConfigUtil.getConfig().getAsync().getPassword();


        factory = new ActiveMQConnectionFactory(
                userName,
                password,
                new StringBuilder()
                        .append("failover:(")
                        .append(host)
                        .append(")?jms.useAsyncSend=true")
                        .append("&jms.sendTimeout=").append(3000)
                        .append("&jms.prefetchPolicy.all=50")
                        .toString());


        if(factory != null) {
            for(int i = 0; i < 2; i++) {
                new Thread(new ConsumerRunnable()).start();
            }
        } else {
            throw new IOException("An error occured connecting to ");
        }
    }

    public class ConsumerRunnable implements java.lang.Runnable {
        private MessageConsumer photoTextInput_consumer;
        private MessageConsumer tagTextInput_consumer;
        private MessageConsumer photoBlob_consumer;

        private Connection connection;
        private Session session;

        private void connect() throws JMSException {
            try {
                logger.warn("connecting");
                close();
                connection = factory.createConnection(
                        ConfigUtil.getConfig().getAsync().getUsername(),
                        ConfigUtil.getConfig().getAsync().getPassword());

                connection.setExceptionListener(exception -> {
                    close();
                    logger.warn("JMSException occured", exception);
                    try {
                        Thread.sleep(ConfigUtil.getConfig().getAsync().getReconnectTimeout());
                        connect();
                    } catch (JMSException e) {
                        logger.warn("An exception occured ", e);
                    } catch (InterruptedException e) {
                        logger.warn("Interrupted Exception occured ", e);
                    }
                });

                connection.start();

                session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);

                photoTextInput_consumer = session.createConsumer(photoTextInputQueue);
                tagTextInput_consumer = session.createConsumer(tagTextInputQueue);
                photoBlob_consumer = session.createConsumer(photoBlobQueue);

                photoTextInput_consumer.setMessageListener(message -> {
                    org.hibernate.Session session1 = DaoService.Instance.getCurrentSession();

                    Transaction tx = null;
                    try {
                        String messageContent = getMessage(message);

                        Marker PHOTOTEXT_MARKER = MarkerManager.getMarker("PHOTOTEXT");
                        messageLogger.info(PHOTOTEXT_MARKER, messageContent);

                        List<PhotoTextMessage> photoTextMessageList = (List<PhotoTextMessage>) new PhotoTextItemParser().parseMessage(messageContent);

                        tx = session1.beginTransaction();

                        for (int i = 0; i < photoTextMessageList.size(); i++) {
                            PhotoTextDataService.INSTANCE.saveOrUpdate(photoTextMessageList.get(i), session1);
                        }

                        tx.commit();

                        message.acknowledge();
                    } catch (Exception ex) {
                        logger.error("Error occured processing photoTextMessage: ", ex);
                        if (tx != null) {
                            tx.rollback();
                        }

//                        try {
//                            session.rollback();
//                        } catch (JMSException e) {
//                            logger.error("Error occured rollingback session: ", e);
//                        }
                    } finally {
                        session1.close();
                    }


                });

                tagTextInput_consumer.setMessageListener(message -> {
                    org.hibernate.Session session1 = null;
                    try {
                        session1 = DaoService.Instance.getCurrentSession();
                        message.acknowledge();
                    } catch (SessionFactoryClosedException | JMSException ex) {
                        logger.error("Error: ", ex);
                        return;
                    }

                    Transaction tx = null;
                    try {
                        String messageContent = getMessage(message);

                        Marker TAGTEXT_MARKER = MarkerManager.getMarker("TAGTEXT");
                        messageLogger.info(TAGTEXT_MARKER, messageContent);

                        List<TagTextMessage> tagTextMessageList = (List<TagTextMessage>) new TagTextItemParser().parseMessage(messageContent);

                        tx = session1.beginTransaction();

                        for (int i = 0; i < tagTextMessageList.size(); i++) {
                            TextTagDataService.INSTANCE.saveOrUpdate(tagTextMessageList.get(i), session1);
                        }

                        tx.commit();

                        message.acknowledge();
                    } catch (Exception ex) {
                        logger.error("Error occured processing tagTextMessage: ", ex);
                        if (tx != null) {
                            tx.rollback();
                        }

//                        try {
//                            session.rollback();
//                        } catch (JMSException e) {
//                            logger.error("Error occured rollingback session: ", e);
//                        }
                    } finally {
                        session1.close();
                    }
                });

                photoBlob_consumer.setMessageListener(message -> {
                    org.hibernate.Session session1 = null;
                    try {
                        session1 = DaoService.Instance.getCurrentSession();
                        message.acknowledge();
                    } catch (SessionFactoryClosedException | JMSException ex) {
                        logger.error("Error: ", ex);
                        return;
                    }

                    Transaction tx = null;
                    try {
                        String messageContent = getMessage(message);

//                    Marker TAGTEXT_MARKER = MarkerManager.getMarker("PHOTOBLOB");
//                    messageLogger.info(TAGTEXT_MARKER, messageContent);

                        List<PhotoBlobMessage> photoBlobMessageList = (List<PhotoBlobMessage>) new PhotoBlobItemParser().parseMessage(messageContent);

                        tx = session1.beginTransaction();

                        for (int i = 0; i < photoBlobMessageList.size(); i++) {
                            PhotoBlobDataService.INSTANCE.saveOrUpdate(photoBlobMessageList.get(i), session1);
                        }

                        tx.commit();

                        message.acknowledge();
                    } catch (Exception ex) {
                        logger.error("Error occured processing photoBlobMessage: ", ex);
                        if (tx != null) {
                            tx.rollback();
                        }

//                        try {
//                            session.rollback();
//                        } catch (JMSException e) {
//                            logger.error("Error occured rollingback session: ", e);
//                        }
                    } finally {
                        session1.close();
                    }
                });

                logger.info("connection established");
            } catch (Exception e) {
                logger.warn("reconnecting exception " + e.toString() + e);
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                close();
            }
        }

        private String getMessage(Message message) throws Exception {
            if (message instanceof BytesMessage) {
                BytesMessage bytesMessage = (BytesMessage) message;
                byte[] buffer = new byte[(int) bytesMessage.getBodyLength()];
                int readBytes = bytesMessage.readBytes(buffer);
                if (readBytes != bytesMessage.getBodyLength()) {
                    throw new IOException("Inconsistant message length");
                }
                return new String(buffer, "utf-8");
            }
            return null;
        }

        private void close() {
            try {
                if (photoTextInput_consumer != null)
                    photoTextInput_consumer.close();
            } catch (Exception e) {
                logger.warn("exception photoTextInput_consumer close  : ", e);
            }

            try {
                if (tagTextInput_consumer != null)
                    tagTextInput_consumer.close();
            } catch (Exception e) {
                logger.warn("exception tagTextInput_consumer close  : ", e);
            }

            try {
                if (photoBlob_consumer != null)
                    photoBlob_consumer.close();
            } catch (Exception e) {
                logger.warn("exception photoBlob_consumer close  : ", e);
            }

            try {
                if (session != null)
                    session.close();
            } catch (Exception ex) {
                logger.warn("exception close session : ", ex);
            }

            try {
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                logger.warn("exception close connection : ", e);
            }
        }


        @Override
        public void run() {
            try {
                connect();
            } catch (Exception ex) {
                logger.error("Error connecting ", ex);
            }
        }
    }


    public static void main(String[] args) {
    }

}
