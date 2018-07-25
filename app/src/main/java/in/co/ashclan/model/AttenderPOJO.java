package in.co.ashclan.model;

public class AttenderPOJO {
    String id,name,phone,gender,age,address,f_name,l_name;
    String photoURL,photoLocalPath;



    public AttenderPOJO() {
    }

    public AttenderPOJO(String id, String name, String phone, String gender, String age, String address) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.age = age;
        this.address = address;
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

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getL_name() {
        return l_name;
    }

    public void setL_name(String l_name) {
        this.l_name = l_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
