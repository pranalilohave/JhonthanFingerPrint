package in.co.ashclan.model;

import java.io.Serializable;

public class EventAttendancePOJO  implements Serializable {
    String id, eventId, userId, memberId, familyId, anonymous;
    String date,eventName;
    String createdAt, updatedAt;
    String attenDate,attenTime;

    @Override
    public String toString() {
        return "EventAttendancePOJO{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", userId='" + userId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", familyId='" + familyId + '\'' +
                ", anonymous='" + anonymous + '\'' +
                ", date='" + date + '\'' +
                ", eventName='" + eventName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", attenDate='" + attenDate + '\'' +
                ", attenTime='" + attenTime + '\'' +
                '}';
    }

    public EventAttendancePOJO(String id, String eventId, String userId, String memberId, String familyId, String anonymous, String date, String eventName, String createdAt, String updatedAt, String attenDate, String attenTime) {
        this.id = id;
        this.eventId = eventId;
        this.userId = userId;
        this.memberId = memberId;
        this.familyId = familyId;
        this.anonymous = anonymous;
        this.date = date;
        this.eventName = eventName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.attenDate = attenDate;
        this.attenTime = attenTime;
    }

    public EventAttendancePOJO(String date, String eventName) {
        this.date = date;
        this.eventName = eventName;
    }



    public EventAttendancePOJO() {
        this.id = "";
        this.eventId = "";
        this.userId = "";
        this.memberId = "";
        this.familyId = "";
        this.anonymous = "";
        this.date = "";
        this.createdAt = "";
        this.updatedAt = "";
        this.eventName = "";
        this.attenDate = "";
        this.attenTime = "";
    }

    public String getAttenDate() {
        return attenDate;
    }

    public void setAttenDate(String attenDate) {
        this.attenDate = attenDate;
    }

    public String getAttenTime() {
        return attenTime;
    }

    public void setAttenTime(String attenTime) {
        this.attenTime = attenTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}