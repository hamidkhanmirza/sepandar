package com.fanap.sepandar.config;

/**
 * Created by admin123 on 2/13/2019.
 */
public class Config {
    Async async;
    Queue queue;
    DB db;
    FileServer fileServer;

    public Async getAsync() {
        return async;
    }

    public void setAsync(Async async) {
        this.async = async;
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public FileServer getFileServer() {
        return fileServer;
    }

    public void setFileServer(FileServer fileServer) {
        this.fileServer = fileServer;
    }
}
