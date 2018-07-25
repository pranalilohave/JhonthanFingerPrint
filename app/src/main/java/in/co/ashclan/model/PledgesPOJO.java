package in.co.ashclan.model;

public class PledgesPOJO {

    String id,branchId,userId,memberId,familyId,fundId;
    String pledgeType,campaignId,recurring,recur_frequency,recur_type,recur_start_date,recur_end_date,recur_next_date;
    String total_amount,times_number,date,year,month,notes;
    String createdAt,updatedAt;
    // Campaing POJO
    String campaign_id,campaign_name,amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PledgePOJO{" +
                "id='" + id + '\'' +
                ", branchId='" + branchId + '\'' +
                ", userId='" + userId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", familyId='" + familyId + '\'' +
                ", fundId='" + fundId + '\'' +
                ", pledgeType='" + pledgeType + '\'' +
                ", campaignId='" + campaignId + '\'' +
                ", amount='" + amount + '\'' +
                ", campaignname='" + campaign_name+ '\'' +
                ", recurring='" + recurring + '\'' +
                ", recur_frequency='" + recur_frequency+ '\'' +
                ", recur_type='" + recur_type+ '\'' +
                ", recur_start_date='" + recur_start_date+ '\'' +
                ", recur_end_date='" + recur_end_date+ '\'' +
                ", recur_next_date='" + recur_next_date+ '\'' +
                ", total_amount='" + total_amount+ '\'' +
                ", times_number='" +times_number+ '\'' +
                ", date='" +date+ '\'' +
                ", year='" +year+ '\'' +
                ", month='" +month+ '\'' +
                ", notes='" +notes+ '\'' +
                ", createdAt='" +createdAt+ '\'' +
                ", updatedAt='" +updatedAt+ '\'' +
                '}';
    }

    public PledgesPOJO(String pledgeType, String campaignId, String total_amount, String date, String notes) {
        this.pledgeType = pledgeType;
        this.campaignId = campaignId;
        this.amount = amount;
        this.date = date;
        this.notes = notes;
    }

    public PledgesPOJO() {
        this.id = "";
        this.branchId = "";
        this.userId = "";
        this.memberId = "";
        this.familyId = "";
        this.fundId = "";
        this.pledgeType = "";
        this.campaignId = "";
        this.amount = "";
        this.recurring = "";
        this.recur_frequency = "";
        this.recur_type = "";
        this.recur_start_date = "";
        this.recur_end_date = "";
        this.recur_next_date = "";
        this.total_amount = "";
        this.times_number = "";
        this.date = "";
        this.year = "";
        this.month = "";
        this.notes = "";
        this.createdAt = "";
        this.updatedAt = "";
        this.campaign_name = "";
    }

    public String getCampaign_id() {
        return campaign_id;
    }

    public void setCampaign_id(String campaign_id) {
        this.campaign_id = campaign_id;
    }

    public String getCampaign_name() {
        return campaign_name;
    }

    public void setCampaign_name(String campaign_name) {
        this.campaign_name = campaign_name;
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

    public String getPledgeType() {
        return pledgeType;
    }

    public void setPledgeType(String pledgeType) {
        this.pledgeType = pledgeType;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getRecurring() {
        return recurring;
    }

    public void setRecurring(String recurring) {
        this.recurring = recurring;
    }

    public String getRecur_frequency() {
        return recur_frequency;
    }

    public void setRecur_frequency(String recur_frequency) {
        this.recur_frequency = recur_frequency;
    }

    public String getRecur_type() {
        return recur_type;
    }

    public void setRecur_type(String recur_type) {
        this.recur_type = recur_type;
    }

    public String getRecur_start_date() {
        return recur_start_date;
    }

    public void setRecur_start_date(String recur_start_date) {
        this.recur_start_date = recur_start_date;
    }

    public String getRecur_end_date() {
        return recur_end_date;
    }

    public void setRecur_end_date(String recur_end_date) {
        this.recur_end_date = recur_end_date;
    }

    public String getRecur_next_date() {
        return recur_next_date;
    }

    public void setRecur_next_date(String recur_next_date) {
        this.recur_next_date = recur_next_date;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getTimes_number() {
        return times_number;
    }

    public void setTimes_number(String times_number) {
        this.times_number = times_number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

