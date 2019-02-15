package com.fanap.sepandar.sharedEntities;

import com.fanap.sepandar.message.messageParsers.PhotoTextItemParser;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

/**
 * Created by admin123 on 2/13/2019.
 */
@Entity
@Table(
        name = "TBL_PHOTOTEXTMSG"
)
@GenericGenerator(name = "sequence_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SEQ_PHOTO_TEXT"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1000"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")
        }
)
public class PhotoTextMessage extends BaseMessage {
    @Id
    String id;

    String imageCode;
    String plateType;
    String plateText;
    String laneCode;
    Long dateTime;
    Integer camAccuracy;
    Integer camCode;
    Integer direction;
    Integer classType;
    Integer classAccuracy;
    Integer tspCode;
    Integer passageCode;
    Long firstPicDateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageCode() {
        return imageCode;
    }

    public void setImageCode(String imageCode) {
        this.imageCode = imageCode;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getPlateText() {
        return plateText;
    }

    public void setPlateText(String plateText) {
        this.plateText = plateText;
    }

    public String getLaneCode() {
        return laneCode;
    }

    public void setLaneCode(String laneCode) {
        this.laneCode = laneCode;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getCamAccuracy() {
        return camAccuracy;
    }

    public void setCamAccuracy(Integer camAccuracy) {
        this.camAccuracy = camAccuracy;
    }

    public Integer getCamCode() {
        return camCode;
    }

    public void setCamCode(Integer camCode) {
        this.camCode = camCode;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public Integer getClassType() {
        return classType;
    }

    public void setClassType(Integer classType) {
        this.classType = classType;
    }

    public Integer getClassAccuracy() {
        return classAccuracy;
    }

    public void setClassAccuracy(Integer classAccuracy) {
        this.classAccuracy = classAccuracy;
    }

    public Integer getTspCode() {
        return tspCode;
    }

    public void setTspCode(Integer tspCode) {
        this.tspCode = tspCode;
    }

    public Integer getPassageCode() {
        return passageCode;
    }

    public void setPassageCode(Integer passageCode) {
        this.passageCode = passageCode;
    }

    public Long getFirstPicDateTime() {
        return firstPicDateTime;
    }

    public void setFirstPicDateTime(Long firstPicDateTime) {
        this.firstPicDateTime = firstPicDateTime;
    }
}
