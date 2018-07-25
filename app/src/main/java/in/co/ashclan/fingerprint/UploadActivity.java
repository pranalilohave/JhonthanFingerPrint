package in.co.ashclan.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import android_serialport_api.SerialPortManager;
import android_serialport_api.SerialPortManagerA5;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.adpater.UploadAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.database.DataBaseHelperOffline;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.Constants;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class UploadActivity extends AppCompatActivity
       implements NavigationView.OnNavigationItemSelectedListener{

    FloatingActionButton fab;
    ListView listView;
    Context mContext;
    DataBaseHelper dataBaseHelper;
    DataBaseHelperOffline dataBaseHelperOffline;
    UploadAdapter uploadAdapter;
    MemberAdapter memberAdapter;
    ArrayList<MemberPOJO> list = new ArrayList<MemberPOJO>();
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mContext = UploadActivity.this;
        listView = (ListView)findViewById(R.id.list);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);

        dataBaseHelper =new DataBaseHelper(mContext);
        dataBaseHelperOffline=new DataBaseHelperOffline(mContext);

        list = (ArrayList<MemberPOJO>) dataBaseHelperOffline.getAllOfflineMembers();

        memberAdapter = new MemberAdapter(mContext,list,"ic_person.png");
        listView.setAdapter(memberAdapter);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext,MemberRegisterActivity.class);
                intent.putExtra("type","register");
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(2).setChecked(true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MemberPOJO member = new MemberPOJO();
                member = list.get(i);
                progressBar.setVisibility(View.VISIBLE);
             //   getAccessTokenGovNet(PreferenceUtils.getUrlLogin(mContext),PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),member);
                GetAccessTokenTask aTask = new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                        PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),member);
                aTask.execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.just_logout, menu);
        return true;
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            PreferenceUtils.setSignIn(mContext,false);
            Intent intent = new Intent(mContext, LoginActivity.class);
          //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_member) {
            Intent intent = new Intent(mContext,MemberListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(mContext,HomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_recording) {
            Intent intent = new Intent(mContext,RecordingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_setting) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public class GetAccessTokenTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String email,password;
        private MemberPOJO memberPOJO;
        GetAccessTokenTask(Context mContext,String URL,String email,String password,MemberPOJO memberPOJO) {
            this.mContext = mContext;
            this.email=email;
            this.password=password;
            this.URL=URL;
            this.memberPOJO = memberPOJO;
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
            postData.put("email", email);
            postData.put("password", password);
         //   String url = "https://bwc.pentecostchurch.org/api/login";
         //   String urls = "http://52.172.221.235:8983/api/login";
            String json_output = performPostCall(URL, postData);
            return json_output;
        }

        @Override
        protected void onPostExecute(String output) {
            String token=null;
            try {
                Log.e("--->", output);
                JSONParser parser = new JSONParser();

                JSONObject object = (JSONObject)parser.parse(output.toString());
                token = (String)object.get("result");
                Log.e("--->",token);

            if(!token.equalsIgnoreCase("wrong email or password.")){

                Log.e("--->",memberPOJO.getServerType());
                if (memberPOJO.getServerType().equals(Constants.SUBMIT)){
                    memberRegisterGovNet(PreferenceUtils.getUrlCreateMember(mContext),memberPOJO,token);
                }else{
                    memberUpdateGovNet(PreferenceUtils.getUrlUpdateMember(mContext),memberPOJO,token);
                }
            }else{
                Toast.makeText(mContext,"Something went Wrong",Toast.LENGTH_LONG).show();
            }
            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }
        @Override
        protected void onCancelled() {
        }
    }


    public void memberRegisterGovNet(String URL, final MemberPOJO memberDetails,final String token){
        try {
            Log.e("---->",URL);

            final String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
         //   UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;



            multipartUploadRequest.addFileToUpload(memberDetails.getPhotoLocalPath(),"photo" )
                    .addParameter("token", token)
                    .addParameter("first_name", memberDetails.getFirstName())
                    .addParameter("middle_name", memberDetails.getMiddleName())
                    .addParameter("last_name", memberDetails.getLastName())
                    .addParameter("gender", memberDetails.getGender())
                    .addParameter("marital_status", memberDetails.getMaritalStatus())
                    .addParameter("status", memberDetails.getStatus())
                    .addParameter("mobile_phone", memberDetails.getMobilePhone())
                    .addParameter("dob", memberDetails.getDob())
                    .addParameter("address", memberDetails.getAddress())
                    .addParameter("fingerprint", memberDetails.getFingerPrint())
                    .addParameter("home_phone", memberDetails.getHomePhone())
                    .addParameter("work_phone", memberDetails.getWorkPhone())
                    .addParameter("email", memberDetails.getEmail())
                    .addParameter("notes", memberDetails.getNotes())
                    .addParameter("fingerprint2",memberDetails.getFingerPrint2())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                      //      buttonSubmit.setEnabled(true);
                           progressBar.setVisibility(View.GONE);
                          //  Toast.makeText(MemberRegisterActivity.this,"error",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("--->",serverResponse.getBodyAsString().toString());

                            JSONParser parser = new JSONParser();
                            MemberPOJO memberRegister = new MemberPOJO();
                            JSONObject jsonObject=null;
                            try{
                                jsonObject = (JSONObject)parser.parse(serverResponse.getBodyAsString().toString());
                                String result = (String) jsonObject.get("result");
                                Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();

                                JSONObject memberObject = (JSONObject) jsonObject.get("member");
                                memberRegister.setFirstName(isNull(memberObject,"first_name"));
                                memberRegister.setMiddleName(isNull(memberObject,"middle_name"));
                                memberRegister.setLastName(isNull(memberObject,"last_name"));
                                memberRegister.setEmail(isNull(memberObject,"email"));
                                memberRegister.setUserId(isNull(memberObject,"user_id"));
                                memberRegister.setRollNo(isNull(memberObject,"No"));
                                memberRegister.setGender(isNull(memberObject,"gender"));
                                memberRegister.setMaritalStatus(isNull(memberObject,"marital_status"));
                                memberRegister.setStatus(isNull(memberObject,"status"));
                                memberRegister.setHomePhone(isNull(memberObject,"home_phone"));
                                memberRegister.setMobilePhone(isNull(memberObject,"mobile_phone"));
                                memberRegister.setWorkPhone(isNull(memberObject,"work_phone"));
                                memberRegister.setFingerPrint(isNull(memberObject,"fingerprint"));
                                memberRegister.setDob(isNull(memberObject,"dob"));
                                memberRegister.setPhotoURL(isNull(memberObject,"photo"));
                                memberRegister.setAddress(isNull(memberObject,"address"));
                                memberRegister.setUpdateAt(isNull(memberObject,"updated_at"));
                                memberRegister.setCreateAt(isNull(memberObject,"created_at"));
                                memberRegister.setId(isNull(memberObject,"id"));
                                memberRegister.setNotes(isNull(memberObject,"notes"));
                                memberRegister.setFingerPrint2(isNull(memberObject,"fingerprint2"));

                                dataBaseHelper.insertMemberData(memberRegister);
/*
                                dataBaseHelperOffline.deleteOfflineMember(memberDetails);
                                list.addAll(dataBaseHelperOffline.getAllOfflineMembers());
                                memberAdapter = new MemberAdapter(mContext,list,"ic_person.png");
                                memberAdapter.notifyDataSetChanged();
                                listView.setAdapter(memberAdapter);
*/
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            progressBar.setVisibility(View.GONE);
                            dataBaseHelperOffline.deleteOfflineMember(memberDetails);
                            list = (ArrayList<MemberPOJO>) dataBaseHelperOffline.getAllOfflineMembers();
                            memberAdapter = new MemberAdapter(mContext,list,"ic_person.png");
                            memberAdapter.notifyDataSetChanged();
                            listView.setAdapter(memberAdapter);
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

    public void memberUpdateGovNet(String URL, final MemberPOJO memberDetails,String token){
        try {
            final String uploadId = UUID.randomUUID().toString();
            Log.e("--->",memberDetails.toString());

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver = null;


            if (memberDetails.getServerType().equals(Constants.UPDATEWITHIMAGE)) {
                Log.e("--->",Constants.UPDATEWITHIMAGE);

                multipartUploadRequest.addFileToUpload(memberDetails.getPhotoLocalPath(), "photo")
                        .addParameter("token", token)
                        .addParameter("first_name", memberDetails.getFirstName())
                        .addParameter("middle_name", memberDetails.getMiddleName())
                        .addParameter("last_name", memberDetails.getLastName())
                        .addParameter("gender", memberDetails.getGender())
                        .addParameter("marital_status", memberDetails.getMaritalStatus())
                        .addParameter("status", memberDetails.getStatus())
                        .addParameter("mobile_phone", memberDetails.getMobilePhone())
                        .addParameter("dob", memberDetails.getDob())
                        .addParameter("address", memberDetails.getAddress())
                        .addParameter("fingerprint", memberDetails.getFingerPrint())
                        .addParameter("home_phone", memberDetails.getHomePhone())
                        .addParameter("work_phone", memberDetails.getWorkPhone())
                        .addParameter("email", memberDetails.getEmail())
                        .addParameter("notes", memberDetails.getNotes())
                        .addParameter("id", memberDetails.getUserId())
                        .addParameter("fingerprint2",memberDetails.getFingerPrint2())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Log.e("---->", serverResponse.getBodyAsString().toString());
                                Log.e("--->",Constants.UPDATEWITHIMAGE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext,"fail",Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                                Log.e("--->", serverResponse.getBodyAsString().toString());
                                JSONParser parser = new JSONParser();
                                MemberPOJO memberRegister = new MemberPOJO();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = (JSONObject) parser.parse(serverResponse.getBodyAsString().toString());
                                    String result = (String) jsonObject.get("result");
                                    JSONObject memberObject = (JSONObject) jsonObject.get("member");
                                    memberRegister.setFirstName(isNull(memberObject, "first_name"));
                                    memberRegister.setMiddleName(isNull(memberObject, "middle_name"));
                                    memberRegister.setLastName(isNull(memberObject, "last_name"));
                                    memberRegister.setEmail(isNull(memberObject, "email"));
                                    memberRegister.setUserId(isNull(memberObject, "user_id"));
                                    memberRegister.setRollNo(isNull(memberObject, "No"));
                                    memberRegister.setGender(isNull(memberObject, "gender"));
                                    memberRegister.setMaritalStatus(isNull(memberObject, "marital_status"));
                                    memberRegister.setStatus(isNull(memberObject, "status"));
                                    memberRegister.setHomePhone(isNull(memberObject, "home_phone"));
                                    memberRegister.setMobilePhone(isNull(memberObject, "mobile_phone"));
                                    memberRegister.setWorkPhone(isNull(memberObject, "work_phone"));
                                    memberRegister.setFingerPrint(isNull(memberObject, "fingerprint"));
                                    memberRegister.setDob(isNull(memberObject, "dob"));
                                    memberRegister.setPhotoURL(isNull(memberObject, "photo"));
                                    memberRegister.setAddress(isNull(memberObject, "address"));
                                    memberRegister.setUpdateAt(isNull(memberObject, "updated_at"));
                                    memberRegister.setCreateAt(isNull(memberObject, "created_at"));
                                    memberRegister.setId(isNull(memberObject, "id"));
                                    memberRegister.setNotes(isNull(memberObject, "notes"));
                                    memberRegister.setFingerPrint2(isNull(memberObject,"fingerprint2"));
                                    //    dataBaseHelper.insertMemberData(memberRegister);
                                    dataBaseHelper.updateMemberData(memberRegister);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                progressBar.setVisibility(View.GONE);
                                dataBaseHelperOffline.deleteOfflineMember(memberDetails);
                                list = (ArrayList<MemberPOJO>) dataBaseHelperOffline.getAllOfflineMembers();
                                memberAdapter = new MemberAdapter(mContext,list,"ic_person.png");
                                memberAdapter.notifyDataSetChanged();
                                listView.setAdapter(memberAdapter);

                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Log.e("--->", "caln");
                            }
                        })
                        .startUpload(); //Starting the upload
            }else if(memberDetails.getServerType().equals(Constants.UDATEWITHOUTIMAGE)) {
                Log.e("--->",Constants.UDATEWITHOUTIMAGE);
                multipartUploadRequest.addParameter("token", token)
                        .addParameter("first_name", memberDetails.getFirstName())
                        .addParameter("middle_name", memberDetails.getMiddleName())
                        .addParameter("last_name", memberDetails.getLastName())
                        .addParameter("gender", memberDetails.getGender())
                        .addParameter("marital_status", memberDetails.getMaritalStatus())
                        .addParameter("status", memberDetails.getStatus())
                        .addParameter("mobile_phone", memberDetails.getMobilePhone())
                        .addParameter("dob", memberDetails.getDob())
                        .addParameter("address", memberDetails.getAddress())
                        .addParameter("fingerprint", memberDetails.getFingerPrint())
                        .addParameter("home_phone", memberDetails.getHomePhone())
                        .addParameter("work_phone", memberDetails.getWorkPhone())
                        .addParameter("email", memberDetails.getEmail())
                        .addParameter("notes", memberDetails.getNotes())
                        .addParameter("id", memberDetails.getUserId())
                        .addParameter("fingerprint2", memberDetails.getFingerPrint2())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Log.e("---->", serverResponse.getBodyAsString().toString());
                                Log.e("--->",Constants.UDATEWITHOUTIMAGE);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext,"fail",Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                                Log.e("--->", serverResponse.getBodyAsString().toString());
                                JSONParser parser = new JSONParser();
                                MemberPOJO memberRegister = new MemberPOJO();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = (JSONObject) parser.parse(serverResponse.getBodyAsString().toString());
                                    String result = (String) jsonObject.get("result");
                                    JSONObject memberObject = (JSONObject) jsonObject.get("member");
                                    memberRegister.setFirstName(isNull(memberObject, "first_name"));
                                    memberRegister.setMiddleName(isNull(memberObject, "middle_name"));
                                    memberRegister.setLastName(isNull(memberObject, "last_name"));
                                    memberRegister.setEmail(isNull(memberObject, "email"));
                                    memberRegister.setUserId(isNull(memberObject, "user_id"));
                                    memberRegister.setRollNo(isNull(memberObject, "No"));
                                    memberRegister.setGender(isNull(memberObject, "gender"));
                                    memberRegister.setMaritalStatus(isNull(memberObject, "marital_status"));
                                    memberRegister.setStatus(isNull(memberObject, "status"));
                                    memberRegister.setHomePhone(isNull(memberObject, "home_phone"));
                                    memberRegister.setMobilePhone(isNull(memberObject, "mobile_phone"));
                                    memberRegister.setWorkPhone(isNull(memberObject, "work_phone"));
                                    memberRegister.setFingerPrint(isNull(memberObject, "fingerprint"));
                                    memberRegister.setDob(isNull(memberObject, "dob"));
                                    memberRegister.setPhotoURL(isNull(memberObject, "photo"));
                                    memberRegister.setAddress(isNull(memberObject, "address"));
                                    memberRegister.setUpdateAt(isNull(memberObject, "updated_at"));
                                    memberRegister.setCreateAt(isNull(memberObject, "created_at"));
                                    memberRegister.setId(isNull(memberObject, "id"));
                                    memberRegister.setNotes(isNull(memberObject, "notes"));
                                    memberRegister.setFingerPrint2(isNull(memberObject,"fingerprint2"));
                                    dataBaseHelper.updateMemberData(memberRegister);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                progressBar.setVisibility(View.GONE);
                                dataBaseHelperOffline.deleteOfflineMember(memberDetails);
                                list = (ArrayList<MemberPOJO>) dataBaseHelperOffline.getAllOfflineMembers();
                                memberAdapter = new MemberAdapter(mContext,list,"ic_person.png");
                                memberAdapter.notifyDataSetChanged();
                                listView.setAdapter(memberAdapter);

                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Log.e("--->", "caln");
                            }
                        })
                        .startUpload(); //Starting the upload
            }
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getAccessTokenGovNet(String URL, final String mEmail, final String mPassword, final MemberPOJO memberPOJO){
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
                            } catch (org.json.simple.parser.ParseException e) {
                                e.printStackTrace();
                            }

                            PreferenceUtils.setToken(mContext,token);
                            switch (memberPOJO.getServerType()) {
                                case "update":
                                    //                                  memberUpdateGovNet(PreferenceUtils.getUrlUpdateMember(mContext), memberPOJO);
                                    break;
                                case "submit":
                                    memberRegisterGovNet(PreferenceUtils.getUrlCreateMember(mContext), memberPOJO,token);
                                    break;
                            }
                        }
                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}
