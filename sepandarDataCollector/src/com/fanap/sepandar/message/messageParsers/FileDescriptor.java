package com.fanap.sepandar.message.messageParsers;

/**
 * Created by admin123 on 2/15/2019.
 */
public class FileDescriptor {
    String fileName;
    String base64FileData;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBase64FileData() {
        return base64FileData;
    }

    public void setBase64FileData(String base64FileData) {
        this.base64FileData = base64FileData;
    }
}
