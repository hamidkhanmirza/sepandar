package com.fanap.sepandar.persistence;

import com.fanap.sepandar.sharedEntities.TagTextMessage;
import org.hibernate.Session;

/**
 * Created by admin123 on 2/15/2019.
 */
public class TextTagDataService extends BaseDataService {
    public static final TextTagDataService INSTANCE = new TextTagDataService();

    private TextTagDataService() {}

    public void saveOrUpdate(TagTextMessage tagTextMessage, Session session) throws Exception {
        super.saveOrUpdate(tagTextMessage, session);
    }
}
