package com.fanap.sepandar;

import com.fanap.sepandar.persistence.DaoService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by admin123 on 2/14/2019.
 */
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = AppConfig.class)
public class SepandarServlet extends HttpServlet {
    static final Logger logger = LogManager.getLogger("appLogger");

    @Autowired
    com.fanap.sepandar.message.requestHandler.IRequestHandler requestHandler;


    @Override
    public void init() throws ServletException {
        super.init();

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        requestHandler = (com.fanap.sepandar.message.requestHandler.IRequestHandler) context.getBean("requestHandler");

        logger.debug("*************************************************************************************************");
        logger.debug("                               Initializing Sepandar Data Collector                              ");
        logger.debug("*************************************************************************************************");

        DaoService.Instance.init();

        DaoService.Instance.createTablesOnDatabaseForFirstTime().thenApply(o -> {
            try {
                requestHandler.init();
            } catch (Exception ex) {
                logger.error("Error occured initializing requestHandler! ", ex);
                throw new RuntimeException(ex);
            }
        }).exceptionally(ex -> {
            throw new RuntimeException((Exception)ex);
        });
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Sepandar Server Started Successfully...");
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Sepandar Server Started Successfully...");
    }
}
