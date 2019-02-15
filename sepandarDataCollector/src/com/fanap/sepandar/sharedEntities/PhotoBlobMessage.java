package com.fanap.sepandar.sharedEntities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Created by admin123 on 2/13/2019.
 */
@Entity
@Table(name = "TBL_PHOTOBLOBMSG")
@GenericGenerator(name = "sequence_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SEQ_PHOTO_BLOB"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1000"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")
        }
)
public class PhotoBlobMessage extends BaseMessage {
    @Id
    String fileName;

    @Transient
    byte[] fileData;

    String fileKeyOnFileServer;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public String getFileKeyOnFileServer() {
        return fileKeyOnFileServer;
    }

    public void setFileKeyOnFileServer(String fileKeyOnFileServer) {
        this.fileKeyOnFileServer = fileKeyOnFileServer;
    }
}
