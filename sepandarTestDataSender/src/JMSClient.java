import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import config.ConfigUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.command.ActiveMQQueue;
import org.joda.time.Instant;
import org.json.JSONObject;
import utils.JsonUtil;

import javax.jms.*;
import java.io.File;
import java.nio.file.Files;

/**
 * Created by admin123 on 2/15/2019.
 */
public class JMSClient implements AutoCloseable {
    String host = ConfigUtil.getConfig().getAsync().getHost();
    String userName = ConfigUtil.getConfig().getAsync().getUsername();
    String password = ConfigUtil.getConfig().getAsync().getPassword();
    Connection connection;
    Session session;

    public void init() throws Exception {
        ConnectionFactory factory = new ActiveMQConnectionFactory(
                userName,
                password,
                new StringBuilder()
                        .append("failover:(")
                        .append(host)
                        .append(")?jms.useAsyncSend=true")
                        .append("&jms.sendTimeout=").append(3000)
                        .append("&jms.blobTransferPolicy.defaultUploadUrl=").append(ConfigUtil.getConfig().getFileServer().getUrl())
                        .toString());

        connection = factory.createConnection(
                ConfigUtil.getConfig().getAsync().getUsername(),
                ConfigUtil.getConfig().getAsync().getPassword());

        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

    }

    public void sendMessage(String message, String queueName) throws Exception {
        MessageProducer producer = null;
        try {
            producer = session.createProducer(new ActiveMQQueue(queueName));

            byte[] bytes = message.getBytes("utf-8");
            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(bytes);
            producer.send(bytesMessage);

        } catch (Exception ex) {
            throw ex;
        } finally {
            if (producer != null)
                producer.close();
        }
    }

    public void sendBlobMessage(File file, String queueName) throws Exception {
        MessageProducer producer = null;

        try {
            producer = session.createProducer(new ActiveMQQueue(queueName));

            Instant start = Instant.now();

            byte[] fileContent = Files.readAllBytes(file.toPath());

            FileDescriptor fileDescriptor = new FileDescriptor();
            fileDescriptor.setFileName(file.getName());
            fileDescriptor.setBase64FileData(Base64.encode(fileContent));

            String stringifiedFileDescriptor = JsonUtil.getJson(fileDescriptor);

            BytesMessage bytesMessage = session.createBytesMessage();
            bytesMessage.writeBytes(stringifiedFileDescriptor.getBytes("UTF-8"));

            producer.send(bytesMessage);

            Instant end = Instant.now();

            System.out.println("sending file with name " + file.getName() + " finished! elapsed time is " + (end.getMillis() - start.getMillis()));
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (producer != null)
                producer.close();
        }
    }

    public void close() throws Exception {
        connection.close();
    }
}
