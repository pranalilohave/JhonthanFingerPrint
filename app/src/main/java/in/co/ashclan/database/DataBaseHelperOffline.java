package in.co.ashclan.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import in.co.ashclan.model.MemberPOJO;

public class DataBaseHelperOffline extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dataBaseOffline.db";

    public static final String MEMBERS_OFFLINE_TABLE = "member_offline_table";
    public static final String MEMBER_COl_1 = "member_id";
    public static final String MEMBER_COL_2 = "first_name";
    public static final String MEMBER_COL_3 = "middle_name";
    public static final String MEMBER_COL_4 = "last_name";
    public static final String MEMBER_COL_5 = "gender";
    public static final String MEMBER_COL_6 = "status";
    public static final String MEMBER_COL_7 = "marital_status";
    public static final String MEMBER_COL_8 = "dob";
    public static final String MEMBER_COL_9 = "home_phone";
    public static final String MEMBER_COL_10 = "mobile_phone";
    public static final String MEMBER_COL_11 = "work_phone";
    public static final String MEMBER_COL_12 = "email";
    public static final String MEMBER_COL_13 = "address";
    public static final String MEMBER_COL_14 = "notes";
    public static final String MEMBER_COL_15 = "photo_local_path";
    public static final String MEMBER_COL_16 = "fingerprint";
    public static final String MEMBER_COL_17 = "server_type";
    public static final String MEMBER_COL_18 = "mem_id";
    public static final String MEMBER_COL_19 = "fingerprint2";

    public static final String CREATE_TABLE_MEMBER_OFFLINE =
            "CREATE TABLE " + MEMBERS_OFFLINE_TABLE + "("
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
                    + MEMBER_COL_19 + " TEXT"
                    + ")";


    public DataBaseHelperOffline(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase dbOffline) {
        dbOffline.execSQL(CREATE_TABLE_MEMBER_OFFLINE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MEMBERS_OFFLINE_TABLE);
        onCreate(db);
    }



    public boolean insertOfflineMemberData(MemberPOJO member) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(MEMBER_COL_2,member.getFirstName());
        values.put(MEMBER_COL_3,member.getMiddleName());
        values.put(MEMBER_COL_4,member.getLastName());
        values.put(MEMBER_COL_5,member.getGender());
        values.put(MEMBER_COL_6,member.getStatus());
        values.put(MEMBER_COL_7,member.getMaritalStatus());
        values.put(MEMBER_COL_8,member.getDob());
        values.put(MEMBER_COL_9,member.getHomePhone());
        values.put(MEMBER_COL_10,member.getMobilePhone());
        values.put(MEMBER_COL_11,member.getWorkPhone());
        values.put(MEMBER_COL_12,member.getEmail());
        values.put(MEMBER_COL_13,member.getAddress());
        values.put(MEMBER_COL_14,member.getNotes());
        values.put(MEMBER_COL_15,member.getPhotoLocalPath());
        values.put(MEMBER_COL_16,member.getFingerPrint());
        values.put(MEMBER_COL_17,member.getServerType());
        values.put(MEMBER_COL_18,member.getId());
        values.put(MEMBER_COL_19,member.getFingerPrint2());


        long result = db.insert(MEMBERS_OFFLINE_TABLE,null,values);

        db.close();
        if(result==-1){
            return false;
        }else {
            return true;
        }
    }
    public List<MemberPOJO> getAllOfflineMembers(){

        List<MemberPOJO> members = new ArrayList<MemberPOJO>();

        String selectQuery = "SELECT  * FROM " + MEMBERS_OFFLINE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        //looping through all row and adding to list
        if (cursor.moveToFirst()){
            do {

                MemberPOJO member = new MemberPOJO();
                int id = cursor.getInt(cursor.getColumnIndex(MEMBER_COl_1));
                member.setId(String.valueOf(id));
                member.setUserId(cursor.getString(cursor.getColumnIndex(MEMBER_COL_18)));
                member.setFirstName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_2)));
                member.setMiddleName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_3)));
                member.setLastName(cursor.getString(cursor.getColumnIndex(MEMBER_COL_4)));
                member.setGender(cursor.getString(cursor.getColumnIndex(MEMBER_COL_5)));
                member.setStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_6)));
                member.setMaritalStatus(cursor.getString(cursor.getColumnIndex(MEMBER_COL_7)));
                member.setDob(cursor.getString(cursor.getColumnIndex(MEMBER_COL_8)));
                member.setHomePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_9)));
                member.setMobilePhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_10)));
                member.setWorkPhone(cursor.getString(cursor.getColumnIndex(MEMBER_COL_11)));
                member.setEmail(cursor.getString(cursor.getColumnIndex(MEMBER_COL_12)));
                member.setAddress(cursor.getString(cursor.getColumnIndex(MEMBER_COL_13)));
                member.setNotes(cursor.getString(cursor.getColumnIndex(MEMBER_COL_14)));
                member.setPhotoLocalPath(cursor.getString(cursor.getColumnIndex(MEMBER_COL_15)));
                member.setFingerPrint(cursor.getString(cursor.getColumnIndex(MEMBER_COL_16)));
                member.setServerType(cursor.getString(cursor.getColumnIndex(MEMBER_COL_17)));
                member.setFingerPrint2(cursor.getString(cursor.getColumnIndex(MEMBER_COL_19)));

                members.add(member);
            }while (cursor.moveToNext());
        }
        db.close();
        return members;
    }
    public void deleteOfflineMember(MemberPOJO member) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MEMBERS_OFFLINE_TABLE, MEMBER_COl_1 + " = ?",
                new String[]{String.valueOf(member.getId())});
        db.close();
    }
}