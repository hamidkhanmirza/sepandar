package com.fanap.sepandar.config;

/**
 * Created by admin123 on 2/13/2019.
 */
public class Async {
    String host;
    String username;
    String password;
    Integer reconnectTimeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getReconnectTimeout() {
        return reconnectTimeout;
    }

    public void setReconnectTimeout(Integer reconnectTimeout) {
        this.reconnectTimeout = reconnectTimeout;
    }
}
