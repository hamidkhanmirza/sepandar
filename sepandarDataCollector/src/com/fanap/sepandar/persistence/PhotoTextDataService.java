package com.fanap.sepandar.persistence;

import com.fanap.sepandar.sharedEntities.PhotoTextMessage;
import org.hibernate.Session;

/**
 * Created by admin123 on 2/15/2019.
 */
public class PhotoTextDataService extends BaseDataService {
    public static final PhotoTextDataService INSTANCE = new PhotoTextDataService();

    private PhotoTextDataService() {}

    public void saveOrUpdate(PhotoTextMessage photoTextMessage, Session session) throws Exception {
        super.saveOrUpdate(photoTextMessage, session);
    }
}
