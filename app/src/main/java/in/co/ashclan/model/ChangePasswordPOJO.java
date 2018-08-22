package in.co.ashclan.model;

public class ChangePasswordPOJO {

    String id, userid, oldpassword, newpassword, comformpassword, adminfirstname, adminid,adminlastname;
    String photoUrl;

    public ChangePasswordPOJO(String id, String userid, String oldpassword, String newpassword, String comformpassword, String adminfirstname, String adminid, String adminlastname, String photoUrl) {
        this.id = id;
        this.userid = userid;
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
        this.comformpassword = comformpassword;
        this.adminfirstname = adminfirstname;
        this.adminid = adminid;
        this.adminlastname = adminlastname;
        this.photoUrl = photoUrl;
    }

    public ChangePasswordPOJO() {
    }

    @Override
    public String toString() {
        return "ChangePasswordPOJO{" +
                "id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", oldpassword='" + oldpassword + '\'' +
                ", newpassword='" + newpassword + '\'' +
                ", comformpassword='" + comformpassword + '\'' +
                ", adminfirstname='" + adminfirstname + '\'' +
                ", adminid='" + adminid + '\'' +
                ", adminlastname='" + adminlastname + '\'' +
                ", photoUrl='" + photoUrl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getOldpassword() {
        return oldpassword;
    }

    public void setOldpassword(String oldpassword) {
        this.oldpassword = oldpassword;
    }

    public String getNewpassword() {
        return newpassword;
    }

    public void setNewpassword(String newpassword) {
        this.newpassword = newpassword;
    }

    public String getComformpassword() {
        return comformpassword;
    }

    public void setComformpassword(String comformpassword) {
        this.comformpassword = comformpassword;
    }

    public String getAdminfirstname() {
        return adminfirstname;
    }

    public void setAdminfirstname(String adminfirstname) {
        this.adminfirstname = adminfirstname;
    }

    public String getAdminid() {
        return adminid;
    }

    public void setAdminid(String adminid) {
        this.adminid = adminid;
    }

    public String getAdminlastname() {
        return adminlastname;
    }

    public void setAdminlastname(String adminlastname) {
        this.adminlastname = adminlastname;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}