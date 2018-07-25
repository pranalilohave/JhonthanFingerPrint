package in.co.ashclan.model;

import java.io.Serializable;

public class GroupsPOJO implements Serializable {

       String Id,memberId,userId,tagId;

    @Override
    public String toString() {
        return  "GroupPOJO{" +
                "id='" + Id + '\'' +
                ",userId='" + userId + '\'' +
                ",memberId='" + memberId + '\'' +
                ",tagId='" + tagId + '\'' +
                '}';
    }

    public GroupsPOJO() {
        Id = "";
        this.memberId = "";
        this.userId = "";
        this.tagId = "";
    }

    public GroupsPOJO(String id, String memberId, String userId, String tagId) {
        Id = id;
        this.memberId = memberId;
        this.userId = userId;
        this.tagId = tagId;
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
}
