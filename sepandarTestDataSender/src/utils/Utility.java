package utils;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by admin123 on 2/15/2019.
 */
public class Utility {
    public static String readFileToString(String filePath) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(filePath));

        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
            line = br.readLine();
        }

        return sb.toString();
    }
}
