package in.co.ashclan.model;

import java.io.Serializable;

public class MemberPhotoPojo implements Serializable {
    public String id,member_id,photoname,filepath;

    public MemberPhotoPojo(String id, String member_id, String photoname, String filepath) {
        this.id = id;
        this.member_id = member_id;
        this.photoname = photoname;
        this.filepath = filepath;
    }

    @Override
    public String toString() {
        return "MemberPhotoPojo{" +
                "id='" + id + '\'' +
                ", member_id='" + member_id + '\'' +
                ", photoname='" + photoname + '\'' +
                ", filepath='" + filepath + '\'' +
                '}';
    }

    public MemberPhotoPojo() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getPhotoname() {
        return photoname;
    }

    public void setPhotoname(String photoname) {
        this.photoname = photoname;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
