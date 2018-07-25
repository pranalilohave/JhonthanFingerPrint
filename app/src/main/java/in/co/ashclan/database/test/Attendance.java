package in.co.ashclan.database.test;

public class Attendance {
    int date_time_id;
    String member_id,date_time;

    public Attendance() {
    }

    public Attendance(int date_time_id, String member_id, String date_time) {
        this.date_time_id = date_time_id;
        this.member_id = member_id;
        this.date_time = date_time;
    }

    public Attendance(String member_id, String date_time) {
        this.member_id = member_id;
        this.date_time = date_time;
    }

    public int getDate_time_id() {
        return date_time_id;
    }

    public void setDate_time_id(int date_time_id) {
        this.date_time_id = date_time_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getDate_time() {
        return date_time;
    }

    public void setDate_time(String date_time) {
        this.date_time = date_time;
    }
}
