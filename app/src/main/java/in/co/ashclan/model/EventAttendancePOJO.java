package in.co.ashclan.model;

import java.io.Serializable;

public class EventAttendancePOJO  implements Serializable {
    String id, eventId, userId, memberId, familyId, anonymous;
    String date,eventName;
    String createdAt, updatedAt;

    @Override
    public String toString() {
        return "EventAttendancePOJO{" +
                "id='" + id + '\'' +
                ", eventId='" + eventId + '\'' +
                ", userId='" + userId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", anonymous='" + anonymous + '\'' +
                ", date='" + date + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", eventName='" + eventName + '\'' +
                '}';
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