package in.co.ashclan.model;

import java.io.Serializable;

public class MemberPOJO implements Serializable{
    String id,bytes;
    String userId, firstName, middleName, lastName, gender, status, maritalStatus, dob, homePhone, workPhone,
            mobilePhone, address, email, notes, rollNo, fingerPrint, fingerPrint1,fingerPrint2;

    String photoURL,photoLocalPath;
    String serverType;
    String createAt,updateAt;

    public MemberPOJO() {
        this.id = "";
        this.userId = "";
        this.firstName = "";
        this.middleName = "";
        this.lastName = "";
        this.gender = "";
        this.status = "";
        this.maritalStatus = "";
        this.dob = "";
        this.homePhone = "";
        this.workPhone = "";
        this.mobilePhone = "";
        this.address = "";
        this.email = "";
        this.notes = "";
        this.rollNo = "";
        this.fingerPrint = "";
        this.fingerPrint1 = "";
        this.fingerPrint2 = "";
        this.photoURL = "";
        this.photoLocalPath = "";
        this.createAt = "";
        this.updateAt = "";
    }

    public String getBytes() {
        return bytes;
    }

    public void setBytes(String bytes) {
        this.bytes = bytes;
    }

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getPhotoLocalPath() {
        return photoLocalPath;
    }

    public void setPhotoLocalPath(String photoLocalPath) {
        this.photoLocalPath = photoLocalPath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public String getFingerPrint1() {
        return fingerPrint1;
    }

    public void setFingerPrint1(String fingerPrint1) {
        this.fingerPrint1 = fingerPrint1;
    }

    public String getFingerPrint2() {
        return fingerPrint2;
    }

    public void setFingerPrint2(String fingerPrint2) {
        this.fingerPrint2 = fingerPrint2;
    }

    @Override
    public String toString() {
        return "MemberPOJO{" +
                "id='" + id + '\'' +
                ", bytes='" + bytes + '\'' +
                ", userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", status='" + status + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", dob='" + dob + '\'' +
                ", homePhone='" + homePhone + '\'' +
                ", workPhone='" + workPhone + '\'' +
                ", mobilePhone='" + mobilePhone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", notes='" + notes + '\'' +
                ", rollNo='" + rollNo + '\'' +
                ", fingerPrint='" + fingerPrint + '\'' +
                ", fingerPrint1='" + fingerPrint1 + '\'' +
                ", fingerPrint2='" + fingerPrint2 + '\'' +
                ", photoURL='" + photoURL + '\'' +
                ", photoLocalPath='" + photoLocalPath + '\'' +
                ", serverType='" + serverType + '\'' +
                ", createAt='" + createAt + '\'' +
                ", updateAt='" + updateAt + '\'' +
                '}';
    }
}
