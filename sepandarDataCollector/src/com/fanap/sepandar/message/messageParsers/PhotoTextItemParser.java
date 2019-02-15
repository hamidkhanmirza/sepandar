package com.fanap.sepandar.message.messageParsers;

import com.fanap.sepandar.jsEngine.JavascriptEngine;
import com.fanap.sepandar.sharedEntities.BaseMessage;
import com.fanap.sepandar.sharedEntities.PhotoTextMessage;
import com.fanap.sepandar.utils.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

/**
 * Created by admin123 on 2/14/2019.
 */
public class PhotoTextItemParser implements IMessageParser {

    @Override
    public List<? extends BaseMessage> parseMessage(String message) throws Exception {
        String parsedData = JavascriptEngine.evaluateInput("photoTextItemParser", "parsePhotoTextItems", message);

        List<PhotoTextMessage> photoTextMessageList = JsonUtil.getObject(parsedData, new TypeReference<List<PhotoTextMessage>>(){});

        return photoTextMessageList;
    }
}
