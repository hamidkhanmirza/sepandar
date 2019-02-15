package config;

/**
 * Created by admin123 on 2/13/2019.
 */
public class Config {
    Async async;
    Queue queue;
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

    public FileServer getFileServer() {
        return fileServer;
    }

    public void setFileServer(FileServer fileServer) {
        this.fileServer = fileServer;
    }
}
