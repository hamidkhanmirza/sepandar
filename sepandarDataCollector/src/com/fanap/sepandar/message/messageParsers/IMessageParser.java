package com.fanap.sepandar.message.messageParsers;

import com.fanap.sepandar.sharedEntities.BaseMessage;

import java.util.List;

/**
 * Created by admin123 on 2/14/2019.
 */
public interface IMessageParser {
    public <T extends BaseMessage> List<T> parseMessage(String message) throws Exception;

}
