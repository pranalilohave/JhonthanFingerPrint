package in.co.ashclan.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.VisibleForTesting;

import java.security.PublicKey;

public class PreferenceUtils {

    public static final String PREFERENCE_KEY = "church";

    public static final String TEST_MATCH = "testMatch";

    public static final String SELECT_SERVER="selectServer";

    public static final String PREFERENCE_KEY_INTERNET_ACCESS="internetAccess";

    public static final String PREFERENCE_KEY_SIGN_IN="signIn";

    public static final String PREFERENCE_KEY_ADMIN_NAME = "adminName";
    public static final String PREFERENCE_KEY_ADMIN_PASSWORD = "adminPassword";
    public static final String PREFERENCE_KEY_TOKEN = "token";

    public static final String URL_LOGIN="login";
    public static final String URL_GET_ALL_MEMBERS="getAllMembers";
    public static final String URL_GET_ALL_EVENTS="getAllEvents";
    public static final String URL_CREATE_MEMBER="createMember";
    public static final String URL_GET_CONTRIBUTIONS="getContributions";
    public static final String URL_GET_FAMILY="getFamily";
    public static final String URL_GET_PLEDGES="getPledges";
    public static final String URL_GET_GROUP="getGroup";
    public static final String URL_ADD_CHECKIN="addCheckIn";
    public static final String URL_UPLOAD_IMAGE="uploadImage";

    public static final String URL_UPDATE_MEMBER="updateMember";
    public static final String URL_GET_ALL_MEMBER_EVENTS="getAllMemberEvents";

    public static final String URL_GET_ALL_MEMBER_DETAILS="getAllMemberData";
    public static final String URL_ADD_LOCATION="addLocations";
    public static final String URL_ADD_CALENDAR="addCalender";
    public static final String URL_EVENT_REGISTRATION="eventRegistrations";


    //prevent instantiation
    private PreferenceUtils(){}

    public static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(PREFERENCE_KEY,Context.MODE_PRIVATE);
    }

    public static String getUrlGetAllMembersDetails(Context context){
        return getSharedPreferences(context).getString(URL_GET_ALL_MEMBER_DETAILS,"");
    }
    public static void setUrlGetAllMemberDetails(Context context,String getAllMemberDetails){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_ALL_MEMBER_DETAILS,getAllMemberDetails).apply();
    }

    public static void setTestMatch(Context context,String test){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(TEST_MATCH,test).apply();
    }

    public static String getTestMatch(Context context){
        return getSharedPreferences(context).getString(TEST_MATCH,"");
    }


    public static void setSelectServer(Context context,int i){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(SELECT_SERVER,i).apply();
    }

    public static int getSelectServer(Context context){
        return getSharedPreferences(context).getInt(SELECT_SERVER,0);
    }

    public static void setInternetAccess(Context context,boolean internetAccess){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREFERENCE_KEY_INTERNET_ACCESS,internetAccess).apply();
    }

    public static boolean getInternetAccess(Context context){
        return getSharedPreferences(context).getBoolean(PREFERENCE_KEY_INTERNET_ACCESS,false);
    }



    public static void setSignIn(Context context,boolean signIn){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putBoolean(PREFERENCE_KEY_SIGN_IN,signIn).apply();
    }

    public static boolean getSignIn(Context context){
        return getSharedPreferences(context).getBoolean(PREFERENCE_KEY_SIGN_IN,false);
    }

    public static void setAdminName(Context context,String adminName){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_KEY_ADMIN_NAME,adminName).apply();
    }

    public static String getAdminName(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_KEY_ADMIN_NAME,"");
    }

    public static void setAdminPassword(Context context,String adminPassword){
        SharedPreferences.Editor editor =getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_KEY_ADMIN_PASSWORD,adminPassword).apply();
    }

    public static String getAdminPassword(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_KEY_ADMIN_PASSWORD,"");
    }

    public static void setToken(Context context, String token){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(PREFERENCE_KEY_TOKEN,token).apply();
    }

    public static String getToken(Context context){
        return getSharedPreferences(context).getString(PREFERENCE_KEY_TOKEN,"");
    }

    public static void setUrlLogin(Context context,String login){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_LOGIN,login).apply();
    }
    public static String getUrlLogin(Context context){
        return getSharedPreferences(context).getString(URL_LOGIN,"");
    }

    public static void setUrlGetAllMembers(Context context, String getAllMember){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_ALL_MEMBERS,getAllMember).apply();
    }

    public static String getUrlGetAllMembers(Context context){
        return getSharedPreferences(context).getString(URL_GET_ALL_MEMBERS,"");
    }

    public static void setUrlGetAllEvents(Context context,String getAllEvent){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_ALL_EVENTS,getAllEvent).apply();
    }

    public static String getUrlGetAllEvents(Context context){
        return getSharedPreferences(context).getString(URL_GET_ALL_EVENTS,"");
    }

    public static void setUrlCreateMember(Context context,String createMember){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_CREATE_MEMBER,createMember).apply();
    }
    public static String getUrlCreateMember(Context context){
        return getSharedPreferences(context).getString(URL_CREATE_MEMBER,"");
    }

    public static void setUrlGetContributions(Context context, String getContributions){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_CONTRIBUTIONS,getContributions).apply();
    }
    public static String getUrlGetContributions(Context context){
        return getSharedPreferences(context).getString(URL_GET_CONTRIBUTIONS,"");
    }

    public static void setUrlGetPledges(Context context,String getPledges){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_PLEDGES,getPledges).apply();
    }
    public static String getUrlGetPledges(Context context){
        return getSharedPreferences(context).getString(URL_GET_PLEDGES,"");
    }

    public static void setUrlGetFamily(Context context,String getFamily){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_FAMILY,getFamily).apply();
    }
    public static String getUrlGetFamily(Context context){
        return getSharedPreferences(context).getString(URL_GET_FAMILY,"");
    }

    public static void setUrlGetGroup(Context context,String getGroup){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_GROUP,getGroup).apply();
    }
    public static String getUrlGetGroup(Context context){
        return getSharedPreferences(context).getString(URL_GET_GROUP,"");
    }

    public static void setUrlAddCheckin(Context context,String addCheckIn){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_ADD_CHECKIN,addCheckIn).apply();
    }
    public static String getUrlAddCheckin(Context context){
        return getSharedPreferences(context).getString(URL_ADD_CHECKIN,"");
    }

    public static void setUrlUploadImage(Context context,String uploadImage){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_UPLOAD_IMAGE,uploadImage).apply();
    }
    public static String getUrlUploadImage(Context context){
        return getSharedPreferences(context).getString(URL_UPLOAD_IMAGE,"");
    }

    public static void setUrlUpdateMember(Context context,String updateMember){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_UPDATE_MEMBER,updateMember).apply();
    }
    public static String getUrlUpdateMember(Context context){
        return getSharedPreferences(context).getString(URL_UPDATE_MEMBER,"");
    }

    public static void setUrlGetAllMemberEvents(Context context,String getAllMemberEvents){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_GET_ALL_MEMBER_EVENTS,getAllMemberEvents).apply();
    }
    public static String getUrlGetAllMemberEvents(Context context){
        return getSharedPreferences(context).getString(URL_GET_ALL_MEMBER_EVENTS,"");
    }


    public static void setUrlAddLocation(Context context,String addLocation){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_ADD_LOCATION,addLocation).apply();
    }
    public static String getUrlAddLocation(Context context){
        return getSharedPreferences(context).getString(URL_ADD_LOCATION,"");
    }

    public static void setUrlAddCalendar(Context context,String addCalendar){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_ADD_CALENDAR,addCalendar).apply();
    }
    public static String getUrlAddCalendar(Context context){
        return getSharedPreferences(context).getString(URL_ADD_CALENDAR,"");
    }

    public static void setUrlEventRegistration(Context context,String eventRegistration){
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(URL_EVENT_REGISTRATION,eventRegistration).apply();
    }
    public static String getUrlEventRegistration(Context context){
        return getSharedPreferences(context).getString(URL_EVENT_REGISTRATION,"");
    }


}
