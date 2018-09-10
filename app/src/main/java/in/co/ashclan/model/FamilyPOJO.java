package in.co.ashclan.model;

public class FamilyPOJO {
    /*"family": [
    {
        "id": 1,"branch_id": null,"user_id": 1,"member_id": 6,"name": "Attipoe",
        "notes": null,"picture": null,"created_at": "2018-02-03 13:28:25","updated_at": "2018-02-03 13:28:25"
    }
*/
String id,branchId,userId,memberId,name,notes,picture,createdAt,updatedAt,familyid,role;

    public FamilyPOJO(String id, String branchId, String userId, String memberId, String name, String notes, String picture, String createdAt, String updatedAt, String familyid, String role) {
        this.id = id;
        this.branchId = branchId;
        this.userId = userId;
        this.memberId = memberId;
        this.name = name;
        this.notes = notes;
        this.picture = picture;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.familyid = familyid;
        this.role = role;
    }

    public FamilyPOJO() {
    }

    @Override
    public String toString() {
        return "FamilyPOJO{" +
                "id='" + id + '\'' +
                ", branchId='" + branchId + '\'' +
                ", userId='" + userId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", picture='" + picture + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", familyid='" + familyid + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
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

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getFamilyid() {
        return familyid;
    }

    public void setFamilyid(String familyid) {
        this.familyid = familyid;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}