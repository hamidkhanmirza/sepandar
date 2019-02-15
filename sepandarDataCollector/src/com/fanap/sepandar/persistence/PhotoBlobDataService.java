package com.fanap.sepandar.persistence;

import com.fanap.sepandar.sharedEntities.PhotoBlobMessage;
import org.hibernate.Session;

/**
 * Created by admin123 on 2/15/2019.
 */
public class PhotoBlobDataService extends BaseDataService {
    public static final PhotoBlobDataService INSTANCE = new PhotoBlobDataService();

    private PhotoBlobDataService() {}

    public void saveOrUpdate(PhotoBlobMessage photoBlobMessage, Session session) throws Exception {
        super.saveOrUpdate(photoBlobMessage, session);
    }
}
