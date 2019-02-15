package com.fanap.sepandar.fileserver;

import com.fanap.sepandar.config.ConfigUtil;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin123 on 7/11/2016.
 */
public class FileServerService {
    public final static FileServerService Instance = new FileServerService();

    final static String fileServerURL = ConfigUtil.getConfig().getFileServer().getUrl();

    public final static String FILE_DOWNLOAD_SERVER_PATH;
    public final static String FILE_UPLOAD_SERVER_PATH;
    public final static String FILE_PERSIST_SERVER_PATH;
    public final static String FILE_DELETE_SERVER_PATH;
    public final static String FILE_EXIST_QUERY_SERVER_PATH;
    public final static String FILE_SERVER_DOWNLOAD_COUNT;
    public final static String FILE_EXIST_QUERY_SERVER_PACKAGE_FILE_SIZE;

    static {
        FILE_DOWNLOAD_SERVER_PATH = fileServerURL + "download?key=${key}";
        FILE_UPLOAD_SERVER_PATH = fileServerURL + "upload";
        FILE_PERSIST_SERVER_PATH = fileServerURL + "persist?key=${key}";
        FILE_DELETE_SERVER_PATH = fileServerURL + "delete?key=${key}";
        FILE_EXIST_QUERY_SERVER_PATH = fileServerURL + "isExist?key=${key}";
        FILE_SERVER_DOWNLOAD_COUNT = fileServerURL + "getNumber?key=${key}";
        FILE_EXIST_QUERY_SERVER_PACKAGE_FILE_SIZE = fileServerURL + "getSize?key=${key}";
    }

    public static class FileDeleteException extends RuntimeException {
        public FileDeleteException(String message, Exception ex) {
            super(message, ex);
        }

        public FileDeleteException(String message) {
            super(message);
        }

        public FileDeleteException(Exception ex) {
            super(ex);
        }
    }

    public static class FileDownloadException extends RuntimeException {
        public FileDownloadException(String message, Exception ex) {
            super(message, ex);
        }

        public FileDownloadException(String message) {
            super(message);
        }

        public FileDownloadException(Exception ex) {
            super(ex);
        }
    }

    public static class FileUploadException extends RuntimeException {
        public FileUploadException(String message, Exception ex) {
            super(message, ex);
        }

        public FileUploadException(String message) {
            super(message);
        }

        public FileUploadException(Exception ex) {
            super(ex);
        }
    }

    public static class UploadDescriptor {
        boolean persist;
        boolean inChunk;
        int chunkSize;

        public boolean isPersist() {
            return persist;
        }

        public void setPersist(boolean persist) {
            this.persist = persist;
        }

        public boolean isInChunk() {
            return inChunk;
        }

        public void setInChunk(boolean inChunk) {
            this.inChunk = inChunk;
        }

        public int getChunkSize() {
            return chunkSize;
        }

        public void setChunkSize(int chunkSize) {
            this.chunkSize = chunkSize;
        }
    }

    public void downloadFileFromServer(String fileKey, OutputStream out) throws FileDownloadException, IOException {
        String fileDownloadURL = FILE_DOWNLOAD_SERVER_PATH;
        String concreateDownLoadURL = fileDownloadURL.replace("${key}", fileKey);
        try (InputStream input = new URL(concreateDownLoadURL).openStream()) {
            IOUtils.copy(input, out);
            out.flush();
        } catch (Exception ex) {
            throw new FileDownloadException(ex);
        } finally {
            out.close();
        }
    }

    public String uploadFileToFileServer(String fileName, byte[] fileData) throws Exception {
        String url = FILE_UPLOAD_SERVER_PATH;


        HttpEntity entity = MultipartEntityBuilder.create()
                .addPart("file", new ByteArrayBody(fileData, fileName))
                .build();

        HttpPost request = new HttpPost(url);
        request.setEntity(entity);

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);

        if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
            throw new RuntimeException("file not uploaded successfully");
        }

        String responseString = new BasicResponseHandler().handleResponse(response);
        return responseString;
    }

    public void deleteFileFromServer(String fileKey) throws FileDeleteException {
        try {
            String fileDeleteURL = FILE_DELETE_SERVER_PATH;
            String concreteDeleteURL = fileDeleteURL.replace("${key}", fileKey);

            URLConnection connection = new URL(concreteDeleteURL).openConnection();
            connection.setDoOutput(true);

            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new RuntimeException("file not deleted successfuly!response returned from server is " + responseCode);
            }

        } catch (Exception ex) {
            throw new FileDeleteException(ex.getMessage(), ex);
        }
    }

    public void persistFileToServer(String fileKey) {
        String persistURL = FILE_PERSIST_SERVER_PATH;
        try {
            fileKey = fileKey.substring(0, fileKey.length());
            String tempPersistURL = persistURL.replace("${key}", fileKey);
            URL url = new URL(tempPersistURL);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();
            if (responseCode != HttpStatus.SC_OK) {
                throw new FileUploadException("error in persisting file on upload server!");
            }
        } catch (Exception ex) {
            throw new FileUploadException(ex.getMessage(), ex);
        }
    }

    public String copyFileFromServerToTemp(String fileKeyInFileServer) throws Exception {
        String tempLocation = System.getProperty("java.io.tmpdir");
        String tempFileName = tempLocation + "/" + fileKeyInFileServer;
        FileOutputStream fout = new FileOutputStream(tempFileName);
        downloadFileFromServer(fileKeyInFileServer, fout);
        return tempFileName;
    }

    public String getDownloadURL(String key) {
        String downloadPath = FILE_DOWNLOAD_SERVER_PATH;
        downloadPath = downloadPath.replace("${key}", key);
        return downloadPath;
    }

}

