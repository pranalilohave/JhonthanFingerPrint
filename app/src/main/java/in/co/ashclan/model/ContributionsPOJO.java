package in.co.ashclan.model;

import java.io.Serializable;

public class ContributionsPOJO implements Serializable {
    String id,branchId,userId,memberId,familyId,fundId;
    String memberType,contributionBatchId,paymentMethodId,date;
    String files,notes,transRef,amount,year,month;
    String createdAt,updatedAt;

    String batchName,batchNote,batchdate,batchCurrent,batchStatus;
    String paymentMethod;

    @Override
    public String toString() {
        return "ContributionsPOJO{" +
                "id='" + id + '\'' +
                ", branchId='" + branchId + '\'' +
                ", userId='" + userId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", familyId='" + familyId + '\'' +
                ", fundId='" + fundId + '\'' +
                ", memberType='" + memberType + '\'' +
                ", contributionBatchId='" + contributionBatchId + '\'' +
                ", paymentMethodId='" + paymentMethodId + '\'' +
                ", date='" + date + '\'' +
                ", files='" + files + '\'' +
                ", notes='" + notes + '\'' +
                ", transRef='" + transRef + '\'' +
                ", amount='" + amount + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", batchName='" + batchName + '\'' +
                ", batchNote='" + batchNote + '\'' +
                ", batchdate='" + batchdate + '\'' +
                ", batchCurrent='" + batchCurrent + '\'' +
                ", batchStatus='" + batchStatus + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                '}';
    }

    public ContributionsPOJO() {
        this.id = "";
        this.branchId = "";
        this.userId = "";
        this.memberId = "";
        this.familyId = "";
        this.fundId = "";
        this.memberType = "";
        this.contributionBatchId = "";
        this.paymentMethodId = "";
        this.date = "";
        this.files = "";
        this.notes = "";
        this.transRef = "";
        this.amount = "";
        this.year = "";
        this.month = "";
        this.createdAt = "";
        this.updatedAt = "";
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getBatchNote() {
        return batchNote;
    }

    public void setBatchNote(String batchNote) {
        this.batchNote = batchNote;
    }

    public String getBatchdate() {
        return batchdate;
    }

    public void setBatchdate(String batchdate) {
        this.batchdate = batchdate;
    }

    public String getBatchCurrent() {
        return batchCurrent;
    }

    public void setBatchCurrent(String batchCurrent) {
        this.batchCurrent = batchCurrent;
    }

    public String getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(String batchStatus) {
        this.batchStatus = batchStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMeberType(String meberType) {
        this.memberType = meberType;
    }

    public String getContributionBatchId() {
        return contributionBatchId;
    }

    public void setContributionBatchId(String contributionBatchId) {
        this.contributionBatchId = contributionBatchId;
    }

    public String getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(String paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTransRef() {
        return transRef;
    }

    public void setTransRef(String transRef) {
        this.transRef = transRef;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
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
