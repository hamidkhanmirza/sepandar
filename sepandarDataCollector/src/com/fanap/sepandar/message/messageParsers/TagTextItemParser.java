package com.fanap.sepandar.message.messageParsers;

import com.fanap.sepandar.jsEngine.JavascriptEngine;
import com.fanap.sepandar.sharedEntities.BaseMessage;
import com.fanap.sepandar.sharedEntities.TagTextMessage;
import com.fanap.sepandar.utils.JsonUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by admin123 on 2/15/2019.
 */
public class TagTextItemParser implements IMessageParser {
    @Override
    public List<? extends BaseMessage> parseMessage(String message) throws Exception {
        String parsedData = JavascriptEngine.evaluateInput("tagTextItemParser", "parseTagTextItems", message);

        TagTextMessage tagTextMessage = JsonUtil.getObject(parsedData, TagTextMessage.class);

        return Arrays.asList(tagTextMessage);
    }
}
