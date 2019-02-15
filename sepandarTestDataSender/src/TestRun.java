import config.ConfigUtil;
import utils.Utility;

import java.io.File;

/**
 * Created by admin123 on 2/15/2019.
 */
public class TestRun {

    public static void main(String[] args) throws Exception {
        try (JMSClient jmsClient = new JMSClient()) {
            jmsClient.init();

            String photoTextContent = Utility.readFileToString(TestRun.class.getResource("/testData/photoTextTestData.txt").getFile());
            String tagTextContent = Utility.readFileToString(TestRun.class.getResource("/testData/tagTextTestData.txt").getFile());
            File photoBlobFile = new File(TestRun.class.getResource("/testData/FCO_1_281558174_1_1549214556000_85_12_1_1_79_11_125_1549214556000.jpg").getFile());

            jmsClient.sendMessage(photoTextContent, ConfigUtil.getConfig().getQueue().getPhotoTextQueue());
            jmsClient.sendMessage(tagTextContent, ConfigUtil.getConfig().getQueue().getTagTextQueue());
            jmsClient.sendBlobMessage(photoBlobFile, ConfigUtil.getConfig().getQueue().getPhotoBlobQueue());

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}
