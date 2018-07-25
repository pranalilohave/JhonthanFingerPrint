package in.co.ashclan.model;

import java.io.Serializable;

public class EventPOJO implements Serializable {

    String id, branchId,userId,parentId,eventLocationId,eventCalenderId;

    String name,cost,allDay,startDate,start_time,end_date,end_time;
    String recurring,recurFrequency,recurStartDate,recurEndDate,recurNextDate,recurType;
    String checkInType, tags, includeCheckOut, familyCheckIn,featured_image;
    String gallery,files;
    String year, month,notes;
    String createdAt,updatedAt;

    @Override
    public String toString() {
        return "EventPOJO{" +
                "id='" + id + '\'' +
                ", branchId='" + branchId + '\'' +
                ", userId='" + userId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", eventLocationId='" + eventLocationId + '\'' +
                ", eventCalenderId='" + eventCalenderId + '\'' +
                ", name='" + name + '\'' +
                ", cost='" + cost + '\'' +
                ", allDay='" + allDay + '\'' +
                ", startDate='" + startDate + '\'' +
                ", start_time='" + start_time + '\'' +
                ", end_date='" + end_date + '\'' +
                ", end_time='" + end_time + '\'' +
                ", recurring='" + recurring + '\'' +
                ", recurFrequency='" + recurFrequency + '\'' +
                ", recurStartDate='" + recurStartDate + '\'' +
                ", recurEndDate='" + recurEndDate + '\'' +
                ", recurNextDate='" + recurNextDate + '\'' +
                ", recurType='" + recurType + '\'' +
                ", checkInType='" + checkInType + '\'' +
                ", tags='" + tags + '\'' +
                ", includeCheckOut='" + includeCheckOut + '\'' +
                ", familyCheckIn='" + familyCheckIn + '\'' +
                ", featured_image='" + featured_image + '\'' +
                ", gallery='" + gallery + '\'' +
                ", files='" + files + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", notes='" + notes + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public EventPOJO() {
        this.userId="";
        this.gallery="";
        this.files="";
        this.id = "";
        this.branchId = "";
        this.parentId = "";
        this.eventLocationId = "";
        this.eventCalenderId = "";
        this.name = "";
        this.cost = "";
        this.allDay = "";
        this.startDate = "";
        this.start_time = "";
        this.end_date = "";
        this.end_time = "";
        this.recurring = "";
        this.recurFrequency = "";
        this.recurStartDate = "";
        this.recurEndDate = "";
        this.recurNextDate = "";
        this.recurType = "";
        this.checkInType = "";
        this.tags = "";
        this.includeCheckOut = "";
        this.familyCheckIn = "";
        this.featured_image = "";
        this.year = "";
        this.month = "";
        this.notes = "";
        this.createdAt = "";
        this.updatedAt = "";
    }

    public String getGallery() {
        return gallery;
    }

    public void setGallery(String gallery) {
        this.gallery = gallery;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getEventLocationId() {
        return eventLocationId;
    }

    public void setEventLocationId(String eventLocationId) {
        this.eventLocationId = eventLocationId;
    }

    public String getEventCalenderId() {
        return eventCalenderId;
    }

    public void setEventCalenderId(String eventCalenderId) {
        this.eventCalenderId = eventCalenderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getAllDay() {
        return allDay;
    }

    public void setAllDay(String allDay) {
        this.allDay = allDay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }

    public String getRecurFrequency() {
        return recurFrequency;
    }

    public void setRecurFrequency(String recurFrequency) {
        this.recurFrequency = recurFrequency;
    }

    public String getRecurStartDate() {
        return recurStartDate;
    }

    public void setRecurStartDate(String recurStartDate) {
        this.recurStartDate = recurStartDate;
    }

    public String getRecurEndDate() {
        return recurEndDate;
    }

    public void setRecurEndDate(String recurEndDate) {
        this.recurEndDate = recurEndDate;
    }

    public String getRecurNextDate() {
        return recurNextDate;
    }

    public void setRecurNextDate(String recurNextDate) {
        this.recurNextDate = recurNextDate;
    }

    public String getRecurType() {
        return recurType;
    }

    public void setRecurType(String recurType) {
        this.recurType = recurType;
    }

    public String getCheckInType() {
        return checkInType;
    }

    public void setCheckInType(String checkInType) {
        this.checkInType = checkInType;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIncludeCheckOut() {
        return includeCheckOut;
    }

    public void setIncludeCheckOut(String includeCheckOut) {
        this.includeCheckOut = includeCheckOut;
    }

    public String getFamilyCheckIn() {
        return familyCheckIn;
    }

    public void setFamilyCheckIn(String familyCheckIn) {
        this.familyCheckIn = familyCheckIn;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
}
