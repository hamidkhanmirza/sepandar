import config.ConfigUtil;
import org.apache.wicket.ajax.json.JSONObject;
import utils.Utility;

import java.io.File;
import java.time.Instant;

/**
 * Created by admin123 on 2/15/2019.
 */
public class TestRun {


    public static void main(String[] args) throws Exception {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try (JMSClient jmsClient = new JMSClient()) {
                        jmsClient.init();

                        JSONObject jsonObject = new JSONObject();
                        String currentMilli = String.valueOf(Instant.now().toEpochMilli());
                        String randomString = "_1_" + currentMilli + "_1_" + currentMilli + "_85_12_1_1_79_11_125_" + currentMilli + ".jpg";
                        jsonObject.put("FCO", "FCO" + randomString);
                        jsonObject.put("BIR", "BIR" + randomString);
                        jsonObject.put("PFR", "PFR" + randomString);
                        jsonObject.put("FIR", "FIR" + randomString);
                        jsonObject.put("PBA", "PBA" + randomString);
                        jsonObject.put("BCO", "BCO" + randomString);

                        String photoTextContent = jsonObject.toString();

                        JSONObject jsonObject2 = new JSONObject();
                        jsonObject2.put("RFID", String.valueOf(Instant.now().toEpochMilli()));
                        jsonObject2.put("time", String.valueOf(Instant.now().toEpochMilli()));
                        jsonObject2.put("c_code", "354");
                        jsonObject2.put("g_code", "23322");
                        jsonObject2.put("s_code", "212344");


                        String tagTextContent = jsonObject2.toString();
                        File photoBlobFile = new File(TestRun.class.getResource("/testData/FCO_1_281558174_1_1549214556000_85_12_1_1_79_11_125_1549214556000.jpg").getFile());

                        jmsClient.sendMessage(photoTextContent, ConfigUtil.getConfig().getQueue().getPhotoTextQueue());
                        jmsClient.sendMessage(tagTextContent, ConfigUtil.getConfig().getQueue().getTagTextQueue());
                        jmsClient.sendBlobMessage(photoBlobFile, ConfigUtil.getConfig().getQueue().getPhotoBlobQueue());

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    } finally {
//                         System.exit(0);
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };


        for(int i = 0; i < 50; i++) {
            new Thread(runnable).start();
        }

    }
}
