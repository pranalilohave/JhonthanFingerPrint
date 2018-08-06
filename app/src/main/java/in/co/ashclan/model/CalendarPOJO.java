package in.co.ashclan.model;

import java.io.Serializable;

public class CalendarPOJO implements Serializable {
   public String id,calendar_id,branch_id,user_id,name,color;

    public CalendarPOJO(String id, String calendar_id, String branch_id, String user_id, String name, String color) {
        this.id = id;
        this.calendar_id = calendar_id;
        this.branch_id = branch_id;
        this.user_id = user_id;
        this.name = name;
        this.color = color;
    }

    public CalendarPOJO() {
    }
    @Override
    public String toString() {
        return "CalendarPOJO{" +
                "id='" + id + '\'' +
                ", calendar_id='" + calendar_id + '\'' +
                ", branch_id='" + branch_id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", name='" + name + '\'' +
                ", color='" + color + '\'' +
                '}';
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCalendar_id() {
        return calendar_id;
    }
    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }
    public String getBranch_id() {
        return branch_id;
    }
    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
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
    public String getColor() {
        return color;
    }
    public void setColor(String color) {
        this.color = color;
    }
}
