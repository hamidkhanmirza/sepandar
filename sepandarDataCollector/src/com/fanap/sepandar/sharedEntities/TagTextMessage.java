package com.fanap.sepandar.sharedEntities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by admin123 on 2/13/2019.
 */
@Entity
@Table(name = "TBL_TAGTEXTMSG")
@GenericGenerator(name = "sequence_generator",
        strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
        parameters = {
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SEQ_TAG_TEXT"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.OPT_PARAM, value = "pooled"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INITIAL_PARAM, value = "1000"),
                @org.hibernate.annotations.Parameter(name = SequenceStyleGenerator.INCREMENT_PARAM, value = "1")
        }
)
public class TagTextMessage extends BaseMessage {
    @Id
    String rFID;

    Long time;
    Integer c_code;
    Integer g_code;
    Integer s_code;

    public String getrFID() {
        return rFID;
    }

    public void setrFID(String rFID) {
        this.rFID = rFID;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Integer getC_code() {
        return c_code;
    }

    public void setC_code(Integer c_code) {
        this.c_code = c_code;
    }

    public Integer getG_code() {
        return g_code;
    }

    public void setG_code(Integer g_code) {
        this.g_code = g_code;
    }

    public Integer getS_code() {
        return s_code;
    }

    public void setS_code(Integer s_code) {
        this.s_code = s_code;
    }
}
