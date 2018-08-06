package in.co.ashclan.fingerprint;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import in.co.ashclan.adpater.ContributionAdapter;
import in.co.ashclan.adpater.EventAttendanceAdapter;
import in.co.ashclan.adpater.FamilyAdapter;
import in.co.ashclan.adpater.GroupsAdapter;
import in.co.ashclan.adpater.PledgeAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.ContributionsPOJO;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.FamilyPOJO;
import in.co.ashclan.model.GroupsPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.model.PledgesPOJO;
import in.co.ashclan.utils.PreferenceUtils;

public class MemberDetailsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationBar.OnTabSelectedListener,
        View.OnClickListener
{
    FloatingActionButton fab;
    BottomNavigationBar bottomNavigationBar;
    LinearLayout linearLayout;
    private int[] layouts;
    ContributionsPOJO contriPOJO;
    ViewPager viewPager;
    MyViewPagerAdapter myViewPagerAdapter;
    MyAttenViewPagerAdapter myAttenViewPagerAdapter;
    MyContriViewPagerAdapter myContriViewPagerAdapter;
    MyPledgeViewPagerAdapter myPledgeViewPagerAdapter;
    MyFamilyViewPagerAdapter myFamilyViewPagerAdapter;


    LinearLayout layoutEmail,layoutMobile,layoutHome,layoutWork,layoutAddress;

    TextView textViewName, textViewId, textViewAge, textViewGender, textViewStatus, textViewMaritalStatus,
            textViewEmail, textViewMobilePhone, textViewHomePhone, textViewWorkPhone, textViewAddress;
    ImageView profileImageView, editImageView;
    ViewPager viewPagerMember;
    int lastSelectedPosition = 0;
    MemberPOJO memberDetails = new MemberPOJO();
    EventPOJO event =  new EventPOJO();
    ArrayList<MemberPOJO> list=new ArrayList<MemberPOJO>();
    DataBaseHelper dataBaseHelper;
    RelativeLayout removeLayout;
    MemberFragment memberFragment;
    ImageLoaderConfiguration loaderConfiguration;
    ImageLoader imageLoader = ImageLoader.getInstance();

    Context context,mContext;// = MemberDetailsActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        mContext=this;
        setContentView(R.layout.activity_member_details_hm);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataBaseHelper = new DataBaseHelper(this);
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);

        list.addAll(dataBaseHelper.getAllMembers());

        inits();
        MemberPOJO m=(MemberPOJO) getIntent().getSerializableExtra("member_details");
        memberDetails=dataBaseHelper.getMemberDetails(m.getId());
        Log.e("----jj>",memberDetails.toString());
        navigationBar();
        setDetails(memberDetails);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MemberDetailsActivity.this,MemberRegisterActivity.class);
                intent.putExtra("type","register");
                startActivity(intent);

            }
        });
        fab.setVisibility(View.INVISIBLE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);

        bottomNavigationBar.setTabSelectedListener(this);
    }
    public void inits(){

        linearLayout = (LinearLayout)findViewById(R.id.layout_details);
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        viewPager = (ViewPager)findViewById(R.id.member_view_pager);
        profileImageView = (ImageView)findViewById(R.id.profile_image);
        editImageView = (ImageView)findViewById(R.id.edit);
        textViewName = (TextView)findViewById(R.id.member_name);
        textViewId = (TextView)findViewById(R.id.member_id);
        textViewAge = (TextView)findViewById(R.id.member_age);
        textViewGender = (TextView)findViewById(R.id.member_gender);
        textViewStatus = (TextView)findViewById(R.id.member_status);
        textViewMaritalStatus = (TextView)findViewById(R.id.member_marital_status);
        textViewEmail = (TextView)findViewById(R.id.member_email);
        textViewMobilePhone = (TextView)findViewById(R.id.member_mobile_phone);
        textViewHomePhone = (TextView)findViewById(R.id.member_home_phone);
        textViewWorkPhone = (TextView)findViewById(R.id.member_work_phone);
        textViewAddress = (TextView)findViewById(R.id.member_address);
        removeLayout=(RelativeLayout)findViewById(R.id.removeLayout);

        layoutEmail=(LinearLayout)findViewById(R.id.layout_email);
        layoutMobile=(LinearLayout)findViewById(R.id.layout_mobile);
        layoutHome=(LinearLayout)findViewById(R.id.layout_home);
        layoutWork=(LinearLayout)findViewById(R.id.layout_work);
        layoutAddress=(LinearLayout)findViewById(R.id.layout_address);

        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageOnLoading(R.drawable.ic_person)
                .showImageForEmptyUri(R.drawable.ic_person)
                .showImageOnFail(R.drawable.ic_person)
                .build();

        loaderConfiguration = new ImageLoaderConfiguration.Builder(MemberDetailsActivity.this)
                .defaultDisplayImageOptions(imageOptions).build();
        imageLoader.init(loaderConfiguration);
        layouts = new int[]
                {
                        R.layout.fragment_fragment_groups
                };
        bottomNavigationBar.setTabSelectedListener(this);
        editImageView.setOnClickListener(this);
    }
    public void setDetails(MemberPOJO memberDetails){

        textViewId.setText(memberDetails.getRollNo());
        textViewName.setText(memberDetails.getFirstName()+" "+memberDetails.getLastName());

        if(memberDetails.getDob()!=null) {
            String birthdateStr = memberDetails.getDob();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            try {
                Date birthdate = df.parse(birthdateStr);
                textViewAge.setText(String.valueOf(calculateAge(birthdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        textViewGender.setText(memberDetails.getGender());
        textViewStatus.setText(memberDetails.getStatus());
        textViewMaritalStatus.setText(memberDetails.getMaritalStatus());

        if(memberDetails.getEmail().equals("")){
            layoutEmail.setVisibility(View.GONE);
        }else {
            textViewEmail.setText(memberDetails.getEmail());
        }
        if (memberDetails.getMobilePhone().equals("")){
            layoutEmail.setVisibility(View.GONE);
        }else {
            textViewMobilePhone.setText(memberDetails.getMobilePhone());
        }
        if (memberDetails.getHomePhone().equals("")){
            layoutHome.setVisibility(View.GONE);
        }else {
            textViewHomePhone.setText(memberDetails.getHomePhone());
        }
        if (memberDetails.getWorkPhone().equals("")){
            layoutWork.setVisibility(View.GONE);
        }else {
            textViewWorkPhone.setText(memberDetails.getWorkPhone());
        }

        Log.e("--->",memberDetails.getHomePhone()+"        "+memberDetails.getWorkPhone()+"   "+memberDetails.getMobilePhone());

        if (memberDetails.getAddress().equals("")){
            layoutAddress.setVisibility(View.GONE);
        }else {
            textViewAddress.setText(memberDetails.getAddress());
        }
        //image set
        //profileImageView.

        if (null!=memberDetails.getPhotoURL()) {
            //    "http://52.172.221.235:8983/uploads/"
            String imgURL = PreferenceUtils.getUrlUploadImage(context) + memberDetails.getPhotoURL();
            try {
                imageLoader.displayImage(imgURL, profileImageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        //    imageLoader.displayImage("http://52.172.221.235:8983/uploads/" + defaultIcon, imageView);
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        // profileImageView.setImageBitmap();
                        profileImageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_image_1));
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Log.e("--->", imageUri);
                        Log.e("--->", loadedImage.toString());
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }catch (Exception ex){
                profileImageView.setImageResource(R.drawable.ic_profile_image_1);
                ex.printStackTrace();
            }
        }
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
//8788624753
        }
        return yearDifference;
    }

    //BottomNavigationBar.OnTabSelectedListener
    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;
        Toast.makeText(mContext,position+"",Toast.LENGTH_LONG).show();
        setViewPager(position);
    }
    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    //View.OnClickListerner
    @Override
    public void onClick(View view) {
        if(view==editImageView){
            Intent intent = new Intent(MemberDetailsActivity.this,MemberRegisterActivity.class);
            intent.putExtra("type","edit");
            intent.putExtra("member",memberDetails);
            startActivityForResult(intent,101);
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==101)
        {
            if (resultCode == Activity.RESULT_OK){
                MemberPOJO m = (MemberPOJO)data.getSerializableExtra("memberRegister");
                textViewId.setText(m.getRollNo());
                textViewName.setText(m.getFirstName()+" "+m.getLastName());

                if(m.getDob()!=null) {
                    String birthdateStr = m.getDob();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
                    try {
                        Date birthdate = df.parse(birthdateStr);
                        textViewAge.setText(String.valueOf(calculateAge(birthdate)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                textViewGender.setText(m.getGender());
                textViewStatus.setText(m.getStatus());
                textViewMaritalStatus.setText(m.getMaritalStatus());


                if(m.getEmail().equals("")){
                    layoutEmail.setVisibility(View.GONE);
                }else {
                    textViewEmail.setText(m.getEmail());
                }
                if (m.getMobilePhone().equals("")){
                    layoutEmail.setVisibility(View.GONE);
                }else {
                    textViewMobilePhone.setText(m.getMobilePhone());
                }
                if (m.getHomePhone().equals("")){
                    layoutHome.setVisibility(View.GONE);
                }else {
                    textViewHomePhone.setText(m.getHomePhone());
                }
                if (m.getWorkPhone().equals("")){
                    layoutWork.setVisibility(View.GONE);
                }else {
                    textViewWorkPhone.setText(m.getWorkPhone());
                }

                //image set
                //profileImageView.

                if (null!=m.getPhotoURL()) {
//                    String imgURL = "http://52.172.221.235:8983/uploads/" + m.getPhotoURL();
                    String imgURL = PreferenceUtils.getUrlUploadImage(mContext) + m.getPhotoURL();

                    imageLoader.displayImage(imgURL, profileImageView, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            //    imageLoader.displayImage("http://52.172.221.235:8983/uploads/" + defaultIcon, imageView);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });
                }
            }
        }
    }

    private void navigationBar(){
        bottomNavigationBar.clearAll();
        //setFragment(0);
        setViewPager(0);

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_volunteers, "Groups").setActiveColorResource(R.color.blue4))
                .addItem(new BottomNavigationItem(R.drawable.ic_attender, "Attendance").setActiveColorResource(R.color.blue3))
                .addItem(new BottomNavigationItem(R.drawable.ic_contributions, "Contributions").setActiveColorResource(R.color.blue2))
                .addItem(new BottomNavigationItem(R.drawable.ic_pledges, "Pledges").setActiveColorResource(R.color.blue3))
                .addItem(new BottomNavigationItem(R.drawable.ic_volunteers, "Family").setActiveColorResource(R.color.blue4))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();
    }

    private void setViewPager(int index) {
        switch (index) {
            case 0:
                myViewPagerAdapter = new MyViewPagerAdapter(mContext);
                viewPager.setAdapter(myViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 1:
                myAttenViewPagerAdapter = new MyAttenViewPagerAdapter(mContext);
                viewPager.setAdapter(myAttenViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 2:
                myContriViewPagerAdapter = new MyContriViewPagerAdapter(mContext);
                viewPager.setAdapter(myContriViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 3:
                myPledgeViewPagerAdapter = new MyPledgeViewPagerAdapter(mContext);
                viewPager.setAdapter(myPledgeViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 4:
                myFamilyViewPagerAdapter = new MyFamilyViewPagerAdapter(mContext);
                viewPager.setAdapter(myFamilyViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
        }
    }

    //**********************************************************************************
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.just_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            PreferenceUtils.setSignIn(MemberDetailsActivity.this,false);
            Intent intent = new Intent(MemberDetailsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_member) {

            MemberDetailsActivity.this.finish();
    /*        Intent intent = new Intent(MemberDetailsActivity.this,MemberListActivity.class);
            startActivity(intent);
            finish();
*/
        } else if (id == R.id.nav_events) {
            Intent intent = new Intent(MemberDetailsActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();

        } else if (id == R.id.nav_recording) {
            Intent intent = new Intent(MemberDetailsActivity.this,RecordingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intent = new Intent(MemberDetailsActivity.this,MemberDetailsActivity.class);
            startActivity(intent);
        } else if(id==R.id.nav_upload){
            Intent intent = new Intent(mContext, UploadActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
*/

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
                        } catch (org.json.simple.parser.ParseException e) {
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
                            memberDetailsVolley(PreferenceUtils.getUrlGetAllMembers(context),PreferenceUtils.getToken(context));
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

                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
                        //"http://52.172.221.235:8983/api/get_all_events"
                        eventDetailsVolley(PreferenceUtils.getUrlGetAllEvents(context),
                                PreferenceUtils.getToken(context));

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

                                // EventPOJO event = new EventPOJO();
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
                                //                              eventList.add(event);
                                dataBaseHelper.insertEventData(event);
                            }

                        } catch (org.json.simple.parser.ParseException e) {
                            e.printStackTrace();
                        }
//                        eventAdapter.notifyDataSetChanged();
                        //    memberFragment.memberAdapter.notifyDataSetChanged();
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
                            } catch (org.json.simple.parser.ParseException e) {
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

//                                memberList.add(member);
                                    dataBaseHelper.insertMemberData(member);
                                }

                            } catch (org.json.simple.parser.ParseException e) {
                                e.printStackTrace();
                            }
                            //"http://52.172.221.235:8983/api/get_all_events"
                            eventDetailsGovNet(PreferenceUtils.getUrlGetAllEvents(context),
                                    PreferenceUtils.getToken(context));

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
                                    //                              eventList.add(event);
                                    dataBaseHelper.insertEventData(event);
                                }

                            } catch (org.json.simple.parser.ParseException e) {
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

    public String isNull(JSONObject object, String parma){
        return object.get(parma)!=null?object.get(parma).toString():"";
    }

    public String isNull(JSONObject object, String parma,String dafualtStr){
        return object.get(parma)!=null?object.get(parma).toString():dafualtStr;
    }

    /****************************** Group Pager Adapter Class *************************/
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        Context vContext;
        GroupsPOJO groupsPOJO;
        ArrayList<GroupsPOJO> groupsDetailsList = new ArrayList<>();

        public MyViewPagerAdapter(){
        }

        public MyViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            view = inflater.inflate(R.layout.fragment_fragment_groups,container,false);
            container.addView(view);

            ListView listView = (ListView)view.findViewById(R.id.list_groups);

            String member_id = memberDetails.getId().toString();
            // groupsDetailsList = (ArrayList<GroupsPOJO>) dataBaseHelper.getAllGroupsList(member_id);


            groupsDetailsList.addAll(dataBaseHelper.getAllGroupsList(member_id));
            GroupsAdapter groupsAdapter = new GroupsAdapter(MemberDetailsActivity.this,groupsDetailsList);
            listView.setAdapter(groupsAdapter);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            View view = (View)object;
            container.removeView(view);
        }
    }
    /****************************** Attendance Pager Adapter Class *************************/
    public class MyAttenViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        Context vContext;
        ArrayList<EventAttendancePOJO> DetailsList = new ArrayList<>();
        EventAttendancePOJO eventAttendancePOJO;


        public MyAttenViewPagerAdapter(){
        }
        public MyAttenViewPagerAdapter(Context vContext,EventAttendancePOJO EventAttendancePOJO){
            this.vContext = vContext;
            this.eventAttendancePOJO = EventAttendancePOJO;
        }
        public MyAttenViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            view = inflater.inflate(R.layout.fragment_attendance_fragement,container,false);
            container.addView(view);
            ListView listView = (ListView)view.findViewById(R.id.list_attendance);

            String member_id = memberDetails.getId().toString();
            String event_id = event.getId().toString();

            DetailsList.addAll(dataBaseHelper.getAllAttendance(member_id));
            EventAttendanceAdapter eventAttendanceAdapter = new EventAttendanceAdapter(MemberDetailsActivity.this,DetailsList);
            listView.setAdapter(eventAttendanceAdapter);

            return view;
        }
        @Override
        public int getCount() {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            View view = (View)object;
            container.removeView(view);
        }
    }
    /****************************** Contribution Pager Adapter Class *************************/
    public class MyContriViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        Context vContext;
        ContributionsPOJO contriPOJO;
        ArrayList<ContributionsPOJO> contriDetails = new ArrayList<>();

        public MyContriViewPagerAdapter(){
        }
        public MyContriViewPagerAdapter(Context vContext,ContributionsPOJO contributionsPOJO){
            this.vContext = vContext;
            this.contriPOJO = contributionsPOJO;
        }
        public MyContriViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            view = inflater.inflate(R.layout.fragment_contribution,container,false);
            container.addView(view);

            ListView listView = (ListView)view.findViewById(R.id.list_contribution);
            String member_id = memberDetails.getId().toString();

            contriDetails.addAll(dataBaseHelper.getAllContributionList(member_id));
            ContributionAdapter contributionAdapter = new ContributionAdapter(MemberDetailsActivity.this,contriDetails);
            listView.setAdapter(contributionAdapter);


            return view;
        }
        @Override
        public int getCount() {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            View view = (View)object;
            container.removeView(view);
        }
    }
    /******************************Pledge Pager Adapter Class *************************/
    public class MyPledgeViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        Context vContext;
        PledgesPOJO pledgesPOJO;
        ArrayList<PledgesPOJO> Details = new ArrayList<>();

        public MyPledgeViewPagerAdapter(){
        }
        public MyPledgeViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            view = inflater.inflate(R.layout.fragment_pledges_fragments,container,false);
            container.addView(view);

            ListView listView = (ListView)view.findViewById(R.id.list_pledges);
            String member_id = memberDetails.getId().toString();

//            PledgesPOJO pledgesPOJO = new PledgesPOJO("2","Donation","2000","2018-7-23","Amateratsu");
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);
//            Details.add(pledgesPOJO);

            Details.addAll(dataBaseHelper.getAllPledgesList(member_id));
            PledgeAdapter pledgeAdapter = new PledgeAdapter(MemberDetailsActivity.this,Details);
            listView.setAdapter(pledgeAdapter);

            return view;
        }
        @Override
        public int getCount() {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            View view = (View)object;
            container.removeView(view);
        }
    }

    /****************************** Family Pager Adapter Class *************************/
    public class MyFamilyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;
        Context vContext;
        FamilyPOJO contriPOJO;
        ArrayList<FamilyPOJO> Details = new ArrayList<>();


        public MyFamilyViewPagerAdapter(){
        }
        public MyFamilyViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            view = inflater.inflate(R.layout.fragment_fmaily,container,false);
            container.addView(view);

            ListView listView = (ListView)view.findViewById(R.id.list_family);
            String member_id = memberDetails.getId().toString();

            Details.addAll(dataBaseHelper.getAllFamilyList(member_id));
            FamilyAdapter familyAdapter = new FamilyAdapter(MemberDetailsActivity.this,Details);
            listView.setAdapter(familyAdapter);

            return view;
        }
        @Override
        public int getCount() {
            return layouts.length;
        }
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);
            View view = (View)object;
            container.removeView(view);
        }
    }
}
