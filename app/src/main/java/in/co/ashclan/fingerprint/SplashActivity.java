package in.co.ashclan.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.utils.Constants;
import io.fabric.sdk.android.Fabric;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.UUID;

import in.co.ashclan.checkinternet.ConnectivityReceiver;
import in.co.ashclan.checkinternet.MyApplication;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

public class SplashActivity extends AppCompatActivity{// implements ConnectivityReceiver.ConnectivityReceiverListener {

    String email,password;
    ArrayList<MemberPOJO> memberList = new ArrayList<MemberPOJO>();
    ArrayList<EventPOJO> eventList = new ArrayList<EventPOJO>();
    DataBaseHelper dataBaseHelper;
    Context mContext;
   // LinearLayout layout;
    //Button buttonOffline,buttonOnline;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash);
        mContext=this;
        dataBaseHelper = new DataBaseHelper(this);
        Constants.setURL(this,PreferenceUtils.getSelectServer(this));
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
        {
            Toast.makeText(SplashActivity.this,"Tablet",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(SplashActivity.this,"Phone",Toast.LENGTH_LONG).show();
            Log.e("-->",PreferenceUtils.getUrlLogin(this).toString());

        }

        inits();

        //checkConnection();
        if(PreferenceUtils.getSignIn(this)){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                    //        layout.setVisibility(View.VISIBLE);
                }
            },1500);
        }else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            },1500);


          /*
            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
*/
        }

      }
      public void inits(){
          email=PreferenceUtils.getAdminName(SplashActivity.this);
          password=PreferenceUtils.getAdminPassword(SplashActivity.this);
        //  layout = (LinearLayout)findViewById(R.id.layout_splash);
       //   buttonOffline=(Button)findViewById(R.id.button_offline);
        //  buttonOnline=(Button)findViewById(R.id.button_online);
      }



    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        // MyApplication.getInstance().setConnectivityListener(this);
    }
/*
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if(isConnected){
            Toast.makeText(SplashActivity.this,"INTERNET ON",Toast.LENGTH_LONG).show();
            PreferenceUtils.setInternetAccess(SplashActivity.this,true);
        //    sGetAccessToken(email,password);
        }else{
            Toast.makeText(SplashActivity.this, "INTERNET OFF", Toast.LENGTH_SHORT).show();
            PreferenceUtils.setInternetAccess(SplashActivity.this,false);
        }

    }
*/
/*
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        PreferenceUtils.setInternetAccess(SplashActivity.this,isConnected);
        if(isConnected){

        }else{

        }
    }
*/
    //email=test@gmail.com ***** password=123456
    public void getAccessTokenVolley(String URL, String email, String password){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser_obj = new JSONParser();
                        String token = null;
                        try {
                            JSONObject object = (JSONObject) parser_obj.parse(response);
                            token = (String)object.get("result");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                     //   Toast.makeText(SplashActivity.this,token,Toast.LENGTH_SHORT).show();
                        if(token.equalsIgnoreCase("wrong email or password.")|| !PreferenceUtils.getSignIn(SplashActivity.this)){
                            Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            PreferenceUtils.setToken(SplashActivity.this, token);
                            /*
                            Intent intent = new Intent(SplashActivity.this, DataLoadingActivity.class);
                            intent.putExtra("token", "");
                            startActivity(intent);
                            finish();
                            */
                            //"http://52.172.221.235:8983/api/get_all_members"
                            memberDetailsVolley(PreferenceUtils.getUrlGetAllMembers(SplashActivity.this),PreferenceUtils.getToken(SplashActivity.this));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        smr.addStringParam("email",email)
                .addStringParam("password",password);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);

    }
    public void memberDetailsVolley(String URL, String token){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(SplashActivity.this,response,Toast.LENGTH_SHORT).show();
                        dataBaseHelper.deleteAllMembers();
                        JSONParser parser = new JSONParser();
                        JSONArray jsonArray = null;
                        try{
                            jsonArray = (JSONArray)parser.parse(response);

                            for(int i=0;i<jsonArray.size();i++){

                                MemberPOJO member = new MemberPOJO();

                                JSONObject object = (JSONObject)jsonArray.get(i);

                                member.setId(String.valueOf(object.get("id")));
                                member.setUserId(String.valueOf(object.get("user_id")));

                                member.setFirstName(isNull(object,"first_name"));
                                member.setMiddleName(isNull(object,"middle_name"));
                                member.setLastName(isNull(object,"last_name"));
                                member.setGender(isNull(object,"gender"));
                                member.setStatus(isNull(object,"status"));
                                member.setMaritalStatus(isNull(object,"marital_status"));
                                member.setDob(isNull(object,"dob","0000-00-00"));
                                member.setHomePhone(isNull(object,"home_phone"));
                                member.setMobilePhone(isNull(object,"mobile_phone"));
                                member.setWorkPhone(isNull(object,"work_phone"));
                                member.setEmail(isNull(object,"email"));
                                member.setAddress(isNull(object,"address"));
                                member.setNotes(isNull(object,"notes"));
                                member.setRollNo(isNull(object,"No"));
                                member.setCreateAt(isNull(object,"created_at"));
                                member.setUpdateAt(isNull(object,"updated_at"));
                                member.setFingerPrint(isNull(object,"fingerprint"));


                                //remainber to do changes...
                                member.setPhotoURL(isNull(object,"photo",""));

                                memberList.add(member);
                                dataBaseHelper.insertMemberData(member);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //"http://52.172.221.235:8983/api/get_all_events"
                        eventDetailsVolley(PreferenceUtils.getUrlGetAllEvents(SplashActivity.this),
                                PreferenceUtils.getToken(SplashActivity.this));

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });


        smr.addStringParam("token",token);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);
    }
    public void eventDetailsVolley(String URL,String token){

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dataBaseHelper.deleteAllEvents();
                        JSONParser parser = new JSONParser();
                        JSONArray jsonArray = null;
                        try{
                            jsonArray = (JSONArray)parser.parse(response);

                            for(int i=0;i<jsonArray.size();i++){

                                EventPOJO event = new EventPOJO();
                                JSONObject object = (JSONObject)jsonArray.get(i);
                                event.setId(String.valueOf(object.get("id")));
                                event.setBranchId(isNull(object,"branch_id"));
                                event.setUserId(isNull(object,"user_id"));
                                event.setParentId(isNull(object,"parent_id"));
                                event.setEventLocationId(isNull(object,"event_location_id"));
                                event.setEventCalenderId(isNull(object,"event_calendar_id"));
                                event.setName(isNull(object,"name"));
                                event.setCost(isNull(object,"cost"));
                                event.setAllDay(isNull(object,"all_day"));
                                event.setStartDate(isNull(object,"start_date"));
                                event.setStart_time(isNull(object,"start_time"));
                                event.setEnd_date(isNull(object,"end_date"));
                                event.setEnd_time(isNull(object,"end_time"));
                                event.setRecurring(isNull(object,"recurring"));
                                event.setRecurFrequency(isNull(object,"recur_frequency"));
                                event.setRecurStartDate(isNull(object,"recur_start_date"));
                                event.setRecurEndDate(isNull(object,"recur_end_date"));
                                event.setRecurNextDate(isNull(object,"recur_next_date"));
                                event.setRecurType(isNull(object,"recur_type"));
                                event.setCheckInType(isNull(object,"checkin_type"));
                                event.setTags(isNull(object,"tags"));
                                event.setIncludeCheckOut(isNull(object,"include_checkout"));
                                event.setFamilyCheckIn(isNull(object,"family_checkin"));
                                event.setFeatured_image(isNull(object,"featured_image"));
                                event.setGallery(isNull(object,"gallery"));
                                event.setFiles(isNull(object,"files"));
                                event.setYear(isNull(object,"year"));
                                event.setMonth(isNull(object,"month"));
                                event.setNotes(isNull(object,"notes"));
                                event.setCreatedAt(isNull(object,"created_at"));
                                event.setUpdatedAt(isNull(object,"updated_at"));
                                eventList.add(event);
                                dataBaseHelper.insertEventData(event);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                        intent.putExtra("member_list",memberList);
                        startActivity(intent);
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        smr.addStringParam("token",token);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);
    }


    public void getAccessTokenGovNet(String URL,String email,String password){

        Log.e("--->",URL+"   "+email+"  "+password);
        //getting the actual path of the image
        final String path;// = getPathVideo(filePath);

        //Uploading code
        try {
            final String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("email",email)
                    .addParameter("password",password)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            Log.e("--->",serverResponse.getBodyAsString().toString());
                            JSONParser parser_obj = new JSONParser();
                            String token = null;
                            try {
                                JSONObject object = (JSONObject) parser_obj.parse(serverResponse.getBodyAsString().toString());
                                token = (String)object.get("result");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //   Toast.makeText(SplashActivity.this,token,Toast.LENGTH_SHORT).show();
                            if(token.equalsIgnoreCase("wrong email or password.")|| !PreferenceUtils.getSignIn(SplashActivity.this)){
                                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }else {
                                PreferenceUtils.setToken(SplashActivity.this, token);

     /*                       Intent intent = new Intent(SplashActivity.this, DataLoadingActivity.class);
                            //intent.putExtra("token", "");
                            startActivity(intent);
                            finish();
*/
                                //"http://52.172.221.235:8983/api/get_all_members"
                                memberDetailsGovNet(PreferenceUtils.getUrlGetAllMembers(mContext), PreferenceUtils.getToken(mContext));
                            }

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln");
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void memberDetailsGovNet(String URL, String token){
        try {
            final String uploadId = UUID.randomUUID().toString();

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("token",token)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                            dataBaseHelper.deleteAllMembers();
                            JSONParser parser = new JSONParser();
                            JSONArray jsonArray = null;
                            try{
                                jsonArray = (JSONArray)parser.parse(serverResponse.getBodyAsString().toString());

                                for(int i=0;i<jsonArray.size();i++){

                                    MemberPOJO member = new MemberPOJO();

                                    JSONObject object = (JSONObject)jsonArray.get(i);

                                    member.setId(String.valueOf(object.get("id")));
                                    member.setUserId(String.valueOf(object.get("user_id")));

                                    member.setFirstName(isNull(object,"first_name"));
                                    member.setMiddleName(isNull(object,"middle_name"));
                                    member.setLastName(isNull(object,"last_name"));
                                    member.setGender(isNull(object,"gender"));
                                    member.setStatus(isNull(object,"status"));
                                    member.setMaritalStatus(isNull(object,"marital_status"));
                                    member.setDob(isNull(object,"dob","0000-00-00"));
                                    member.setHomePhone(isNull(object,"home_phone"));
                                    member.setMobilePhone(isNull(object,"mobile_phone"));
                                    member.setWorkPhone(isNull(object,"work_phone"));
                                    member.setEmail(isNull(object,"email"));
                                    member.setAddress(isNull(object,"address"));
                                    member.setNotes(isNull(object,"notes"));
                                    member.setRollNo(isNull(object,"No"));
                                    member.setCreateAt(isNull(object,"created_at"));
                                    member.setUpdateAt(isNull(object,"updated_at"));
                                    member.setFingerPrint(isNull(object,"fingerprint"));


                                    //remainber to do changes...
                                    member.setPhotoURL(isNull(object,"photo",""));

                                    memberList.add(member);
                                    dataBaseHelper.insertMemberData(member);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //"http://52.172.221.235:8983/api/get_all_events"
                            eventDetailsGovNet(PreferenceUtils.getUrlGetAllEvents(SplashActivity.this),
                                    PreferenceUtils.getToken(SplashActivity.this));

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln");
                        }
                    })
                    .startUpload();//Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void eventDetailsGovNet(String URL,String token){
        try {
            final String uploadId = UUID.randomUUID().toString();

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("token",token)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                            dataBaseHelper.deleteAllEvents();
                            JSONParser parser = new JSONParser();
                            JSONArray jsonArray = null;
                            try{
                                jsonArray = (JSONArray)parser.parse(serverResponse.getBodyAsString().toString());

                                for(int i=0;i<jsonArray.size();i++){

                                    EventPOJO event = new EventPOJO();
                                    JSONObject object = (JSONObject)jsonArray.get(i);
                                    event.setId(String.valueOf(object.get("id")));
                                    event.setBranchId(isNull(object,"branch_id"));
                                    event.setUserId(isNull(object,"user_id"));
                                    event.setParentId(isNull(object,"parent_id"));
                                    event.setEventLocationId(isNull(object,"event_location_id"));
                                    event.setEventCalenderId(isNull(object,"event_calendar_id"));
                                    event.setName(isNull(object,"name"));
                                    event.setCost(isNull(object,"cost"));
                                    event.setAllDay(isNull(object,"all_day"));
                                    event.setStartDate(isNull(object,"start_date"));
                                    event.setStart_time(isNull(object,"start_time"));
                                    event.setEnd_date(isNull(object,"end_date"));
                                    event.setEnd_time(isNull(object,"end_time"));
                                    event.setRecurring(isNull(object,"recurring"));
                                    event.setRecurFrequency(isNull(object,"recur_frequency"));
                                    event.setRecurStartDate(isNull(object,"recur_start_date"));
                                    event.setRecurEndDate(isNull(object,"recur_end_date"));
                                    event.setRecurNextDate(isNull(object,"recur_next_date"));
                                    event.setRecurType(isNull(object,"recur_type"));
                                    event.setCheckInType(isNull(object,"checkin_type"));
                                    event.setTags(isNull(object,"tags"));
                                    event.setIncludeCheckOut(isNull(object,"include_checkout"));
                                    event.setFamilyCheckIn(isNull(object,"family_checkin"));
                                    event.setFeatured_image(isNull(object,"featured_image"));
                                    event.setGallery(isNull(object,"gallery"));
                                    event.setFiles(isNull(object,"files"));
                                    event.setYear(isNull(object,"year"));
                                    event.setMonth(isNull(object,"month"));
                                    event.setNotes(isNull(object,"notes"));
                                    event.setCreatedAt(isNull(object,"created_at"));
                                    event.setUpdatedAt(isNull(object,"updated_at"));
                                    eventList.add(event);
                                    dataBaseHelper.insertEventData(event);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(SplashActivity.this,HomeActivity.class);
                            intent.putExtra("member_list",memberList);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln");
                        }
                    })
                    .startUpload();//Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    public String isNull(JSONObject object, String parma){
        return object.get(parma)!=null?object.get(parma).toString():"";
    }
    public String isNull(JSONObject object, String parma,String dafualtStr){
        return object.get(parma)!=null?object.get(parma).toString():dafualtStr;
    }
}
