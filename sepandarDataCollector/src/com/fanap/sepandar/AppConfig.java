package com.fanap.sepandar;

import com.fanap.sepandar.message.requestHandler.AsyncRequestHandler;
import com.fanap.sepandar.message.requestHandler.IRequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by admin123 on 2/13/2019.
 */

@Configuration
@ComponentScan(basePackages = "com.fanap.sepandar.message.requestHandler")
public class AppConfig {

    @Bean(name="requestHandler")
    public IRequestHandler requestHandler() {
        return new AsyncRequestHandler();
    }

}
