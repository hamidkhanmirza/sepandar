package com.fanap.sepandar.persistence;

import com.fanap.sepandar.config.ConfigUtil;
import com.fanap.sepandar.utils.sharedClasses.myCompleteableCall.MyUniCompleableCall;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

import java.sql.*;
import java.util.Locale;

/**
 * Created by admin123 on 8/13/2016.
 */
public class DaoService {
    public final static Logger logger = LogManager.getLogger("appLogger");
    public final static DaoService Instance = new DaoService();

    private static final ThreadLocal<Session> currentSession = new ThreadLocal<Session>();
    private SessionFactory sessionFactory;
    private Configuration conf;

    private DaoService() {
    }

    public void init() {
        logger.debug("initializing Dao configuration .... ");
        try {
            conf = new Configuration();
            conf = conf.configure("hibernate.cfg.xml");
            conf.setProperty("hibernate.connection.url", ConfigUtil.getConfig().getDb().getUrl());
            conf.setProperty("hibernate.connection.username", ConfigUtil.getConfig().getDb().getUsername());

            String dbPassword = ConfigUtil.getConfig().getDb().getPassword();
            conf.setProperty("hibernate.connection.password", dbPassword);
            Locale.setDefault(Locale.ENGLISH);

            conf.setProperty("hibernate.connection.CharSet", "UTF-8");
            conf.setProperty("hibernate.connection.characterEncoding", "UTF-8");
            conf.setProperty("hibernate.connection.useUnicode", "true");
            conf.setProperty("automaticTestTable", "autoTestTable");
            conf.setProperty("testConnectionOnCheckout", "true");

            sessionFactory = conf.buildSessionFactory();
            logger.debug("end Dao configuration ....");
        } catch (Exception ex) {
            throw new RuntimeException("Error initializing dao ", ex);
        }
    }

    public Session getCurrentSession() {
        Session session = currentSession.get();
        if (!sessionFactory.isClosed()) {
            if (session == null || !session.isOpen()) {
                session = sessionFactory.openSession();
                currentSession.set(session);
            }
            return session;
        } else {
            init();
            throw new SessionFactoryClosedException();
        }
    }

    public Session getNewSession() {
        return sessionFactory.openSession();
    }

    public void closeSessionFactory() {
        if (sessionFactory != null)
            sessionFactory.close();
    }

    public MyUniCompleableCall createTablesOnDatabaseForFirstTime() {

        MyUniCompleableCall myUniCompleableCall = new MyUniCompleableCall();
        Session session = getCurrentSession();

        session.doWork(connection -> {
            try {

                DatabaseMetaData dbm = connection.getMetaData();

                ResultSet tables = dbm.getTables(null, null, "TBL_PHOTOTEXTMSG", null);
                if (!tables.next()) {
                    Configuration conf1 = new Configuration();
                    conf1 = conf1.configure("hibernate.cfg.xml");
                    conf1.setProperty("hibernate.connection.url", ConfigUtil.getConfig().getDb().getUrl());
                    conf1.setProperty("hibernate.connection.username", ConfigUtil.getConfig().getDb().getUsername());
                    conf1.setProperty("hibernate.connection.password", ConfigUtil.getConfig().getDb().getPassword());

                    SchemaUpdate schemaUpdate = new SchemaUpdate(conf1);
                    schemaUpdate.execute(true, true);
                }

                myUniCompleableCall.complete(true);
            } catch (Exception ex) {
                myUniCompleableCall.completeExceptionally(ex);
            } finally {
                session.close();
            }
        });

        return myUniCompleableCall;
    }

    public static void main(String[] args) {
//        Configuration conf1 = new Configuration();
//        conf1 = conf1.configure("hibernate.cfg.xml");
//        conf1.setProperty("hibernate.connection.url", ConfigUtil.getConfig().getDb().getUrl());
//        conf1.setProperty("hibernate.connection.username", ConfigUtil.getConfig().getDb().getUsername());
//        conf1.setProperty("hibernate.connection.password", ConfigUtil.getConfig().getDb().getPassword());
//
//        SchemaUpdate schemaUpdate = new SchemaUpdate(conf1);
//        schemaUpdate.execute(true, false);
    }
}
