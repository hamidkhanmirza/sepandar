package com.fanap.sepandar.persistence;

import com.fanap.sepandar.sharedEntities.BaseMessage;
import org.hibernate.Session;

/**
 * Created by admin123 on 2/15/2019.
 */
public class BaseDataService {

    protected void saveOrUpdate(BaseMessage baseMessage, Session session) throws Exception {
        session.saveOrUpdate(baseMessage);
    }
}
