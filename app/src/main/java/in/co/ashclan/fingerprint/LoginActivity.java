package in.co.ashclan.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.utils.L;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.ContributionsPOJO;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.FamilyPOJO;
import in.co.ashclan.model.GroupsPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.model.PledgesPOJO;
import in.co.ashclan.utils.Constants;
import in.co.ashclan.utils.PreferenceUtils;
import in.co.ashclan.utils.Utils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

//import main.utils.PreferenceUtils;

public class LoginActivity extends AppCompatActivity implements OnItemSelectedListener{

    Context mContext;
    MaterialSpinner msServer;
    TextView textViewVersions;
    EditText editTextAdmin,editTextAdminPassword;
    Button buttonLogin;
    ContentLoadingProgressBar progressBar;
    ArrayList<MemberPOJO> memberList = new ArrayList<MemberPOJO>();
    ArrayList<EventPOJO> eventList = new ArrayList<EventPOJO>();
    int iSet;
    DataBaseHelper dataBaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        Constants.setURL(this,PreferenceUtils.getSelectServer(this));

        dataBaseHelper = new DataBaseHelper(this);
        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600)
        {
            setContentView(R.layout.activity_login);
        }
        else
        {
            setContentView(R.layout.activity_login);
        }
        init();
        login();
    }
    public void init(){
        textViewVersions = (TextView)findViewById(R.id.text_login_versions);
        editTextAdmin = (EditText)findViewById(R.id.admin);
        editTextAdminPassword = (EditText)findViewById(R.id.admin_password);
        buttonLogin = (Button)findViewById(R.id.button_login);
        progressBar = (ContentLoadingProgressBar)findViewById(R.id.progress_bar_login);
        msServer = (MaterialSpinner)findViewById(R.id.spinner_sever);
        editTextAdmin.setText(PreferenceUtils.getAdminName(LoginActivity.this));
        editTextAdminPassword.setText(PreferenceUtils.getAdminPassword(LoginActivity.this));
        msServer.setSelection(PreferenceUtils.getSelectServer(this));
        msServer.setOnItemSelectedListener(this);
    }
    public void login(){
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean cancel = false;
                View focusView = null;

                if(TextUtils.isEmpty(editTextAdmin.getText())){
                    editTextAdmin.setError("This field is required");
                    focusView=editTextAdmin;
                    cancel=true;
                }

                if(TextUtils.isEmpty(editTextAdminPassword.getText())){
                    editTextAdminPassword.setError("This field is required");
                    focusView=editTextAdminPassword;
                    cancel=true;
                }
                if(msServer.getSelectedItemPosition()==0){
                    msServer.setError("Please Select Server");
                    focusView=msServer;
                    cancel=true;
                }


                if(cancel){
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.

                    focusView.requestFocus();
                }else{
                    //"http://52.172.221.235:8983/api/login"
                    progressBar.setVisibility(View.VISIBLE);
                    buttonLogin.setEnabled(false);
                    //   getAccessTokenGovNet(PreferenceUtils.getUrlLogin(LoginActivity.this),editTextAdmin.getText().toString(),editTextAdminPassword.getText().toString());

                    GetAccessTokenTask aTask=new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                            editTextAdmin.getText().toString(),editTextAdminPassword.getText().toString());
                    aTask.execute();
                }


            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }
    //email=test@gmail.com
    //password=123456
    public void getAccessTokenVolley(String URL, final String mEmail, final String mPassword){
        Log.e("--->",URL+" "+mEmail+" "+mPassword);

        Toast.makeText(LoginActivity.this,URL,Toast.LENGTH_LONG).show();
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
                        Toast.makeText(LoginActivity.this,token,Toast.LENGTH_SHORT).show();
                        if(token.equalsIgnoreCase("wrong email or password.")){
                            View focusView1,focusView2;
                            progressBar.setVisibility(View.INVISIBLE);
                            editTextAdmin.setError("wrong email");
                            focusView1=editTextAdmin;
                            editTextAdminPassword.setError("wrong password");
                            focusView2=editTextAdminPassword;
                            focusView1.requestFocus();
                            focusView2.requestFocus();
                            buttonLogin.setEnabled(true);
                        }else{
                            PreferenceUtils.setToken(LoginActivity.this,token);
                            PreferenceUtils.setAdminName(LoginActivity.this,mEmail);
                            PreferenceUtils.setAdminPassword(LoginActivity.this,mPassword);
                            PreferenceUtils.setSignIn(LoginActivity.this,true);
                            //               PreferenceUtils.setSelectServer(LoginActivity.this,iSet);

                            progressBar.setVisibility(View.INVISIBLE);
                            //"http://52.172.221.235:8983/api/get_all_members"
                            memberDetailsVolley(PreferenceUtils.getUrlGetAllMembers(LoginActivity.this),PreferenceUtils.getToken(LoginActivity.this));

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   progressBar.setVisibility(View.INVISIBLE);
                // buttonLogin.setEnabled(true);
            }
        });


        smr.addStringParam("email",mEmail)
                .addStringParam("password",mPassword);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);

    }
    public void memberDetailsVolley(String URL, String token){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //    Toast.makeText(LoginActivity.this,response,Toast.LENGTH_SHORT).show();
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
                                member.setDob(isNull(object,"dob"));
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
///
//                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
//                        intent.putExtra("member_list",memberList);
//                        startActivity(intent);
//                        finish();
                        eventDetailsVolley(PreferenceUtils.getUrlGetAllEvents(mContext),
                                PreferenceUtils.getToken(mContext));
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // progressBar.setVisibility(View.INVISIBLE);
                // buttonLogin.setEnabled(true);

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
                                /*
                                "latitude": "",
                                "longitude": ""
                                */
                                eventList.add(event);
                                dataBaseHelper.insertEventData(event);

                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                        intent.putExtra("member_list",memberList);
                        startActivity(intent);
                        finish();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                buttonLogin.setEnabled(true);
            }
        });

        smr.addStringParam("token",token);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);
    }
    public void getAccessTokenGovNet(String URL,final String mEmail,final String mPassword){
        Log.e("--->",URL);
        try {
            final String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("email",mEmail)
                    .addParameter("password",mPassword)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            //                Log.e("---->",serverResponse.getBodyAsString().toString());
                            progressBar.setVisibility(View.INVISIBLE);
                            buttonLogin.setEnabled(true);
                            Toast.makeText(mContext,"offline",Toast.LENGTH_LONG).show();
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
                            //    Toast.makeText(LoginActivity.this,token,Toast.LENGTH_SHORT).show();
                            if(token.equalsIgnoreCase("wrong email or password.")){
                                View focusView1,focusView2;
                                progressBar.setVisibility(View.INVISIBLE);
                                editTextAdmin.setError("wrong email");
                                focusView1=editTextAdmin;
                                editTextAdminPassword.setError("wrong password");
                                focusView2=editTextAdminPassword;
                                focusView1.requestFocus();
                                focusView2.requestFocus();
                                buttonLogin.setEnabled(true);
                            }else{
                                PreferenceUtils.setToken(LoginActivity.this,token);
                                PreferenceUtils.setAdminName(LoginActivity.this,mEmail);
                                PreferenceUtils.setAdminPassword(LoginActivity.this,mPassword);
                                PreferenceUtils.setSignIn(LoginActivity.this,true);
                                PreferenceUtils.setSelectServer(LoginActivity.this,iSet);

                                progressBar.setVisibility(View.INVISIBLE);
                                //"http://52.172.221.235:8983/api/get_all_members"
                                memberDetailsGovNet(PreferenceUtils.getUrlGetAllMembers(mContext), PreferenceUtils.getToken(mContext));
                            }
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln "+uploadInfo.toString());
                            progressBar.setVisibility(View.INVISIBLE);
                            buttonLogin.setEnabled(true);

                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void memberDetailsGovNet(String URL, String token){
        try {
            Log.e("--->",URL);
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
                            progressBar.setVisibility(View.INVISIBLE);
                            buttonLogin.setEnabled(true);

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
                            eventDetailsGovNet(PreferenceUtils.getUrlGetAllEvents(mContext),PreferenceUtils.getToken(mContext));

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln");
                            progressBar.setVisibility(View.INVISIBLE);
                            buttonLogin.setEnabled(true);

                        }
                    })
                    .startUpload();//Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void eventDetailsGovNet(String URL,String token){
        Log.e("--->",URL);
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
                            progressBar.setVisibility(View.INVISIBLE);
                            buttonLogin.setEnabled(true);

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

                            Intent intent = new Intent(mContext,HomeActivity.class);
                            intent.putExtra("member_list",memberList);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln");
                            progressBar.setVisibility(View.INVISIBLE);
                            buttonLogin.setEnabled(true);

                        }
                    })
                    .startUpload();//Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //************************************************
    public class GetAccessTokenTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String email,password;

        GetAccessTokenTask(Context mContext,String URL,String email,String password) {
            this.mContext = mContext;
            this.email=email;
            this.password=password;
            this.URL = URL;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("--->","URL = "+URL+" email = "+email+"password = "+password);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                HashMap<String, String> postData = new HashMap<>();
                postData.put("email", email);
                postData.put("password", password);
                String url = "https://bwc.pentecostchurch.org/api/login";
                String urls = "http://52.172.221.235:8983/api/login";
                String json_output = performPostCall(URL, postData);

                return json_output;
            }catch (Exception e){

            }
            return "";
        }

        @Override
        protected void onPostExecute(String output) {
            String token=null;
            try {
                Log.e("--->", output);
                JSONParser parser = new JSONParser();

                JSONObject object = (JSONObject) parser.parse(output.toString());
                token = (String) object.get("result");
                Log.e("--->", token);

                if (token.equalsIgnoreCase("wrong email or password.")) {
                    View focusView1, focusView2;
                    progressBar.setVisibility(View.INVISIBLE);
                    editTextAdmin.setError("wrong email");
                    focusView1 = editTextAdmin;
                    editTextAdminPassword.setError("wrong password");
                    focusView2 = editTextAdminPassword;
                    focusView1.requestFocus();
                    focusView2.requestFocus();
                    buttonLogin.setEnabled(true);

                } else {
                    PreferenceUtils.setToken(LoginActivity.this, token);
                    PreferenceUtils.setAdminName(LoginActivity.this, email);
                    PreferenceUtils.setAdminPassword(LoginActivity.this, password);
                    PreferenceUtils.setSignIn(LoginActivity.this, true);
                    PreferenceUtils.setSelectServer(LoginActivity.this, iSet);

                    //String urls="https://bwc.pentecostchurch.org/api/get_all_members";
                    // String url="http://52.172.221.235:8983/api/get_all_members";

                    /*GetAllMemberTask mTask = new GetAllMemberTask(mContext, PreferenceUtils.getUrlGetAllMembers(mContext), token);
                    mTask.execute();
*/
                    GetAllDetailsTask detailsTask = new GetAllDetailsTask(mContext,PreferenceUtils.getUrlGetAllMembersDetails(mContext),token);
                    detailsTask.execute();
                }
            }catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);
            buttonLogin.setEnabled(true);
        }
    }
    //CheckIn
    public class CheckInTask extends AsyncTask<String,String,String>{

        private Context mContext;
        private String URL;
        private String member_id,event_id;
        private String token;
        CheckInTask(Context mContext,String URL,String member_id,String event_id,String token){
            this.mContext=mContext;
            this.member_id=member_id;
            this.event_id=event_id;
            this.URL=URL;
            this.token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e("--->","URL = "+URL+"member_id = "+member_id+" event_id = "+event_id+" token = "+token);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }


        @Override
        protected String doInBackground(String... strings) {
            try{
                HashMap<String, String> postData = new HashMap<>();
                postData.put("token",token);
                postData.put("member_id",member_id);
                postData.put("event_id",event_id);
                String json_output = performPostCall(URL,postData);
                return json_output;
            }catch (Exception ex){
                Log.e("--->",ex.toString());
            }

            return "";
        }

        @Override
        protected void onPostExecute(String output) {
            super.onPostExecute(output);
            try{
                Log.e("---->",URL);
                Log.e("---->",output.toString());
                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject)parser.parse(output.toString());

                JSONObject jsonResultObject=(JSONObject)jsonObject.get("result");

                JSONArray jsonAttendanceArray=(JSONArray)jsonObject.get("attendance");

                for(int i=0;i<jsonAttendanceArray.size();i++){
                   /*
                    "event_id": "4",
                            "user_id": 11,
                            "anonymous": 0,
                            "member_id": "353",
                            "date": "2018-06-28",
                            "updated_at": "2018-06-28 02:52:20",
                            "created_at": "2018-06-28 02:52:20",
                            "id": 12
                            */

                   EventAttendancePOJO eventAttendance=new EventAttendancePOJO();
                    JSONObject object = (JSONObject)jsonAttendanceArray.get(i);
                    eventAttendance.setEventId(isNull(object,"event_id"));
                    eventAttendance.setUserId(isNull(object,"user_id"));
                    eventAttendance.setAnonymous(isNull(object,"anonymous"));
                    eventAttendance.setMemberId(isNull(object,"member_id"));
                    eventAttendance.setDate(isNull(object,"date"));
                    eventAttendance.setCreatedAt(isNull(object,"created_at"));
                    eventAttendance.setUpdatedAt(isNull(object,"updated_at"));
                }
            }catch (Exception ex){
                Log.e("--->",ex.toString());

            }
        }
    }

    //GET ALL MEMBERS
    public class GetAllMemberTask extends AsyncTask<String, String, String> {
        private Context mContext;
        private String URL;
        private String token;
        GetAllMemberTask(Context mContext,String URL,String token) {
            this.mContext = mContext;
            this.URL=URL;
            this.token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("token", token);
            String json_output = performPostCall(URL, postData);
            return json_output;
        }

        @Override
        protected void onPostExecute(String output) {
            // mAuthTask = null;
            //showProgress(false);
            try {
                Log.e("--->", output);

                dataBaseHelper.deleteAllMembers();

                JSONParser parser = new JSONParser();
                JSONArray jsonArray = (JSONArray)parser.parse(output);

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
                    Log.e("--->",member.toString());
                    dataBaseHelper.insertMemberData(member);
                }


                //String urls="https://bwc.pentecostchurch.org/api/get_all_events";
                //String url="http://52.172.221.235:8983/api/get_all_events";
//            GetAllEventsTask eTask = new GetAllEventsTask(mContext,PreferenceUtils.getUrlGetAllEvents(mContext),token);
//            eTask.execute();

            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);
            buttonLogin.setEnabled(true);
        }
    }
    //GET ALL EVENTS
    public class GetAllEventsTask extends AsyncTask<String, String, String> {
        private Context mContext;
        private String URL;
        private String token;
        GetAllEventsTask(Context mContext,String URL,String token) {
            this.mContext = mContext;
            this.URL=URL;
            this.token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("token", token);
            String json_output = performPostCall(URL, postData);
            return json_output;
        }

        @Override
        protected void onPostExecute(String output) {
            try {
                Log.e("--->",URL);
                //    Log.e("--->THIS!T", output);
                Log.e("--->THIS1", output);
                dataBaseHelper.deleteAllEvents();
                JSONParser parser = new JSONParser();
                JSONArray jsonArray =(JSONArray)parser.parse(output.toString());

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
                    Log.e("--->",event.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertEventData(event);
                }

               /* //Contribution
                GetAllContributionTask cTask = new GetAllContributionTask(mContext,PreferenceUtils.getUrlGetContributions(mContext),token);
                cTask.execute();
*/

                Intent intent = new Intent(mContext,HomeActivity.class);
//            intent.putExtra("member_list",memberList);
                startActivity(intent);
                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                finish();
            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
        }
    }
    //GET ALL CONTRIBUTIONS
    public class GetAllContributionTask extends AsyncTask<String, String, String> {
        private Context mContext;
        private String URL;
        private String token;
        GetAllContributionTask(Context mContext, String URL, String token) {
            this.mContext = mContext;
            this.URL=URL;
            this.token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("token", token);
            String json_output = performPostCall(URL, postData);
            return json_output;
        }

        @Override
        protected void onPostExecute(String output) {
            try {
                Log.e("contribution",URL);
                //    Log.e("--->THIS!T", output);
                Log.e("--->contribution", output);

                dataBaseHelper.deleteAllContribution();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject)parser.parse(output.toString());
                JSONArray jsonArray =(JSONArray) jsonObject.get("contributions");//parser.parse(output.toString());

                for(int i=0;i<jsonArray.size();i++){

                    ContributionsPOJO contribution = new ContributionsPOJO();
                    JSONObject object = (JSONObject)jsonArray.get(i);

                  /*  String id,branchId,userId,memberId,familyId,fundId;
                    String memberType,contributionBatchId,paymentMethodId,date;
                    String files,notes,transRef,amount,year,month;
                    String createdAt,updatedAt;
*/
                    contribution.setId(String.valueOf(object.get("id")));
                    contribution.setBranchId(isNull(object,"branch_id"));
                    contribution.setMeberType(isNull(object,"member_type"));
                    contribution.setUserId(isNull(object,"user_id"));
                    contribution.setMemberId(isNull(object,"member_id"));
                    contribution.setFamilyId(isNull(object,"family_id"));
                    contribution.setFundId(isNull(object,"funds_id"));
                    contribution.setContributionBatchId(isNull(object,"contribution_batch_id"));
                    contribution.setPaymentMethodId(isNull(object,"payment_method_id"));
                    contribution.setDate(isNull(object,"date"));
                    contribution.setNotes(isNull(object,"notes"));
                    contribution.setFiles(isNull(object,"files"));
                    contribution.setTransRef(isNull(object,"trans_ref"));
                    contribution.setAmount(isNull(object,"amount"));
                    contribution.setYear(isNull(object,"year"));
                    contribution.setMonth(isNull(object,"month"));
                    contribution.setCreatedAt(isNull(object,"created_at"));
                    contribution.setUpdatedAt(isNull(object,"updated_at"));

                    Log.e("--->c",contribution.toString());

                    //   eventList.add(event);
                    dataBaseHelper.insertOfflineContributionData(contribution);
                }

//                //pledge
//                GetAllPledgeTask pledgeTask = new GetAllPledgeTask(mContext,PreferenceUtils.getUrlGetPledges(mContext),token);
//                pledgeTask.execute();

                Intent intent = new Intent(mContext,HomeActivity.class);
//            intent.putExtra("member_list",memberList);
                startActivity(intent);
                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                finish();
            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
        }
    }
    //GET ALL PLEDGE
    public class GetAllPledgeTask extends AsyncTask<String, String, String> {
        private Context mContext;
        private String URL;
        private String token;
        GetAllPledgeTask(Context mContext, String URL, String token) {
            this.mContext = mContext;
            this.URL=URL;
            this.token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("token", token);
            String json_output = performPostCall(URL, postData);
            return json_output;
        }

        @Override
        protected void onPostExecute(String output) {
            try {
                Log.e("pledge",URL);
                //    Log.e("--->THIS!T", output);
                Log.e("--->pledge", output);

                dataBaseHelper.deleteAllPledges();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject)parser.parse(output.toString());
                JSONArray jsonArray =(JSONArray) jsonObject.get("pledges");

                for(int i=0;i<jsonArray.size();i++){

                    PledgesPOJO pledges = new PledgesPOJO();
                    JSONObject object = (JSONObject)jsonArray.get(i);

                  /*  String id,branchId,userId,memberId,familyId,fundId;
                    String memberType,contributionBatchId,paymentMethodId,date;
                    String files,notes,transRef,amount,year,month;
                    String createdAt,updatedAt;
*/
                    pledges.setId(String.valueOf(object.get("id")));
                    pledges.setBranchId(isNull(object,"branch_id"));
                    pledges.setUserId(isNull(object,"user_id"));
                    pledges.setMemberId(isNull(object,"member_id"));
                    pledges.setFamilyId(isNull(object,"family_id"));
                    pledges.setPledgeType(isNull(object,"pledge_type"));
                    pledges.setCampaignId(isNull(object,"campaign_id"));
                    pledges.setTotal_amount(isNull(object,"amount"));
                    pledges.setRecurring(isNull(object,"recurring"));
                    pledges.setRecur_frequency(isNull(object,"recur_frequency"));
                    pledges.setRecur_type(isNull(object,"recur_type"));
                    pledges.setRecur_start_date(isNull(object,"recur_start_date"));
                    pledges.setRecur_end_date(isNull(object,"recur_end_date"));
                    pledges.setRecur_next_date(isNull(object,"recur_next_date"));
                    pledges.setTotal_amount(isNull(object,"total_amount"));
                    pledges.setTimes_number(isNull(object,"times_number"));
                    pledges.setYear(isNull(object,"year"));
                    pledges.setMonth(isNull(object,"month"));
                    pledges.setDate(isNull(object,"date"));
                    pledges.setNotes(isNull(object,"notes"));
                    pledges.setCreatedAt(isNull(object,"creacted_at"));
                    pledges.setUpdatedAt(isNull(object,"updated_at"));
                    Log.e("--->",pledges.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertPledgeData(pledges);
                }

//                //Family
//                GetAllFamilyTask familyTask = new GetAllFamilyTask(mContext,PreferenceUtils.getUrlGetFamily(mContext),token);
//                familyTask.execute();

                Intent intent = new Intent(mContext,HomeActivity.class);
//            intent.putExtra("member_list",memberList);
                startActivity(intent);
                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                finish();
            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
        }
    }
    //GET ALL FAMILY
    public class GetAllFamilyTask extends AsyncTask<String, String, String> {
        private Context mContext;
        private String URL;
        private String token;
        GetAllFamilyTask(Context mContext, String URL, String token) {
            this.mContext = mContext;
            this.URL=URL;
            this.token=token;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("token", token);
            String json_output = performPostCall(URL, postData);
            return json_output;
        }
        @Override
        protected void onPostExecute(String output) {
            try {
                Log.e("family",URL);
                //    Log.e("--->THIS!T", output);
                Log.e("--->family", output);

                dataBaseHelper.deleteAllFamily();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject)parser.parse(output.toString());
                JSONArray jsonArray =(JSONArray) jsonObject.get("family");

                for(int i=0;i<jsonArray.size();i++){

                    FamilyPOJO family = new FamilyPOJO();
                    JSONObject object = (JSONObject)jsonArray.get(i);

                  /*  String id,branchId,userId,memberId,familyId,fundId;
                    String memberType,contributionBatchId,paymentMethodId,date;
                    String files,notes,transRef,amount,year,month;
                    String createdAt,updatedAt;
*/
                    family.setId(String.valueOf(object.get("id")));
                    family.setBranchId(isNull(object,"branch_id"));
                    family.setUserId(isNull(object,"user_id"));
                    family.setMemberId(isNull(object,"member_id"));
                    family.setName(isNull(object,"name"));
                    family.setNotes(isNull(object,"notes"));
                    family.setPicture(isNull(object,"picture"));
                    family.setCreatedAt(isNull(object,"created_at"));
                    family.setUpdatedAt(isNull(object,"updated_at"));
                    Log.e("--->",family.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertFAMILYData(family);
                }

//                //Groups
//                GetAllGroupsTask groupsTask = new GetAllGroupsTask(mContext,PreferenceUtils.getUrlGetGroup(mContext),token);
//                groupsTask.execute();

                Intent intent = new Intent(mContext,HomeActivity.class);
//            intent.putExtra("member_list",memberList);
                startActivity(intent);
                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                finish();
            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
        }
    }
    //GET ALL GROUPS
    public class GetAllGroupsTask extends AsyncTask<String, String, String> {
        private Context mContext;
        private String URL;
        private String token;
        GetAllGroupsTask(Context mContext, String URL, String token) {
            this.mContext = mContext;
            this.URL=URL;
            this.token=token;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("token", token);
            String json_output = performPostCall(URL, postData);
            return json_output;
        }

        @Override
        protected void onPostExecute(String output) {
            try {
                Log.e("groups",URL);
                //    Log.e("--->THIS!T", output);
                Log.e("--->groups", output);

                dataBaseHelper.deleteAllGroups();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject)parser.parse(output.toString());
                JSONArray jsonArray =(JSONArray) jsonObject.get("groups");

                for(int i=0;i<jsonArray.size();i++){

                    GroupsPOJO groups = new GroupsPOJO();
                    JSONObject object = (JSONObject)jsonArray.get(i);

                  /*  String id,branchId,userId,memberId,familyId,fundId;
                    String memberType,contributionBatchId,paymentMethodId,date;
                    String files,notes,transRef,amount,year,month;
                    String createdAt,updatedAt;
*/
                    groups.setId(String.valueOf(object.get("id")));
                    groups.setMemberId(isNull(object,"member_id"));
                    groups.setUserId(isNull(object,"user_id"));
                    groups.setTagId(isNull(object,"tag_id"));
                    Log.e("--->",groups.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertGroupsData(groups);
                }



                Intent intent = new Intent(mContext,HomeActivity.class);
//            intent.putExtra("member_list",memberList);
                startActivity(intent);
                progressBar.setVisibility(View.VISIBLE);
                buttonLogin.setEnabled(false);
                finish();
            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
        }
    }

    //*********************************************************************
    //GET ALL MEMBER DETAILS
    public class GetAllDetailsTask extends AsyncTask<String, String, String> {
        private Context mContext;
        private String URL;
        private String token;
        GetAllDetailsTask(Context mContext, String URL, String token) {
            this.mContext = mContext;
            this.URL=URL;
            this.token=token;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("token", token);
            String json_output = performPostCall(URL, postData);
            return json_output;
        }
        @Override
        protected void onPostExecute(String output) {
            try {
                Log.e("D--->",output);
                //data delete pervious table.....
                dataBaseHelper.deleteAllMembers();

                JSONParser parser = new JSONParser();
                JSONObject jsonObject = (JSONObject)parser.parse(output);
                JSONArray jsonMemberArray = (JSONArray)jsonObject.get("memberDetails");
                for(int i=0;i<jsonMemberArray.size();i++){

                    MemberPOJO member = new MemberPOJO();
                    JSONObject object = (JSONObject)jsonMemberArray.get(i);
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
                    member.setFingerPrint1(isNull(object,"fingerprint2"));

                    //remainber to do changes...
                    member.setPhotoURL(isNull(object,"photo",""));
                    Log.e("D--->",member.toString());
                    dataBaseHelper.insertMemberData(member);

                }

                Log.i("--->Details-->",dataBaseHelper.getAllMembers().toString());
                dataBaseHelper.deleteAllEvents();
                JSONArray jsonEventArray = (JSONArray)jsonObject.get("eventDetails");

                for(int i=0;i<jsonEventArray.size();i++){

                    EventPOJO event = new EventPOJO();
                    JSONObject object = (JSONObject)jsonEventArray.get(i);
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
                    Log.e("D--->",event.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertEventData(event);
                }
                dataBaseHelper.deleteAllContribution();

                JSONArray jsonContributionsArray = (JSONArray)jsonObject.get("contributions");
                for(int i=0;i<jsonContributionsArray.size();i++){

                    ContributionsPOJO contribution = new ContributionsPOJO();
                    JSONObject object = (JSONObject)jsonContributionsArray.get(i);

                    contribution.setId(String.valueOf(object.get("id")));
                    contribution.setBranchId(isNull(object,"branch_id"));
                    contribution.setMeberType(isNull(object,"member_type"));
                    contribution.setUserId(isNull(object,"user_id"));
                    contribution.setMemberId(isNull(object,"member_id"));
                    contribution.setFamilyId(isNull(object,"family_id"));
                    contribution.setFundId(isNull(object,"funds_id"));
                    contribution.setContributionBatchId(isNull(object,"contribution_batch_id"));
                    contribution.setPaymentMethodId(isNull(object,"payment_method_id"));
                    contribution.setDate(isNull(object,"date"));
                    contribution.setNotes(isNull(object,"notes"));
                    contribution.setFiles(isNull(object,"files"));
                    contribution.setTransRef(isNull(object,"trans_ref"));
                    contribution.setAmount(isNull(object,"amount"));
                    contribution.setYear(isNull(object,"year"));
                    contribution.setMonth(isNull(object,"month"));
                    contribution.setCreatedAt(isNull(object,"created_at"));
                    contribution.setUpdatedAt(isNull(object,"updated_at"));


                    JSONObject paymentMethod= (JSONObject)object.get("payment_method");
                    contribution.setPaymentMethod(isNull(paymentMethod,"name"));

                    JSONObject batch = (JSONObject)object.get("batch");
                    contribution.setBatchName(isNull(batch,"name"));
                    contribution.setBatchNote(isNull(batch,"notes"));
                    contribution.setBatchdate(isNull(batch,"date"));
                    contribution.setBatchCurrent(isNull(batch,"current"));
                    contribution.setBatchStatus(isNull(batch,"status"));

                    Log.e("D--->",contribution.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertOfflineContributionData(contribution);
                }

                dataBaseHelper.deleteAllFamily();
                JSONArray jsonFamilyArray = (JSONArray)jsonObject.get("family");

                for(int i=0;i<jsonFamilyArray.size();i++){

                    FamilyPOJO family = new FamilyPOJO();
                    JSONObject object = (JSONObject)jsonFamilyArray.get(i);

                    family.setId(String.valueOf(object.get("id")));
                    family.setBranchId(isNull(object,"branch_id"));
                    family.setUserId(isNull(object,"user_id"));
                    family.setMemberId(isNull(object,"member_id"));
                    family.setName(isNull(object,"name"));
                    family.setNotes(isNull(object,"notes"));
                    family.setPicture(isNull(object,"picture"));
                    family.setCreatedAt(isNull(object,"created_at"));
                    family.setUpdatedAt(isNull(object,"updated_at"));
                    Log.e("D--->",family.toString());

                    //   eventList.add(event);
                    dataBaseHelper.insertFAMILYData(family);
                }
                dataBaseHelper.deleteAllEventAttendance();
                JSONArray jsonEventAttendanceArray = (JSONArray)jsonObject.get("eventAttendence");
                for(int i=0;i<jsonEventAttendanceArray.size();i++){

                    EventAttendancePOJO eventAttendancePOJO = new EventAttendancePOJO();
                    JSONObject object = (JSONObject)jsonEventAttendanceArray.get(i);

                    eventAttendancePOJO.setEventId(isNull(object,"event_id"));
                    eventAttendancePOJO.setUserId(isNull(object,"user_id"));
                    eventAttendancePOJO.setMemberId(isNull(object,"member_id"));
                    eventAttendancePOJO.setAnonymous(isNull(object,"anonymous"));
                    eventAttendancePOJO.setDate(isNull(object,"date"));
                    eventAttendancePOJO.setCreatedAt(isNull(object,"created_at"));
                    eventAttendancePOJO.setUpdatedAt(isNull(object,"updated_at"));

                    Log.e("D--->",eventAttendancePOJO.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertEventAttendaceData(eventAttendancePOJO);
                }

                dataBaseHelper.deleteAllPledges();
                JSONArray jsonPledgeArray = (JSONArray)jsonObject.get("pledges");
                for(int i=0;i<jsonPledgeArray.size();i++){

                    PledgesPOJO pledges = new PledgesPOJO();
                    JSONObject object = (JSONObject)jsonPledgeArray.get(i);

                    pledges.setId(String.valueOf(object.get("id")));
                    pledges.setBranchId(isNull(object,"branch_id"));
                    pledges.setUserId(isNull(object,"user_id"));
                    pledges.setMemberId(isNull(object,"member_id"));
                    pledges.setFamilyId(isNull(object,"family_id"));
                    pledges.setPledgeType(isNull(object,"pledge_type"));
                    pledges.setCampaignId(isNull(object,"campaign_id"));
                    pledges.setTotal_amount(isNull(object,"amount"));
                    pledges.setRecurring(isNull(object,"recurring"));
                    pledges.setRecur_frequency(isNull(object,"recur_frequency"));
                    pledges.setRecur_type(isNull(object,"recur_type"));
                    pledges.setRecur_start_date(isNull(object,"recur_start_date"));
                    pledges.setRecur_end_date(isNull(object,"recur_end_date"));
                    pledges.setRecur_next_date(isNull(object,"recur_next_date"));
                    pledges.setTotal_amount(isNull(object,"total_amount"));
                    pledges.setTimes_number(isNull(object,"times_number"));
                    pledges.setYear(isNull(object,"year"));
                    pledges.setMonth(isNull(object,"month"));
                    pledges.setDate(isNull(object,"date"));
                    pledges.setNotes(isNull(object,"notes"));
                    pledges.setCreatedAt(isNull(object,"creacted_at"));
                    pledges.setUpdatedAt(isNull(object,"updated_at"));

                    JSONObject Campaign = (JSONObject)object.get("campaign");
                    pledges.setCampaign_name(isNull(Campaign,"name"));

                    Log.e("D--->",pledges.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertPledgeData(pledges);
                }
                //Groups
                GetAllGroupsTask groupsTask = new GetAllGroupsTask(mContext,PreferenceUtils.getUrlGetGroup(mContext),token);
                groupsTask.execute();

            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                buttonLogin.setEnabled(true);
                e.printStackTrace();
            }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setEnabled(false);
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        PreferenceUtils.setSelectServer(mContext,i);
        Constants.setURL(LoginActivity.this,i);
        iSet=i;
        if(i==0){
            Toast.makeText(LoginActivity.this,"0",Toast.LENGTH_LONG).show();
        }
        if (i==1){
            Toast.makeText(LoginActivity.this,"1",Toast.LENGTH_LONG).show();
        }
        if (i==2){
            Toast.makeText(LoginActivity.this,"2",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
