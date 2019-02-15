package com.fanap.sepandar.message.messageParsers;

import com.fanap.sepandar.fileserver.FileServerService;
import com.fanap.sepandar.sharedEntities.BaseMessage;
import com.fanap.sepandar.sharedEntities.PhotoBlobMessage;
import com.fanap.sepandar.utils.JsonUtil;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.util.Arrays;
import java.util.List;

/**
 * Created by admin123 on 2/15/2019.
 */
public class PhotoBlobItemParser implements IMessageParser {
    @Override
    public List<? extends BaseMessage> parseMessage(String message) throws Exception {
        FileDescriptor fileDescriptor = JsonUtil.getObject(message, FileDescriptor.class);

        String fileName = fileDescriptor.getFileName();
        String base64FileData = fileDescriptor.getBase64FileData();

        byte[] realFileData = Base64.decode(base64FileData);
        String fileKey = FileServerService.Instance.uploadFileToFileServer(fileName, realFileData);

        FileServerService.Instance.persistFileToServer(fileKey);

        PhotoBlobMessage photoBlobMessage = new PhotoBlobMessage();
        photoBlobMessage.setFileKeyOnFileServer(fileKey);
        photoBlobMessage.setFileName(fileName);

        return Arrays.asList(photoBlobMessage);
    }
}
