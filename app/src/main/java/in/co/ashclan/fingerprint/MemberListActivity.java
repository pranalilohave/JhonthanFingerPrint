package in.co.ashclan.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;

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

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import in.co.ashclan.AsynkTask.DownloadTask;
import in.co.ashclan.adpater.EventAdapter;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.ashclancalendar.data.Day;
import in.co.ashclan.ashclancalendar.widget.CollapsibleCalendar;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;
import in.co.ashclan.utils.Utils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class MemberListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    DataBaseHelper dataBaseHelper;
    FloatingActionButton fab;
    ListView listView;
    ImageView imageX;
   // ArrayList<MemberPOJO> sList = new ArrayList<MemberPOJO>();
    ArrayList<MemberPOJO> memberList = new ArrayList<MemberPOJO>();
    ArrayList<EventPOJO> eventList = new ArrayList<EventPOJO>();
    private AsyncTask mMyTask;
    EventAdapter eventAdapter;

    Context mContext;
    ProgressBar progressBar;

    MemberAdapter memberAdapter;
    EditText sreachEditText;
    LinearLayout searchLayout;

    InputMethodManager inputMethodManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_list);
        mContext=this;
        dataBaseHelper = new DataBaseHelper(mContext);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        memberList.addAll(dataBaseHelper.getAllMembers());
        eventList.addAll(dataBaseHelper.getAllEvent());

        listView = (ListView)findViewById(R.id.list);
        imageX = (ImageView)findViewById(R.id.x_img);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
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
        navigationView.getMenu().getItem(0).setChecked(true);

        memberAdapter = new MemberAdapter(mContext,memberList,"ic_person.png");
        memberAdapter.notifyDataSetChanged();
       // dataBaseHelper=new DataBaseHelper(mContext);
        listView.setAdapter(memberAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MemberPOJO memberDetails = memberList.get(i);
                Log.e("---->M",memberDetails.toString());
                Intent intent = new Intent(mContext,MemberDetailsActivity.class);
                intent.putExtra("member_details",memberDetails);
                startActivity(intent);
            }
        });

        searchLayout=(LinearLayout)findViewById(R.id.layout_search);
        sreachEditText=(EditText)findViewById(R.id.edit_search);

        sreachEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                memberList.clear();
                memberList = (ArrayList) dataBaseHelper.getSerachMember(sreachEditText.getText().toString());
                memberAdapter = new MemberAdapter(mContext,memberList,"ic_person.png");
                memberAdapter.notifyDataSetChanged();
                listView.setAdapter(memberAdapter);
                //   sreachEditText.setText("");
                //   Utils.hideKeyboard(MemberFragment.this.getActivity());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        imageX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchLayout.setVisibility(View.GONE);
                memberAdapter = new MemberAdapter(mContext,memberList,"ic_person.png");
                memberAdapter.notifyDataSetChanged();
                listView.setAdapter(memberAdapter);
                    //inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    //inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
                closeKeyboard();
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
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            PreferenceUtils.setSignIn(mContext,false);
            Intent intent = new Intent(mContext, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
         }
        if(id == R.id.action_refresh){
            progressBar.setVisibility(View.VISIBLE);
            Log.e("--->","URL= "+PreferenceUtils.getUrlLogin(mContext));
     //       getAccessTokenGovNet(PreferenceUtils.getUrlLogin(mContext),PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext));
                GetAccessTokenTask aTask = new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext));
                aTask.execute();
        }
        if (id == R.id.action_search){
            searchLayout.setVisibility(View.VISIBLE);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_member) {
/*
            Intent intent = new Intent(mContext,MemberListActivity.class);
            startActivity(intent);
            finish();
   */
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(mContext,HomeActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_recording) {
            Intent intent = new Intent(mContext,RecordingActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(mContext, SettingsActivity.class);
            startActivity(intent);


        } else if(id==R.id.nav_upload){
            Intent intent = new Intent(mContext, UploadActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void onClick(View view) {

    }


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
                        if(token.equalsIgnoreCase("wrong email or password.")|| !PreferenceUtils.getSignIn(mContext)){
                        }else {
                            PreferenceUtils.setToken(mContext, token);
                            /*
                            Intent intent = new Intent(SplashActivity.this, DataLoadingActivity.class);
                            intent.putExtra("token", "");
                            startActivity(intent);
                            finish();
                            */
                            //"http://52.172.221.235:8983/api/get_all_members"
                            memberDetailsVolley(PreferenceUtils.getUrlGetAllMembers(mContext),PreferenceUtils.getToken(mContext));
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

//                                memberList.add(member);
                                dataBaseHelper.insertMemberData(member);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //"http://52.172.221.235:8983/api/get_all_events"
                        eventDetailsVolley(PreferenceUtils.getUrlGetAllEvents(mContext),
                                PreferenceUtils.getToken(mContext));

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
                        eventAdapter.notifyDataSetChanged();
                        progressBar.bringToFront();
                        progressBar.setVisibility(View.GONE);
                        //        MemberFragment.newInstance().memberAdapter.notifyDataSetChanged();
//                     memberFragment.memberAdapter.notifyDataSetChanged();
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
                            progressBar.setVisibility(View.GONE);
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            Log.e("--->", serverResponse.getBodyAsString().toString());
                            JSONParser parser_obj = new JSONParser();
                            String token = null;
                            try {
                                JSONObject object = (JSONObject) parser_obj.parse(serverResponse.getBodyAsString().toString());
                                token = (String)object.get("result");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //   Toast.makeText(SplashActivity.this,token,Toast.LENGTH_SHORT).show();
                            if(token.equalsIgnoreCase("wrong email or password.")|| !PreferenceUtils.getSignIn(context)){
                            }else {
                                PreferenceUtils.setToken(context, token);
                            /*
                            Intent intent = new Intent(SplashActivity.this, DataLoadingActivity.class);
                            intent.putExtra("token", "");
                            startActivity(intent);
                            finish();
                            */
                                //"http://52.172.221.235:8983/api/get_all_members"
                                memberDetailsGovNet(PreferenceUtils.getUrlGetAllMembers(context),PreferenceUtils.getToken(context));
                            }

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln");
                            progressBar.setVisibility(View.GONE);
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
                            progressBar.setVisibility(View.GONE);
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

//                                memberList.add(member);
                                    dataBaseHelper.insertMemberData(member);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            memberList = (ArrayList<MemberPOJO>)dataBaseHelper.getAllMembers();
                            memberAdapter = new MemberAdapter(mContext,memberList,"ic_person.png");
                            memberAdapter.notifyDataSetChanged();
                            listView.setAdapter(memberAdapter);
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->","caln");
                            progressBar.setVisibility(View.GONE);

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

    public class GetAccessTokenTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String email,password;

        GetAccessTokenTask(Context mContext,String URL,String email,String password) {
            this.mContext = mContext;
            this.email=email;
            this.password=password;
            this.URL=URL;

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
            String json_output = performPostCall(URL, postData);
            return json_output;
        }

        @Override
        protected void onPostExecute(String output) {
            String token=null;
            try {
                Log.e("--->","URL = "+URL+" email = "+email+" password = "+password);
                Log.e("--->", output);
                JSONParser parser = new JSONParser();

                JSONObject object = (JSONObject)parser.parse(output.toString());
                token = (String)object.get("result");
                Log.e("--->",token);

            if(token.equalsIgnoreCase("wrong email or password.")){

            }else{
                //"https://bwc.pentecostchurch.org/api/get_all_members"
               GetAllMemberTask mTask = new GetAllMemberTask(mContext,PreferenceUtils.getUrlGetAllMembers(mContext),token);
                mTask.execute();
            }
            } catch (Exception e) {
                Log.e("--->", e.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);

        }
    }
    public class GetAllMemberTask extends AsyncTask<String, String, String> {

        private Context context;
        private String URL;
        private String token;
        GetAllMemberTask(Context context,String URL,String token) {
            this.context = context;
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

                    member.setFirstName(Utils.isNull(object,"first_name"));
                    member.setMiddleName(Utils.isNull(object,"middle_name"));
                    member.setLastName(Utils.isNull(object,"last_name"));
                    member.setGender(Utils.isNull(object,"gender"));
                    member.setStatus(Utils.isNull(object,"status"));
                    member.setMaritalStatus(Utils.isNull(object,"marital_status"));
                    member.setDob(Utils.isNull(object,"dob","0000-00-00"));
                    member.setHomePhone(Utils.isNull(object,"home_phone"));
                    member.setMobilePhone(Utils.isNull(object,"mobile_phone"));
                    member.setWorkPhone(Utils.isNull(object,"work_phone"));
                    member.setEmail(Utils.isNull(object,"email"));
                    member.setAddress(Utils.isNull(object,"address"));
                    member.setNotes(Utils.isNull(object,"notes"));
                    member.setRollNo(Utils.isNull(object,"No"));
                    member.setCreateAt(Utils.isNull(object,"created_at"));
                    member.setUpdateAt(Utils.isNull(object,"updated_at"));
                    member.setFingerPrint(Utils.isNull(object,"fingerprint"));
                    member.setFingerPrint1(Utils.isNull(object,"fingerprint2"));

                    //remainber to do changes...
                    member.setPhotoURL(Utils.isNull(object,"photo",""));
                    Log.e("--->",member.toString());
                    dataBaseHelper.insertMemberData(member);

                        if (!dataBaseHelper.isPhotoAvailable(member.getId(), member.getPhotoURL())) {
                            mMyTask = new DownloadTask(mContext,member)
                                    .execute(stringToURL(
                                            //"http://www.freeimageslive.com/galleries/objects/general/pics/woodenbox0482.jpg"
                                            PreferenceUtils.getUrlUploadImage(mContext)+member.getPhotoURL()
                                    ));
                    }
                    else
                    {
                        Log.e("leave :- ",member.getId().toString());
                    }
                }

            memberList = (ArrayList<MemberPOJO>)dataBaseHelper.getAllMembers();
            memberAdapter = new MemberAdapter(mContext,memberList,"ic_person.png");
            memberAdapter.notifyDataSetChanged();
            listView.setAdapter(memberAdapter);



            //"https://bwc.pentecostchurch.org/api/get_all_events"
            //"http://52.172.221.235:8983/api/get_all_events"
            GetAllEventsTask eTask = new GetAllEventsTask(mContext,PreferenceUtils.getUrlGetAllEvents(mContext),token);
            eTask.execute();

        } catch (Exception e) {
            Log.e("--->", e.toString());
            progressBar.setVisibility(View.GONE);
            Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);

        }
    }
    public class GetAllEventsTask extends AsyncTask<String, String, String> {

        private Context context;
        private String URL;
        private String token;
        GetAllEventsTask(Context context,String URL,String token) {
            this.context = context;
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
                Log.e("--->", output);
                dataBaseHelper.deleteAllEvents();
                JSONParser parser = new JSONParser();
                JSONArray jsonArray =(JSONArray)parser.parse(output.toString());

                for(int i=0;i<jsonArray.size();i++){

                    EventPOJO event = new EventPOJO();
                    JSONObject object = (JSONObject)jsonArray.get(i);
                    event.setId(String.valueOf(object.get("id")));
                    event.setBranchId(Utils.isNull(object,"branch_id"));
                    event.setUserId(Utils.isNull(object,"user_id"));
                    event.setParentId(Utils.isNull(object,"parent_id"));
                    event.setEventLocationId(Utils.isNull(object,"event_location_id"));
                    event.setEventCalenderId(Utils.isNull(object,"event_calendar_id"));
                    event.setName(Utils.isNull(object,"name"));
                    event.setCost(Utils.isNull(object,"cost"));
                    event.setAllDay(Utils.isNull(object,"all_day"));
                    event.setStartDate(Utils.isNull(object,"start_date"));
                    event.setStart_time(Utils.isNull(object,"start_time"));
                    event.setEnd_date(Utils.isNull(object,"end_date"));
                    event.setEnd_time(Utils.isNull(object,"end_time"));
                    event.setRecurring(Utils.isNull(object,"recurring"));
                    event.setRecurFrequency(Utils.isNull(object,"recur_frequency"));
                    event.setRecurStartDate(Utils.isNull(object,"recur_start_date"));
                    event.setRecurEndDate(Utils.isNull(object,"recur_end_date"));
                    event.setRecurNextDate(Utils.isNull(object,"recur_next_date"));
                    event.setRecurType(Utils.isNull(object,"recur_type"));
                    event.setCheckInType(Utils.isNull(object,"checkin_type"));
                    event.setTags(Utils.isNull(object,"tags"));
                    event.setIncludeCheckOut(Utils.isNull(object,"include_checkout"));
                    event.setFamilyCheckIn(Utils.isNull(object,"family_checkin"));
                    event.setFeatured_image(Utils.isNull(object,"featured_image"));
                    event.setGallery(Utils.isNull(object,"gallery"));
                    event.setFiles(Utils.isNull(object,"files"));
                    event.setYear(Utils.isNull(object,"year"));
                    event.setMonth(Utils.isNull(object,"month"));
                    event.setNotes(Utils.isNull(object,"notes"));
                    event.setCreatedAt(Utils.isNull(object,"created_at"));
                    event.setUpdatedAt(Utils.isNull(object,"updated_at"));
                    Log.e("--->",event.toString());
                    dataBaseHelper.insertEventData(event);
                    progressBar.setVisibility(View.GONE);
                }

            } catch (Exception e) {
                Log.e("--->", e.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }/*
            eventList.clear();
            eventList.addAll(dataBaseHelper.getAllEvent());

            eventAdapter = new EventAdapter(HomeActivity.this, eventList);
            eventAdapter.notifyDataSetChanged();
            listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(eventAdapter);
            */

        }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);
        }
    }


    private void closeKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null){
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
        }
    }
    // Custom method to convert string to url
    protected URL stringToURL(String urlString){
        try{
            URL url = new URL(urlString);
            return url;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return null;
    }
}
