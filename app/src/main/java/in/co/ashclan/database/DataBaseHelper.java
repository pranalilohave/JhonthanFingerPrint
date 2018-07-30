package in.co.ashclan.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.model.ContributionsPOJO;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.FamilyPOJO;
import in.co.ashclan.model.GroupsPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.model.PledgesPOJO;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database.db";

    //Member Table
    public static final String MEMBERS_TABLE = "member_table";
    public static final String MEMBER_COl_1 = "member_id";
    public static final String MEMBER_COL_2 = "id";
    public static final String MEMBER_COL_3 = "user_id";
    public static final String MEMBER_COL_4 = "first_name";
    public static final String MEMBER_COL_5 = "middle_name";
    public static final String MEMBER_COL_6 = "last_name";
    public static final String MEMBER_COL_7 = "gender";
    public static final String MEMBER_COL_8 = "status";
    public static final String MEMBER_COL_9 = "marital_status";
    public static final String MEMBER_COL_10 = "dob";
    public static final String MEMBER_COL_11 = "home_phone";
    public static final String MEMBER_COL_12 = "mobile_phone";
    public static final String MEMBER_COL_13 = "work_phone";
    public static final String MEMBER_COL_14 = "email";
    public static final String MEMBER_COL_15 = "address";
    public static final String MEMBER_COL_16 = "notes";
    public static final String MEMBER_COL_17 = "photo_url";
    public static final String MEMBER_COL_18 = "photo_local_path";
    public static final String MEMBER_COL_19 = "fingerprint";
    public static final String MEMBER_COL_20 = "fingerprint1";
    public static final String MEMBER_COL_21 = "rollno";
    public static final String MEMBER_COL_22 = "created_at";
    public static final String MEMBER_COL_23 = "updated_at";

    public static final String CREATE_TABLE_MEMBER =
            "CREATE TABLE " + MEMBERS_TABLE + "("
                    + MEMBER_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + MEMBER_COL_2 + " TEXT,"
                    + MEMBER_COL_3 + " TEXT,"
                    + MEMBER_COL_4 + " TEXT,"
                    + MEMBER_COL_5 + " TEXT,"
                    + MEMBER_COL_6 + " TEXT,"
                    + MEMBER_COL_7 + " TEXT,"
                    + MEMBER_COL_8 + " TEXT,"
                    + MEMBER_COL_9 + " TEXT,"
                    + MEMBER_COL_10 + " TEXT,"
                    + MEMBER_COL_11 + " TEXT,"
                    + MEMBER_COL_12 + " TEXT,"
                    + MEMBER_COL_13 + " TEXT,"
                    + MEMBER_COL_14 + " TEXT,"
                    + MEMBER_COL_15 + " TEXT,"
                    + MEMBER_COL_16 + " TEXT,"
                    + MEMBER_COL_17 + " TEXT,"
                    + MEMBER_COL_18 + " TEXT,"
                    + MEMBER_COL_19 + " TEXT,"
                    + MEMBER_COL_20 + " TEXT,"
                    + MEMBER_COL_21 + " TEXT,"
                    + MEMBER_COL_22 + " TEXT,"
                    + MEMBER_COL_23 + " TEXT"
                    + ")";


    //Events Table
    public static final String EVENTS_TABLE = "event_table";
    public static final String EVENT_COl_1 = "event_id";
    public static final String EVENT_COL_2 = "id";
    public static final String EVENT_COL_30 = "branch_id";
    public static final String EVENT_COL_3 = "user_id";
    public static final String EVENT_COL_4 = "parent_id";
    public static final String EVENT_COL_5 = "event_location_id";
    public static final String EVENT_COL_6 = "event_calendar_id";
    public static final String EVENT_COL_7 = "name";
    public static final String EVENT_COL_8 = "cost";
    public static final String EVENT_COL_9 = "all_day";
    public static final String EVENT_COL_10 = "start_date";
    public static final String EVENT_COL_11 = "start_time";
    public static final String EVENT_COL_31 = "end_date";
    public static final String EVENT_COL_12 = "end_time";
    public static final String EVENT_COL_13 = "recurring";
    public static final String EVENT_COL_14 = "recur_frequency";
    public static final String EVENT_COL_15 = "recu_start_date";
    public static final String EVENT_COL_16 = "recu_end_date";
    public static final String EVENT_COL_17 = "recur_next_date";
    public static final String EVENT_COL_18 = "recur_types";
    public static final String EVENT_COL_19 = "checkin_types";
    public static final String EVENT_COL_20 = "tags";
    public static final String EVENT_COL_21 = "include_checkout";
    public static final String EVENT_COL_22 = "family_checkin";
    public static final String EVENT_COL_23 = "featured_image";
    public static final String EVENT_COL_24 = "gallery";
    public static final String EVENT_COL_25 = "files";
    public static final String EVENT_COL_26 = "year";
    public static final String EVENT_COL_32 = "month";
    public static final String EVENT_COL_27 = "notes";
    public static final String EVENT_COL_28 = "created_at";
    public static final String EVENT_COL_29 = "updated_at";

    public static final String CREATE_TABLE_EVENTS =
            "CREATE TABLE " + EVENTS_TABLE + "("
                    + EVENT_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + EVENT_COL_2 + " TEXT,"
                    + EVENT_COL_3 + " TEXT,"
                    + EVENT_COL_4 + " TEXT,"
                    + EVENT_COL_5 + " TEXT,"
                    + EVENT_COL_6 + " TEXT,"
                    + EVENT_COL_7 + " TEXT,"
                    + EVENT_COL_8 + " TEXT,"
                    + EVENT_COL_9 + " TEXT,"
                    + EVENT_COL_10 + " TEXT,"
                    + EVENT_COL_11 + " TEXT,"
                    + EVENT_COL_12 + " TEXT,"
                    + EVENT_COL_13 + " TEXT,"
                    + EVENT_COL_14 + " TEXT,"
                    + EVENT_COL_15 + " TEXT,"
                    + EVENT_COL_16 + " TEXT,"
                    + EVENT_COL_17 + " TEXT,"
                    + EVENT_COL_18 + " TEXT,"
                    + EVENT_COL_19 + " TEXT,"
                    + EVENT_COL_20 + " TEXT,"
                    + EVENT_COL_21 + " TEXT,"
                    + EVENT_COL_22 + " TEXT,"
                    + EVENT_COL_23 + " TEXT,"
                    + EVENT_COL_24 + " TEXT,"
                    + EVENT_COL_25 + " TEXT,"
                    + EVENT_COL_26 + " TEXT,"
                    + EVENT_COL_27 + " TEXT,"
                    + EVENT_COL_28 + " TEXT,"
                    + EVENT_COL_29 + " TEXT,"
                    + EVENT_COL_30 + " TEXT,"
                    + EVENT_COL_31 + " TEXT,"
                    + EVENT_COL_32 + " TEXT"
                    + ")";

    //Table Contribution
    public static final String CONTRIBUTION_TABLE = "contribution_table";
    public static final String CONTRIBUTION_COl_1 = "CONTRIBUTION_id";
    public static final String CONTRIBUTION_COL_2 = "branchId";
    public static final String CONTRIBUTION_COL_3 = "userId";
    public static final String CONTRIBUTION_COL_4 = "memberId";
    public static final String CONTRIBUTION_COL_5 = "familyId";
    public static final String CONTRIBUTION_COL_6 = "fundId";
    public static final String CONTRIBUTION_COL_7 = "memberType";
    public static final String CONTRIBUTION_COL_8 = "contributionBatchId";
    public static final String CONTRIBUTION_COL_9 = "paymentMethodId";
    public static final String CONTRIBUTION_COL_10 = "date";
    public static final String CONTRIBUTION_COL_11 = "files";
    public static final String CONTRIBUTION_COL_12 = "notes";
    public static final String CONTRIBUTION_COL_13 = "transRef";
    public static final String CONTRIBUTION_COL_14 = "amount";
    public static final String CONTRIBUTION_COL_15 = "year";
    public static final String CONTRIBUTION_COL_16 = "month";
    public static final String CONTRIBUTION_COL_17 = "createdAt";
    public static final String CONTRIBUTION_COL_18 = "updateAt";
    public static final String CONTRIBUTION_COL_19 = "payment_name";
    public static final String CONTRIBUTION_COL_20 = "batch_name";
    public static final String CONTRIBUTION_COL_21 = "batch_note";
    public static final String CONTRIBUTION_COL_22 = "batch_date";
    public static final String CONTRIBUTION_COL_23 = "batch_current";
    public static final String CONTRIBUTION_COL_24 = "batch_status";

    //Query for Table Members
    public static final String CREATE_TABLE_CONTRIBUTION_OFFLINE =
            "CREATE TABLE " + CONTRIBUTION_TABLE + "("
                    + CONTRIBUTION_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + CONTRIBUTION_COL_2 + " TEXT,"
                    + CONTRIBUTION_COL_3 + " TEXT,"
                    + CONTRIBUTION_COL_4 + " TEXT,"
                    + CONTRIBUTION_COL_5 + " TEXT,"
                    + CONTRIBUTION_COL_6 + " TEXT,"
                    + CONTRIBUTION_COL_7 + " TEXT,"
                    + CONTRIBUTION_COL_8 + " TEXT,"
                    + CONTRIBUTION_COL_9 + " TEXT,"
                    + CONTRIBUTION_COL_10 + " TEXT,"
                    + CONTRIBUTION_COL_11 + " TEXT,"
                    + CONTRIBUTION_COL_12 + " TEXT,"
                    + CONTRIBUTION_COL_13 + " TEXT,"
                    + CONTRIBUTION_COL_14 + " TEXT,"
                    + CONTRIBUTION_COL_15 + " TEXT,"
                    + CONTRIBUTION_COL_16 + " TEXT,"
                    + CONTRIBUTION_COL_17 + " TEXT,"
                    + CONTRIBUTION_COL_18 + " TEXT,"
                    + CONTRIBUTION_COL_19 + " TEXT,"
                    + CONTRIBUTION_COL_20 + " TEXT,"
                    + CONTRIBUTION_COL_21 + " TEXT,"
                    + CONTRIBUTION_COL_22 + " TEXT,"
                    + CONTRIBUTION_COL_23 + " TEXT,"
                    + CONTRIBUTION_COL_24 + " TEXT"
                    + ")";

    //Pledges

    public static final String PLEDGES_TABLE = "PLEDGES_table";
    public static final String PLEDGES_COl_1 = "PLEDGES_Id";
    public static final String PLEDGES_COL_2 = "branchId";
    public static final String PLEDGES_COL_3 = "userId";
    public static final String PLEDGES_COL_4 = "memberId";
    public static final String PLEDGES_COL_5 = "familyId";
    public static final String PLEDGES_COL_6 = "fundId";
    public static final String PLEDGES_COL_7 = "pledgeType";
    public static final String PLEDGES_COL_8 = "campaignId";
    public static final String PLEDGES_COL_9 = "amount";
    public static final String PLEDGES_COL_10 = "recurring";
    public static final String PLEDGES_COL_11 = "recur_frequency";
    public static final String PLEDGES_COL_12 = "recur_type";
    public static final String PLEDGES_COL_13 = "recur_start_date";
    public static final String PLEDGES_COL_14 = "recur_end_date";
    public static final String PLEDGES_COL_15 = "recur_next_date";
    public static final String PLEDGES_COL_16 = "total_amount";
    public static final String PLEDGES_COL_17 = "times_number";
    public static final String PLEDGES_COL_18 = "date";
    public static final String PLEDGES_COL_19 = "year";
    public static final String PLEDGES_COL_20 = "month";
    public static final String PLEDGES_COL_21 = "notes";
    public static final String PLEDGES_COL_22 = "createdAt";
    public static final String PLEDGES_COL_23 = "updatedAt";
    public static final String PLEDGES_COL_24 = "campaingn_Name";

    public static final String CREATE_TABLE_PLEDGES =
            "CREATE TABLE " + PLEDGES_TABLE + "("
                    + PLEDGES_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + PLEDGES_COL_2 + " TEXT,"
                    + PLEDGES_COL_3 + " TEXT,"
                    + PLEDGES_COL_4 + " TEXT,"
                    + PLEDGES_COL_5 + " TEXT,"
                    + PLEDGES_COL_6 + " TEXT,"
                    + PLEDGES_COL_7 + " TEXT,"
                    + PLEDGES_COL_8 + " TEXT,"
                    + PLEDGES_COL_9 + " TEXT,"
                    + PLEDGES_COL_10 + " TEXT,"
                    + PLEDGES_COL_11 + " TEXT,"
                    + PLEDGES_COL_12 + " TEXT,"
                    + PLEDGES_COL_13 + " TEXT,"
                    + PLEDGES_COL_14 + " TEXT,"
                    + PLEDGES_COL_15 + " TEXT,"
                    + PLEDGES_COL_16 + " TEXT,"
                    + PLEDGES_COL_17 + " TEXT,"
                    + PLEDGES_COL_18 + " TEXT,"
                    + PLEDGES_COL_19 + " TEXT,"
                    + PLEDGES_COL_20 + " TEXT,"
                    + PLEDGES_COL_21 + " TEXT,"
                    + PLEDGES_COL_22 + " TEXT,"
                    + PLEDGES_COL_23 + " TEXT,"
                    + PLEDGES_COL_24 + " TEXT"
                    + ")";

    //Family
    // String id,branchId,userId,memberId,name,notes,picture,createdAt,updatedAt;
    public static final String FAMILY_TABLE = "FAMILY_table";
    public static final String FAMILY_COl_1 = "PLEDGES_Id";
    public static final String FAMILY_COL_2 = "branchId";
    public static final String FAMILY_COL_3 = "userId";
    public static final String FAMILY_COL_4 = "memberId";
    public static final String FAMILY_COL_5 = "name";
    public static final String FAMILY_COL_6 = "notes";
    public static final String FAMILY_COL_7 = "picture";
    public static final String FAMILY_COL_8 = "createdAt";
    public static final String FAMILY_COL_9 = "updatedAt";
    public static final String CREATE_TABLE_FAMILY =
            "CREATE TABLE " + FAMILY_TABLE + "("
                    + FAMILY_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + FAMILY_COL_2 + " TEXT,"
                    + FAMILY_COL_3 + " TEXT,"
                    + FAMILY_COL_4 + " TEXT,"
                    + FAMILY_COL_5 + " TEXT,"
                    + FAMILY_COL_6 + " TEXT,"
                    + FAMILY_COL_7 + " TEXT,"
                    + FAMILY_COL_8 + " TEXT,"
                    + FAMILY_COL_9 + " TEXT"
                    + ")";
    //Groups
    public static final String GROUPS_TABLE = "GROUPS_table";
    public static final String GROUPS_COl_1 = "id";
    public static final String GROUPS_COL_2 = "member_id";
    public static final String GROUPS_COL_3 = "user_id";
    public static final String GROUPS_COL_4 = "tag_id";

    public static final String CREATE_TABLE_GROUPS =
            "CREATE TABLE " + GROUPS_TABLE + "("
                    + GROUPS_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + GROUPS_COL_2 + " TEXT,"
                    + GROUPS_COL_3 + " TEXT,"
                    + GROUPS_COL_4 + " TEXT"
                    + ")";

    //EVENT ATTENDANCE DETAILS
    public static final String EVENT_ATTEN_TABLE = "EVENT_ATTEN_table";
    public static final String EVENT_ATTEN_COl_1 = "id";
    public static final String EVENT_ATTEN_COL_2 = "event_id";
    public static final String EVENT_ATTEN_COL_3 = "user_id";
    public static final String EVENT_ATTEN_COL_4 = "member_id";
    public static final String EVENT_ATTEN_COL_5 = "anonymous";
    public static final String EVENT_ATTEN_COL_6 = "date";
    public static final String EVENT_ATTEN_COL_7 = "created_at";
    public static final String EVENT_ATTEN_COL_8 = "updated_at";

    public static final String CREATE_TABLE_EVENTATTEN =
            "CREATE TABLE " + EVENT_ATTEN_TABLE + "("
                    + EVENT_ATTEN_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + EVENT_ATTEN_COL_2 + " TEXT,"
                    + EVENT_ATTEN_COL_3 + " TEXT,"
                    + EVENT_ATTEN_COL_4 + " TEXT,"
                    + EVENT_ATTEN_COL_5 + " TEXT,"
                    + EVENT_ATTEN_COL_6 + " TEXT,"
                    + EVENT_ATTEN_COL_7 + " TEXT,"
                    + EVENT_ATTEN_COL_8 + " TEXT"
                    + ")";

    //EVENT ATTENDANCE DETAILS
    public static final String EVENT_CALENDAR_TABLE = "EVENT_CALENDAR_table";
    public static final String EVENT_CALENDAR_COl_1 = "id";
    public static final String EVENT_CALENDAR_COL_2 = "event_id";
    public static final String EVENT_CALENDAR_COL_3 = "user_id";
    public static final String EVENT_CALENDAR_COL_4 = "member_id";
    public static final String EVENT_CALENDAR_COL_5 = "anonymous";


    public static final String CREATE_TABLE_EVENTCALENDAR =
            "CREATE TABLE " + EVENT_CALENDAR_TABLE + "("
                    + EVENT_CALENDAR_COl_1 + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + EVENT_CALENDAR_COL_2 + " TEXT,"
                    + EVENT_CALENDAR_COL_3 + " TEXT,"
                    + EVENT_CALENDAR_COL_4 + " TEXT,"
                    + EVENT_CALENDAR_COL_5 + " TEXT"
                    + ")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create tables
        db.execSQL(CREATE_TABLE_MEMBER);
        db.execSQL(CREATE_TABLE_EVENTS);
        //CREATE TABLE CONTRIBUTION OFFLINE
        db.execSQL(CREATE_TABLE_CONTRIBUTION_OFFLINE);
        //CREATE TABLE PLEDGES
        db.execSQL(CREATE_TABLE_PLEDGES);
        //CREATE FAMILY Table
        db.execSQL(CREATE_TABLE_FAMILY);
        //CREATE GROUPS Table
        db.execSQL(CREATE_TABLE_GROUPS);
        //CREATE TABLE EVENT ATTENDANCE
        db.execSQL(CREATE_TABLE_EVENTATTEN);
       //CREATE TABLE EVENT CALENDAR ATTENDANCE
        db.execSQL(CREATE_TABLE_EVENTCALENDAR);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MEMBERS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENTS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CONTRIBUTION_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FAMILY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PLEDGES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + GROUPS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + EVENT_CALENDAR_TABLE);

        // Create tables again
        onCreate(db);
    }

    //Members
    public boolean insertMemberData(MemberPOJO member) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MEMBER_COL_2, member.getId());
        values.put(MEMBER_COL_3, member.getUserId());
        values.put(MEMBER_COL_4, member.getFirstName());
        values.put(MEMBER_COL_5, member.getMiddleName());
        values.put(MEMBER_COL_6, member.getLastName());
        values.put(MEMBER_COL_7, member.getGender());
        values.put(MEMBER_COL_8, member.getStatus());
        values.put(MEMBER_COL_9, member.getMaritalStatus());
        values.put(MEMBER_COL_10, member.getDob());
        values.put(MEMBER_COL_11, member.getHomePhone());
        values.put(MEMBER_COL_12, member.getMobilePhone());
        values.put(MEMBER_COL_13, member.getWorkPhone());
        values.put(MEMBER_COL_14, member.getEmail());
        values.put(MEMBER_COL_15, member.getAddress());
        values.put(MEMBER_COL_16, member.getNotes());
        values.put(MEMBER_COL_17, member.getPhotoURL());
        values.put(MEMBER_COL_18, member.getPhotoLocalPath());
        values.put(MEMBER_COL_19, member.getFingerPrint());
        values.put(MEMBER_COL_20, member.getFingerPrint1());
        values.put(MEMBER_COL_21, member.getRollNo());
        values.put(MEMBER_COL_22, member.getCreateAt());
        values.put(MEMBER_COL_23, member.getUpdateAt());

        long result = db.insert(MEMBERS_TABLE, null, values);

        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<MemberPOJO> getSerachMember(String str) {
        List<MemberPOJO> members = new ArrayList<MemberPOJO>();
        String selectQuery = "SELECT  * FROM " + MEMBERS_TABLE + " WHERE " + MEMBER_COL_4 + " LIKE '" + str + "%'"
                + " OR " + MEMBER_COL_6 + " LIKE '" + str + "%'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {

                MemberPOJO member = new MemberPOJO();
                member.setId(cursor.getString(cursor.getColumnIndex(MEMBER_COL_2)));
                member.setUserId(cursor.getString(cursor.getColumnIndex(MEMBER_COL_3)));
                member.setFirstName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_4)));
                member.setMiddleName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_5)));
                member.setLastName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_6)));
                member.setGender(cursor.getString(cursor.getColumnIndex(MEMBER_COL_7)));
                member.setStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_8)));
                member.setMaritalStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_9)));
                member.setDob(cursor.getString(cursor.getColumnIndex(MEMBER_COL_10)));
                member.setHomePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_11)));
                member.setMobilePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_12)));
                member.setWorkPhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_13)));
                member.setEmail(cursor.getString(cursor.getColumnIndex(MEMBER_COL_14)));
                member.setAddress(cursor.getString(cursor.getColumnIndex(MEMBER_COL_15)));
                member.setNotes(cursor.getString(cursor.getColumnIndex(MEMBER_COL_16)));
                member.setPhotoURL(cursor.getString(cursor.getColumnIndex(MEMBER_COL_17)));
                member.setPhotoLocalPath(cursor.getString(cursor.getColumnIndex(MEMBER_COL_18)));
                member.setFingerPrint(cursor.getString(cursor.getColumnIndex(MEMBER_COL_19)));
                member.setFingerPrint1(cursor.getString(cursor.getColumnIndex(MEMBER_COL_20)));
                member.setRollNo(cursor.getString(cursor.getColumnIndex(MEMBER_COL_21)));
                member.setCreateAt(cursor.getString(cursor.getColumnIndex(MEMBER_COL_22)));
                member.setUpdateAt(cursor.getString(cursor.getColumnIndex(MEMBER_COL_23)));

                members.add(member);
            } while (cursor.moveToNext());
        }
        db.close();
        return members;
    }

    public List<MemberPOJO> getAllMembers() {

        List<MemberPOJO> members = new ArrayList<MemberPOJO>();

        String selectQuery = "SELECT  * FROM " + MEMBERS_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {

                MemberPOJO member = new MemberPOJO();
                member.setId(cursor.getString(cursor.getColumnIndex(MEMBER_COL_2)));
                member.setUserId(cursor.getString(cursor.getColumnIndex(MEMBER_COL_3)));
                member.setFirstName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_4)));
                member.setMiddleName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_5)));
                member.setLastName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_6)));
                member.setGender(cursor.getString(cursor.getColumnIndex(MEMBER_COL_7)));
                member.setStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_8)));
                member.setMaritalStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_9)));
                member.setDob(cursor.getString(cursor.getColumnIndex(MEMBER_COL_10)));
                member.setHomePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_11)));
                member.setMobilePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_12)));
                member.setWorkPhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_13)));
                member.setEmail(cursor.getString(cursor.getColumnIndex(MEMBER_COL_14)));
                member.setAddress(cursor.getString(cursor.getColumnIndex(MEMBER_COL_15)));
                member.setNotes(cursor.getString(cursor.getColumnIndex(MEMBER_COL_16)));
                member.setPhotoURL(cursor.getString(cursor.getColumnIndex(MEMBER_COL_17)));
                member.setPhotoLocalPath(cursor.getString(cursor.getColumnIndex(MEMBER_COL_18)));
                member.setFingerPrint(cursor.getString(cursor.getColumnIndex(MEMBER_COL_19)));
                member.setFingerPrint1(cursor.getString(cursor.getColumnIndex(MEMBER_COL_20)));
                member.setRollNo(cursor.getString(cursor.getColumnIndex(MEMBER_COL_21)));
                member.setCreateAt(cursor.getString(cursor.getColumnIndex(MEMBER_COL_22)));
                member.setUpdateAt(cursor.getString(cursor.getColumnIndex(MEMBER_COL_23)));

                members.add(member);
            } while (cursor.moveToNext());
        }
        db.close();
        return members;
    }

    public MemberPOJO getMemberDetails(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + MEMBERS_TABLE + " WHERE " + MEMBER_COL_2 + " = " + id;

        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        MemberPOJO member = new MemberPOJO();
        member.setId(cursor.getString(cursor.getColumnIndex(MEMBER_COL_2)));
        member.setUserId(cursor.getString(cursor.getColumnIndex(MEMBER_COL_3)));
        member.setFirstName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_4)));
        member.setMiddleName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_5)));
        member.setLastName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_6)));
        member.setGender(cursor.getString(cursor.getColumnIndex(MEMBER_COL_7)));
        member.setStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_8)));
        member.setMaritalStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_9)));
        member.setDob(cursor.getString(cursor.getColumnIndex(MEMBER_COL_10)));
        member.setHomePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_11)));
        member.setMobilePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_12)));
        member.setWorkPhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_13)));
        member.setEmail(cursor.getString(cursor.getColumnIndex(MEMBER_COL_14)));
        member.setAddress(cursor.getString(cursor.getColumnIndex(MEMBER_COL_15)));
        member.setNotes(cursor.getString(cursor.getColumnIndex(MEMBER_COL_16)));
        member.setPhotoURL(cursor.getString(cursor.getColumnIndex(MEMBER_COL_17)));
        member.setPhotoLocalPath(cursor.getString(cursor.getColumnIndex(MEMBER_COL_18)));
        member.setFingerPrint(cursor.getString(cursor.getColumnIndex(MEMBER_COL_19)));
        member.setFingerPrint1(cursor.getString(cursor.getColumnIndex(MEMBER_COL_20)));
        member.setRollNo(cursor.getString(cursor.getColumnIndex(MEMBER_COL_21)));
        member.setCreateAt(cursor.getString(cursor.getColumnIndex(MEMBER_COL_22)));
        member.setUpdateAt(cursor.getString(cursor.getColumnIndex(MEMBER_COL_23)));

        return member;
    }

    public void deleteAllMembers() {
        try {
            String selectQuery = "DELETE FROM " + MEMBERS_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(MEMBERS_TABLE, null, null);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Member Delete-->", ex.toString());
        }
    }

    public void updateMemberData(MemberPOJO member) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MEMBER_COL_3, member.getUserId());
        values.put(MEMBER_COL_4, member.getFirstName());
        values.put(MEMBER_COL_5, member.getMiddleName());
        values.put(MEMBER_COL_6, member.getLastName());
        values.put(MEMBER_COL_7, member.getGender());
        values.put(MEMBER_COL_8, member.getStatus());
        values.put(MEMBER_COL_9, member.getMaritalStatus());
        values.put(MEMBER_COL_10, member.getDob());
        values.put(MEMBER_COL_11, member.getHomePhone());
        values.put(MEMBER_COL_12, member.getMobilePhone());
        values.put(MEMBER_COL_13, member.getWorkPhone());
        values.put(MEMBER_COL_14, member.getEmail());
        values.put(MEMBER_COL_15, member.getAddress());
        values.put(MEMBER_COL_16, member.getNotes());
        values.put(MEMBER_COL_17, member.getPhotoURL());
        values.put(MEMBER_COL_18, member.getPhotoLocalPath());
        values.put(MEMBER_COL_19, member.getFingerPrint());
        values.put(MEMBER_COL_20, member.getFingerPrint1());
        values.put(MEMBER_COL_21, member.getRollNo());
        values.put(MEMBER_COL_22, member.getCreateAt());
        values.put(MEMBER_COL_23, member.getUpdateAt());

        db.update(MEMBERS_TABLE, values, MEMBER_COL_2 + " = ? ",
                new String[]{member.getId()});
    }

    //Events
    public boolean insertEventData(EventPOJO event) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EVENT_COL_2, event.getId());
        values.put(EVENT_COL_3, event.getUserId());
        values.put(EVENT_COL_4, event.getParentId());
        values.put(EVENT_COL_5, event.getEventLocationId());
        values.put(EVENT_COL_6, event.getEventCalenderId());
        values.put(EVENT_COL_7, event.getName());
        values.put(EVENT_COL_8, event.getCost());
        values.put(EVENT_COL_9, event.getAllDay());
        values.put(EVENT_COL_10, event.getStartDate());
        values.put(EVENT_COL_11, event.getStart_time());
        values.put(EVENT_COL_12, event.getEnd_time());
        values.put(EVENT_COL_13, event.getRecurring());
        values.put(EVENT_COL_14, event.getRecurFrequency());
        values.put(EVENT_COL_15, event.getRecurStartDate());
        values.put(EVENT_COL_16, event.getRecurEndDate());
        values.put(EVENT_COL_17, event.getRecurNextDate());
        values.put(EVENT_COL_18, event.getRecurType());
        values.put(EVENT_COL_19, event.getCheckInType());
        values.put(EVENT_COL_20, event.getTags());
        values.put(EVENT_COL_21, event.getIncludeCheckOut());
        values.put(EVENT_COL_22, event.getFamilyCheckIn());
        values.put(EVENT_COL_23, event.getFeatured_image());
        values.put(EVENT_COL_24, event.getGallery());
        values.put(EVENT_COL_25, event.getFiles());
        values.put(EVENT_COL_26, event.getYear());
        values.put(EVENT_COL_27, event.getNotes());
        values.put(EVENT_COL_28, event.getCreatedAt());
        values.put(EVENT_COL_29, event.getUpdatedAt());
        values.put(EVENT_COL_30, event.getBranchId());
        values.put(EVENT_COL_31, event.getEnd_date());
        values.put(EVENT_COL_32, event.getMonth());

        long result = db.insert(EVENTS_TABLE, null, values);

        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<EventPOJO> getAllEvent() {

        List<EventPOJO> events = new ArrayList<EventPOJO>();
        String selectQuery = "SELECT  * FROM " + EVENTS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {

                EventPOJO event = new EventPOJO();

                event.setId(cursor.getString(cursor.getColumnIndex(EVENT_COL_2)));
                event.setUserId(cursor.getString(cursor.getColumnIndex(EVENT_COL_3)));
                event.setParentId(cursor.getString(cursor.getColumnIndex(EVENT_COL_4)));
                event.setEventLocationId(cursor.getString(cursor.getColumnIndex(EVENT_COL_5)));
                event.setEventCalenderId(cursor.getString(cursor.getColumnIndex(EVENT_COL_6)));
                event.setName(cursor.getString(cursor.getColumnIndex(EVENT_COL_7)));
                event.setCost(cursor.getString(cursor.getColumnIndex(EVENT_COL_8)));
                event.setAllDay(cursor.getString(cursor.getColumnIndex(EVENT_COL_9)));
                event.setStartDate(cursor.getString(cursor.getColumnIndex(EVENT_COL_10)));
                event.setStart_time(cursor.getString(cursor.getColumnIndex(EVENT_COL_11)));
                event.setEnd_time(cursor.getString(cursor.getColumnIndex(EVENT_COL_12)));
                event.setRecurring(cursor.getString(cursor.getColumnIndex(EVENT_COL_13)));
                event.setRecurFrequency(cursor.getString(cursor.getColumnIndex(EVENT_COL_14)));
                event.setRecurStartDate(cursor.getString(cursor.getColumnIndex(EVENT_COL_15)));
                event.setRecurEndDate(cursor.getString(cursor.getColumnIndex(EVENT_COL_16)));
                event.setRecurNextDate(cursor.getString(cursor.getColumnIndex(EVENT_COL_17)));
                event.setRecurType(cursor.getString(cursor.getColumnIndex(EVENT_COL_18)));
                event.setCheckInType(cursor.getString(cursor.getColumnIndex(EVENT_COL_19)));
                event.setTags(cursor.getString(cursor.getColumnIndex(EVENT_COL_20)));
                event.setIncludeCheckOut(cursor.getString(cursor.getColumnIndex(EVENT_COL_21)));
                event.setFamilyCheckIn(cursor.getString(cursor.getColumnIndex(EVENT_COL_22)));
                event.setFeatured_image(cursor.getString(cursor.getColumnIndex(EVENT_COL_23)));
                event.setGallery(cursor.getString(cursor.getColumnIndex(EVENT_COL_24)));
                event.setFiles(cursor.getString(cursor.getColumnIndex(EVENT_COL_25)));
                event.setYear(cursor.getString(cursor.getColumnIndex(EVENT_COL_26)));
                event.setNotes(cursor.getString(cursor.getColumnIndex(EVENT_COL_27)));
                event.setCreatedAt(cursor.getString(cursor.getColumnIndex(EVENT_COL_28)));
                event.setUpdatedAt(cursor.getString(cursor.getColumnIndex(EVENT_COL_29)));
                event.setBranchId(cursor.getString(cursor.getColumnIndex(EVENT_COL_30)));
                event.setEnd_date(cursor.getString(cursor.getColumnIndex(EVENT_COL_31)));
                event.setMonth(cursor.getString(cursor.getColumnIndex(EVENT_COL_32)));

                events.add(event);
            } while (cursor.moveToNext());
        }
        db.close();
        return events;
    }

    public void deleteAllEvents() {
        try {
            String selectQuery = "DELETE FROM " + EVENTS_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(EVENTS_TABLE, null, null);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Event Delete-->", ex.toString());
        }
    }

    //Table Contribution
    public boolean insertOfflineContributionData(ContributionsPOJO contributions) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(CONTRIBUTION_COL_2, contributions.getBranchId());
        values.put(CONTRIBUTION_COL_3, contributions.getUserId());
        values.put(CONTRIBUTION_COL_4, contributions.getMemberId());
        values.put(CONTRIBUTION_COL_5, contributions.getFamilyId());
        values.put(CONTRIBUTION_COL_6, contributions.getFundId());
        values.put(CONTRIBUTION_COL_7, contributions.getMemberType());
        values.put(CONTRIBUTION_COL_8, contributions.getContributionBatchId());
        values.put(CONTRIBUTION_COL_9, contributions.getPaymentMethodId());
        values.put(CONTRIBUTION_COL_10, contributions.getDate());
        values.put(CONTRIBUTION_COL_11, contributions.getFiles());
        values.put(CONTRIBUTION_COL_12, contributions.getNotes());
        values.put(CONTRIBUTION_COL_13, contributions.getTransRef());
        values.put(CONTRIBUTION_COL_14, contributions.getAmount());
        values.put(CONTRIBUTION_COL_15, contributions.getYear());
        values.put(CONTRIBUTION_COL_16, contributions.getMonth());
        values.put(CONTRIBUTION_COL_17, contributions.getCreatedAt());
        values.put(CONTRIBUTION_COL_18, contributions.getUpdatedAt());
        values.put(CONTRIBUTION_COL_19, contributions.getPaymentMethod());
        values.put(CONTRIBUTION_COL_20, contributions.getBatchName());
        values.put(CONTRIBUTION_COL_21, contributions.getBatchNote());
        values.put(CONTRIBUTION_COL_22, contributions.getBatchdate());
        values.put(CONTRIBUTION_COL_23, contributions.getBatchCurrent());
        values.put(CONTRIBUTION_COL_24, contributions.getBatchStatus());


        long result = db.insert(CONTRIBUTION_TABLE, null, values);
        Log.d("TAG-->", "Values inserted into contribution");
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<ContributionsPOJO> getAllContribution() {
        List<ContributionsPOJO> events = new ArrayList<ContributionsPOJO>();
        String selectQuery = "SELECT  * FROM " + CONTRIBUTION_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {
                /*String id,branchId,userId,memberId,familyId,fundId;
                String memberType,contributionBatchId,paymentMethodId,date;
                String files,notes,transRef,amount,year,month;
                String createdAt,updatedAt;*/
                ContributionsPOJO contri = new ContributionsPOJO();

                contri.setBranchId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_2)));
                contri.setUserId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_3)));
                contri.setMemberId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_4)));
                contri.setFamilyId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_5)));
                contri.setFundId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_6)));
                contri.setMeberType(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_7)));
                contri.setContributionBatchId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_8)));
                contri.setPaymentMethodId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_9)));
                contri.setDate(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_10)));
                contri.setFiles(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_11)));
                contri.setNotes(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_12)));
                contri.setTransRef(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_13)));
                contri.setAmount(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_14)));
                contri.setYear(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_15)));
                contri.setMonth(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_16)));
                contri.setCreatedAt(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_17)));
                contri.setUpdatedAt(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_18)));

                events.add(contri);
            } while (cursor.moveToNext());
        }
        db.close();
        return events;
    }

    public List<ContributionsPOJO> getAllContributionList(String str) {
        List<ContributionsPOJO> details = new ArrayList<ContributionsPOJO>();
        String selectQuery = "SELECT  * FROM " + CONTRIBUTION_TABLE + " WHERE " + CONTRIBUTION_COL_4 + " = '" + str + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContributionsPOJO contri = new ContributionsPOJO();

                contri.setBranchId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_2)));
                contri.setUserId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_3)));
                contri.setMemberId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_4)));
                contri.setFamilyId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_5)));
                contri.setFundId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_6)));
                contri.setMeberType(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_7)));
                contri.setContributionBatchId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_8)));
                contri.setPaymentMethodId(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_9)));
                contri.setDate(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_10)));
                contri.setFiles(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_11)));
                contri.setNotes(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_12)));
                contri.setTransRef(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_13)));
                contri.setAmount(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_14)));
                contri.setYear(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_15)));
                contri.setMonth(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_16)));
                contri.setCreatedAt(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_17)));
                contri.setUpdatedAt(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_18)));
                contri.setPaymentMethod(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_19)));
                contri.setBatchName(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_20)));
                contri.setBatchNote(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_21)));
                contri.setBatchdate(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_22)));
                contri.setBatchCurrent(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_23)));
                contri.setBatchStatus(cursor.getString(cursor.getColumnIndex(CONTRIBUTION_COL_24)));

                details.add(contri);
            } while (cursor.moveToNext());
        }
        db.close();
        return details;
    }

    public void deleteAllContribution() {
        try {
            String selectQuery = "DELETE FROM " + CONTRIBUTION_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(CONTRIBUTION_TABLE, null, null);
            Log.d("TAG-->", "Values Deleted from contribution");
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Event Delete-->", ex.toString());
        }
    }

    //Table Pledges
    public boolean insertPledgeData(PledgesPOJO pledges) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PLEDGES_COL_2, pledges.getBranchId());
        values.put(PLEDGES_COL_3, pledges.getUserId());
        values.put(PLEDGES_COL_4, pledges.getMemberId());
        values.put(PLEDGES_COL_5, pledges.getFamilyId());
        values.put(PLEDGES_COL_6, pledges.getFundId());
        values.put(PLEDGES_COL_7, pledges.getPledgeType());
        values.put(PLEDGES_COL_8, pledges.getCampaignId());
        values.put(PLEDGES_COL_9, pledges.getAmount());
        values.put(PLEDGES_COL_10, pledges.getRecurring());
        values.put(PLEDGES_COL_11, pledges.getRecur_frequency());
        values.put(PLEDGES_COL_12, pledges.getRecur_type());
        values.put(PLEDGES_COL_13, pledges.getRecur_start_date());
        values.put(PLEDGES_COL_14, pledges.getRecur_end_date());
        values.put(PLEDGES_COL_15, pledges.getRecur_next_date());
        values.put(PLEDGES_COL_16, pledges.getTotal_amount());
        values.put(PLEDGES_COL_17, pledges.getTimes_number());
        values.put(PLEDGES_COL_18, pledges.getDate());
        values.put(PLEDGES_COL_19, pledges.getYear());
        values.put(PLEDGES_COL_20, pledges.getMonth());
        values.put(PLEDGES_COL_21, pledges.getNotes());
        values.put(PLEDGES_COL_22, pledges.getCreatedAt());
        values.put(PLEDGES_COL_23, pledges.getUpdatedAt());
        values.put(PLEDGES_COL_24, pledges.getCampaign_name());


        long result = db.insert(PLEDGES_TABLE, null, values);
        Log.d("TAG-->", "Values inserted into Pledge");
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<PledgesPOJO> getAllPledges() {

        List<PledgesPOJO> events = new ArrayList<PledgesPOJO>();
        String selectQuery = "SELECT  * FROM " + PLEDGES_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {

                PledgesPOJO pledges = new PledgesPOJO();

                pledges.setBranchId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_2)));
                pledges.setUserId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_3)));
                pledges.setMemberId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_4)));
                pledges.setFamilyId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_5)));
                pledges.setFundId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_6)));
                pledges.setPledgeType(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_7)));
                pledges.setCampaignId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_8)));
                pledges.setRecurring(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_9)));
                pledges.setRecur_frequency(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_10)));
                pledges.setRecur_type(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_11)));
                pledges.setRecur_start_date(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_12)));
                pledges.setRecur_end_date(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_13)));
                pledges.setRecur_next_date(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_14)));
                pledges.setTotal_amount(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_15)));
                pledges.setTimes_number(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_16)));
                pledges.setDate(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_17)));
                pledges.setYear(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_18)));
                pledges.setMonth(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_19)));
                pledges.setNotes(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_20)));
                pledges.setCreatedAt(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_21)));
                pledges.setUpdatedAt(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_22)));

                events.add(pledges);
            } while (cursor.moveToNext());
        }
        db.close();
        return events;
    }

    public List<PledgesPOJO> getAllPledgesList(String str) {
        List<PledgesPOJO> events = new ArrayList<PledgesPOJO>();
        String selectQuery = "SELECT  * FROM " + PLEDGES_TABLE + " WHERE " + PLEDGES_COL_4 + " = '" + str + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {

                PledgesPOJO pledges = new PledgesPOJO();

                pledges.setBranchId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_2)));
                pledges.setUserId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_3)));
                pledges.setMemberId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_4)));
                pledges.setFamilyId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_5)));
                pledges.setFundId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_6)));
                pledges.setPledgeType(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_7)));
                pledges.setCampaignId(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_8)));
                pledges.setAmount(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_9)));
                pledges.setRecurring(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_10)));
                pledges.setRecur_frequency(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_11)));
                pledges.setRecur_type(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_12)));
                pledges.setRecur_start_date(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_13)));
                pledges.setRecur_end_date(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_14)));
                pledges.setRecur_next_date(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_15)));
                pledges.setTotal_amount(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_16)));
                pledges.setTimes_number(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_17)));
                pledges.setDate(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_18)));
                pledges.setYear(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_19)));
                pledges.setMonth(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_20)));
                pledges.setNotes(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_21)));
                pledges.setCreatedAt(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_22)));
                pledges.setUpdatedAt(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_23)));
                pledges.setCampaign_name(cursor.getString(cursor.getColumnIndex(PLEDGES_COL_24)));

                events.add(pledges);
            } while (cursor.moveToNext());
        }
        db.close();
        return events;
    }

    public void deleteAllPledges() {
        try {
            String selectQuery = "DELETE FROM " + PLEDGES_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(PLEDGES_TABLE, null, null);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Event Delete-->", ex.toString());
        }
    }

    //Table Groups
    public boolean insertGroupsData(GroupsPOJO groups) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(GROUPS_COL_2, groups.getMemberId());
        values.put(GROUPS_COL_3, groups.getUserId());
        values.put(GROUPS_COL_4, groups.getTagId());

        long result = db.insert(GROUPS_TABLE, null, values);
        Log.d("TAG-->", "Values inserted into groups");
        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<GroupsPOJO> getAllGroups() {

        List<GroupsPOJO> events = new ArrayList<GroupsPOJO>();
        String selectQuery = "SELECT  * FROM " + GROUPS_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {

                GroupsPOJO groups = new GroupsPOJO();

                groups.setMemberId(cursor.getString(cursor.getColumnIndex(GROUPS_COL_2)));
                groups.setUserId(cursor.getString(cursor.getColumnIndex(GROUPS_COL_3)));
                groups.setTagId(cursor.getString(cursor.getColumnIndex(GROUPS_COL_4)));
                events.add(groups);
            } while (cursor.moveToNext());
        }
        db.close();
        return events;
    }

    public List<GroupsPOJO> getAllGroupsList(String str) {

        List<GroupsPOJO> groupsdetails = new ArrayList<GroupsPOJO>();
        String selectQuery = "SELECT  * FROM " + GROUPS_TABLE + " WHERE " + GROUPS_COL_2 + " = '" + str + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {
                GroupsPOJO groups = new GroupsPOJO();

                groups.setMemberId(cursor.getString(cursor.getColumnIndex(GROUPS_COL_2)));
                groups.setUserId(cursor.getString(cursor.getColumnIndex(GROUPS_COL_3)));
                groups.setTagId(cursor.getString(cursor.getColumnIndex(GROUPS_COL_4)));

                groupsdetails.add(groups);
            } while (cursor.moveToNext());
        }
        db.close();
        return groupsdetails;
    }

    public void deleteAllGroups() {
        try {
            String selectQuery = "DELETE FROM " + GROUPS_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(GROUPS_TABLE, null, null);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Event Delete-->", ex.toString());
        }
    }

    //Table Family
    public boolean insertFAMILYData(FamilyPOJO family) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();


        // String id,branchId,userId,memberId,name,notes,picture,createdAt,updatedAt;


        values.put(FAMILY_COL_2, family.getBranchId());
        values.put(FAMILY_COL_3, family.getUserId());
        values.put(FAMILY_COL_4, family.getMemberId());
        values.put(FAMILY_COL_5, family.getName());
        values.put(FAMILY_COL_6, family.getNotes());
        values.put(FAMILY_COL_7, family.getPicture());
        values.put(FAMILY_COL_8, family.getCreatedAt());
        values.put(FAMILY_COL_9, family.getUpdatedAt());

        long result = db.insert(FAMILY_TABLE, null, values);

        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<FamilyPOJO> getAllFamily() {

        List<FamilyPOJO> events = new ArrayList<FamilyPOJO>();
        String selectQuery = "SELECT  * FROM " + FAMILY_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {

                FamilyPOJO family = new FamilyPOJO();

                // String id,branchId,userId,memberId,name,notes,picture,createdAt,updatedAt;

                family.setBranchId(cursor.getString(cursor.getColumnIndex(FAMILY_COL_2)));
                family.setUserId(cursor.getString(cursor.getColumnIndex(FAMILY_COL_3)));
                family.setMemberId(cursor.getString(cursor.getColumnIndex(FAMILY_COL_4)));
                family.setName(cursor.getString(cursor.getColumnIndex(FAMILY_COL_5)));
                family.setNotes(cursor.getString(cursor.getColumnIndex(FAMILY_COL_6)));
                family.setPicture(cursor.getString(cursor.getColumnIndex(FAMILY_COL_7)));
                family.setCreatedAt(cursor.getString(cursor.getColumnIndex(FAMILY_COL_8)));
                family.setUpdatedAt(cursor.getString(cursor.getColumnIndex(FAMILY_COL_9)));
                events.add(family);
            } while (cursor.moveToNext());
        }
        db.close();
        return events;
    }

    public List<FamilyPOJO> getAllFamilyList(String str) {

        List<FamilyPOJO> details = new ArrayList<FamilyPOJO>();
        String selectQuery = "SELECT  * FROM " + FAMILY_TABLE + " WHERE " + FAMILY_COL_4 + " = '" + str + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {
                FamilyPOJO family = new FamilyPOJO();

                // String id,branchId,userId,memberId,name,notes,picture,createdAt,updatedAt;

                family.setBranchId(cursor.getString(cursor.getColumnIndex(FAMILY_COL_2)));
                family.setUserId(cursor.getString(cursor.getColumnIndex(FAMILY_COL_3)));
                family.setMemberId(cursor.getString(cursor.getColumnIndex(FAMILY_COL_4)));
                family.setName(cursor.getString(cursor.getColumnIndex(FAMILY_COL_5)));
                family.setNotes(cursor.getString(cursor.getColumnIndex(FAMILY_COL_6)));
                family.setPicture(cursor.getString(cursor.getColumnIndex(FAMILY_COL_7)));
                family.setCreatedAt(cursor.getString(cursor.getColumnIndex(FAMILY_COL_8)));
                family.setUpdatedAt(cursor.getString(cursor.getColumnIndex(FAMILY_COL_9)));

                details.add(family);
            } while (cursor.moveToNext());
        }
        db.close();
        return details;
    }

    public void deleteAllFamily() {
        try {
            String selectQuery = "DELETE FROM " + FAMILY_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(FAMILY_TABLE, null, null);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Event Delete-->", ex.toString());
        }
    }

    //Table Event Attendance
    public boolean insertEventAttendaceData(EventAttendancePOJO eventAttendancePOJO) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(EVENT_ATTEN_COL_2, eventAttendancePOJO.getEventId());
        values.put(EVENT_ATTEN_COL_3, eventAttendancePOJO.getUserId());
        values.put(EVENT_ATTEN_COL_4, eventAttendancePOJO.getMemberId());
        values.put(EVENT_ATTEN_COL_5, eventAttendancePOJO.getAnonymous());
        values.put(EVENT_ATTEN_COL_6, eventAttendancePOJO.getDate());
        values.put(EVENT_ATTEN_COL_7, eventAttendancePOJO.getCreatedAt());
        values.put(EVENT_ATTEN_COL_8, eventAttendancePOJO.getUpdatedAt());

        long result = db.insert(EVENT_ATTEN_TABLE, null, values);

        db.close();
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public List<AttenderPOJO> getAllAttender(String eventId) {

        List<AttenderPOJO> events = new ArrayList<AttenderPOJO>();
        //selectQuery = "SELECT  * FROM " + EVENT_ATTEN_TABLE;
        //Query to sort data using event id of all attenders
        String selectQuery = "SELECT member_table.id,member_table.first_name,member_table.last_name,member_table.gender,member_table.dob,member_table.mobile_phone,member_table.address,member_table.photo_url,member_table.rollno FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {

                AttenderPOJO attenderPOJO = new AttenderPOJO();

                // String id,branchId,userId,memberId,name,notes,picture,createdAt,updatedAt;

                attenderPOJO.setId(cursor.getString(cursor.getColumnIndex("rollno")));
                attenderPOJO.setF_name(cursor.getString(cursor.getColumnIndex("first_name")));
                attenderPOJO.setL_name(cursor.getString(cursor.getColumnIndex("last_name")));
                attenderPOJO.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                attenderPOJO.setAge(cursor.getString(cursor.getColumnIndex("dob")));
                attenderPOJO.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                attenderPOJO.setPhone(cursor.getString(cursor.getColumnIndex("mobile_phone")));
                attenderPOJO.setPhotoURL(cursor.getString(cursor.getColumnIndex("photo_url")));
                // attenderPOJO.setPhotoURL(cursor.getString(cursor.getColumnIndex("photo_url")));
                events.add(attenderPOJO);
            } while (cursor.moveToNext());
        }
        db.close();
        return events;
    }

    @SuppressLint("LongLogTag")
    public List<EventAttendancePOJO> getAllAttendance(String str) {

        List<EventAttendancePOJO> details = new ArrayList<EventAttendancePOJO>();
        //String selectQuery = "SELECT  * FROM " + EVENT_ATTEN_TABLE + " WHERE "+ EVENT_ATTEN_COL_4 +" = '" + str + "'";

        //String selectMember = "SELECT  "+ EVENT_COL_7 +" FROM " + EVENTS_TABLE + " WHERE "+ EVENT_COL_2 +" = '" + eventId + "'";
        String selectMember = "SELECT name as n_name,date as d_date FROM event_table INNER JOIN EVENT_ATTEN_table ON event_table.id = EVENT_ATTEN_table.event_id WHERE member_id = '" + str + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        // Cursor cursor = db.rawQuery(selectQuery,null);
        Cursor cursor = db.rawQuery(selectMember, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {
                EventAttendancePOJO eventAttendancePOJO = new EventAttendancePOJO();

//                eventAttendancePOJO.setEventId(cursor.getString(cursor.getColumnIndex(EVENT_ATTEN_COL_2)));
//                eventAttendancePOJO.setUserId(cursor.getString(cursor.getColumnIndex(EVENT_ATTEN_COL_3)));
//                eventAttendancePOJO.setMemberId(cursor.getString(cursor.getColumnIndex(EVENT_ATTEN_COL_4)));
//                eventAttendancePOJO.setAnonymous(cursor.getString(cursor.getColumnIndex(EVENT_ATTEN_COL_5)));
//                eventAttendancePOJO.setCreatedAt(cursor.getString(cursor.getColumnIndex(EVENT_ATTEN_COL_7)));
//                eventAttendancePOJO.setUpdatedAt(cursor.getString(cursor.getColumnIndex(EVENT_ATTEN_COL_8)));
//                //event column
                eventAttendancePOJO.setDate(cursor.getString(cursor.getColumnIndex("d_date")));
                eventAttendancePOJO.setEventName(cursor.getString(cursor.getColumnIndex("n_name")));

                details.add(eventAttendancePOJO);
                Log.d("Attendance_list_database", details.toString());


            } while (cursor.moveToNext());
        }
        db.close();
        return details;
    }

    public void deleteAllEventAttendance() {
        try {
            String selectQuery = "DELETE FROM " + EVENT_ATTEN_TABLE;
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(EVENT_ATTEN_TABLE, null, null);
            db.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.e("Event Delete-->", ex.toString());
        }
    }


    //Counts for Event Reprot
    //Gender
    public int maleCount(String eventId) {
        int male = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.gender) as male FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND gender = 'male'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                male = cursor.getInt(cursor.getColumnIndex("male"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return male;
    }

    public int femaleCount(String eventId) {
        int female = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.gender) as male FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND gender = 'female'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                female = cursor.getInt(cursor.getColumnIndex("male"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return female;
    }

    public int UnknownGenderCount(String eventId) {
        int female = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.gender) as male FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND gender = 'unknown'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                female = cursor.getInt(cursor.getColumnIndex("male"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return female;
    }

    //Marital_Status
    public int MarriedCount(String eventId) {
        int married = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.marital_status) as maritalstatus FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND marital_status = 'married'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                married = cursor.getInt(cursor.getColumnIndex("maritalstatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return married;
    }

    public int EngagedCount(String eventId) {
        int married = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.marital_status) as maritalstatus FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND marital_status = 'engaged'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                married = cursor.getInt(cursor.getColumnIndex("maritalstatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return married;
    }

    public int SeparatedCount(String eventId) {
        int married = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.marital_status) as maritalstatus FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND marital_status = 'separated'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                married = cursor.getInt(cursor.getColumnIndex("maritalstatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return married;
    }

    public int widowedCount(String eventId) {
        int married = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.marital_status) as maritalstatus FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND marital_status = 'widowed'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                married = cursor.getInt(cursor.getColumnIndex("maritalstatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return married;
    }

    public int DivorcedCount(String eventId) {
        int married = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.marital_status) as maritalstatus FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND marital_status = 'divorced'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                married = cursor.getInt(cursor.getColumnIndex("maritalstatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return married;
    }

    public int SingleCount(String eventId) {
        int married = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.marital_status) as maritalstatus FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND marital_status = 'single'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                married = cursor.getInt(cursor.getColumnIndex("maritalstatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return married;
    }

    public int UnknownMaritalCount(String eventId) {
        int married = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.marital_status) as maritalstatus FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND marital_status = 'unknown'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                married = cursor.getInt(cursor.getColumnIndex("maritalstatus"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return married;
    }

    //Status
    public int AttendersCount(String eventId) {
        int status = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.status) as status FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND status = 'attender'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                status = cursor.getInt(cursor.getColumnIndex("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int VisitorsCount(String eventId) {
        int status = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.status) as status FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND status = 'visitor'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                status = cursor.getInt(cursor.getColumnIndex("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int membersCount(String eventId) {
        int status = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.status) as status FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND status = 'member'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                status = cursor.getInt(cursor.getColumnIndex("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int InactiveCount(String eventId) {
        int status = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.status) as status FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND status = 'inactive'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                status = cursor.getInt(cursor.getColumnIndex("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public int unknownStatusCount(String eventId) {
        int status = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String selectQuery = ("SELECT count(member_table.status) as status FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'" + " AND status = 'unknown'");
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToLast()) {
                status = cursor.getInt(cursor.getColumnIndex("status"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    //Age wise Distribution
    public float[] age(String eventId) {

        float countAges[] = new float[9];
        int count = 0;

        List<String> details = new ArrayList<String>();
        String selectMember = "SELECT member_table.dob as dob FROM member_table INNER JOIN EVENT_ATTEN_table ON member_table.id = EVENT_ATTEN_table.member_id WHERE event_id = '" + eventId + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        // Cursor cursor = db.rawQuery(selectQuery,null);
        Cursor cursor = db.rawQuery(selectMember, null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()) {
            do {
                details.add(cursor.getString(cursor.getColumnIndex("dob")));
            } while (cursor.moveToNext());
        }
        db.close();

        int n = details.size();
        int[] ages = new int[n];

        for (int i = 0; i < details.size(); i++) {
            if (details.get(i) != null) {
                String birthdateStr = details.get(i);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                try {
                    Date birthdate = df.parse(birthdateStr);
                    ages[i] = Integer.valueOf(calculateAge(birthdate));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(ages[i]<=6 )
                {
                    countAges[0] = count+1;
                    Log.e("countArray",countAges[0]+"");

                }else if(ages[i]>6 && ages[i]<=12)
                {
                    countAges[1] = count+1;
                    Log.e("countArray",countAges[1]+"");

                }
                else if(ages[i]>=13 && ages[i]<=18)
                {
                    countAges[2] = count+1;
                    Log.e("countArray",countAges[2]+"");

                }
                else if(ages[i]>=19 && ages[i]<=29)
                {
                    countAges[3] = count+1;
                    Log.e("countArray",countAges[3]+"");

                }
                else if(ages[i]>=30 && ages[i]<=49)
                {
                    countAges[4] = count+1;
                    Log.e("countArray",countAges[4]+"");

                }
                else if(ages[i]>=50 && ages[i]<=64)
                {
                    countAges[5] = count+1;
                    Log.e("countArray",countAges[5]+"");

                }
                else if(ages[i]>=65 && ages[i]<=79)
                {
                    countAges[6] = count+1;
                    Log.e("countArray",countAges[6]+"");

                }
                else if(ages[i]>=80 && ages[i]<=150)
                {
                    countAges[7] = count+1;
                    Log.e("countArray",countAges[7]+"");

                }
                else
                {
                    countAges[8] = count+1;
                    Log.e("countArray",countAges[8]+"");

                }
            }
        }
        return countAges;
    }
    public int calculateAge(Date birthdate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthdate);
        Calendar today = Calendar.getInstance();

        int yearDifference = today.get(Calendar.YEAR)
                - birth.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
            yearDifference--;
        } else {
            if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < birth
                    .get(Calendar.DAY_OF_MONTH)) {
                yearDifference--;
            }

        }
        return yearDifference;
    }
}
