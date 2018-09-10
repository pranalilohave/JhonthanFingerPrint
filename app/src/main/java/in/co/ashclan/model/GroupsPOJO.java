package in.co.ashclan.model;

import java.io.Serializable;

public class GroupsPOJO implements Serializable {

       String Id,memberId,userId,tagId,name,notes;

    public GroupsPOJO( String memberId, String userId, String tagId, String name, String notes) {
        this.memberId = memberId;
        this.userId = userId;
        this.tagId = tagId;
        this.name = name;
        this.notes = notes;
    }

    public GroupsPOJO() {
    }

    @Override
    public String toString() {
        return "GroupsPOJO{" +
                "Id='" + Id + '\'' +
                ", memberId='" + memberId + '\'' +
                ", userId='" + userId + '\'' +
                ", tagId='" + tagId + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
