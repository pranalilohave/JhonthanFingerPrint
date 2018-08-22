package in.co.ashclan.model;

import java.io.Serializable;

public class LocationPOJO implements Serializable {
public String id,locatio_id,user_id,name;

    public LocationPOJO(String id, String locatio_id, String user_id, String name) {
        this.id = id;
        this.locatio_id = locatio_id;
        this.user_id = user_id;
        this.name = name;
    }

    public LocationPOJO() {
    }

    @Override
    public String toString() {
        return "LocationPOJO{" +
                "id='" + id + '\'' +
                ", locatio_id='" + locatio_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocatio_id() {
        return locatio_id;
    }

    public void setLocatio_id(String locatio_id) {
        this.locatio_id = locatio_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
