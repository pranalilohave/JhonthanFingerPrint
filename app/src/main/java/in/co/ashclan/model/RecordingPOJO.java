package in.co.ashclan.model;

import java.io.Serializable;

public class RecordingPOJO implements Serializable {
    String eventid,userid,filename,updatedat,createdat,eventDate;

    public RecordingPOJO(String eventid, String userid, String filename, String updatedat, String createdat, String eventDate) {
        this.eventid = eventid;
        this.userid = userid;
        this.filename = filename;
        this.updatedat = updatedat;
        this.createdat = createdat;
        this.eventDate = eventDate;
    }

    public RecordingPOJO() {
    }

    @Override
    public String toString() {
        return "RecordingPOJO{" +
                "eventid='" + eventid + '\'' +
                ", userid='" + userid + '\'' +
                ", filename='" + filename + '\'' +
                ", updatedat='" + updatedat + '\'' +
                ", createdat='" + createdat + '\'' +
                ", eventDate='" + eventDate + '\'' +
                '}';
    }

    public String getEventid() {
        return eventid;
    }

    public void setEventid(String eventid) {
        this.eventid = eventid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getUpdatedat() {
        return updatedat;
    }

    public void setUpdatedat(String updatedat) {
        this.updatedat = updatedat;
    }

    public String getCreatedat() {
        return createdat;
    }

    public void setCreatedat(String createdat) {
        this.createdat = createdat;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }
}
