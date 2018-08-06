package in.co.ashclan.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.fgtit.fpcore.FPMatch;

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

import java.io.IOException;
import java.security.InvalidParameterException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import android_serialport_api.SerialPortManager;
import android_serialport_api.SerialPortManagerA5;
import in.co.ashclan.adpater.EventAdapter;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.ashclancalendar.data.Day;
import in.co.ashclan.ashclancalendar.widget.CollapsibleCalendar;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.fgtit.utils.ToastUtil;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.Constants;
import in.co.ashclan.utils.PreferenceUtils;
import in.co.ashclan.utils.Util;
import in.co.ashclan.utils.Utils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        BottomNavigationBar.OnTabSelectedListener,
        View.OnClickListener {

    DataBaseHelper dataBaseHelper;
    BottomNavigationBar bottomNavigationBar;
    FloatingActionButton fab;
    CollapsibleCalendar collapsibleCalendar;
    MemberFragment memberFragment;
    ListView listView;
    ArrayList<MemberPOJO> list = new ArrayList<MemberPOJO>();
    ArrayList<EventPOJO> eventList = new ArrayList<EventPOJO>();

    EventAdapter eventAdapter;

    int lastSelectedPosition = 0;
    DateFormat df;
    String selectDate, today;
    Day day;
    RelativeLayout removeLayout;

    Context mContext;
    Context context;// = HomeActivity.this;
    CardView cardViewAll, cardViewToday, cardViewMonth, cardViewYear;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        context = this;
        dataBaseHelper = new DataBaseHelper(mContext);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // Drawable d= getResources().getDrawable(R.drawable.ic_menu_2);//.setTint(getR.color.white);
        //toolbar.setOverflowIcon(d);
        inits();
        minitData();

//        Constants.setURL(mContext,PreferenceUtils.getSelectServer(mContext));

        df = new SimpleDateFormat("yyyy-MM-dd");
        list.addAll(dataBaseHelper.getAllMembers());
        eventList.addAll(dataBaseHelper.getAllEvent());

        //   Toast.makeText(HomeActivity.this,list.toString(),Toast.LENGTH_LONG).show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(HomeActivity.this, AttendanceActivity.class);
                intent.putExtra("type", "register");
                startActivity(intent);*/
                Intent intent = new Intent(HomeActivity.this, EventRegister.class);
                intent.putExtra("type", "register");
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
        navigationView.getMenu().getItem(1).setChecked(true);


        bottomNavigationBar.setTabSelectedListener(this);
        refresh();
        day = collapsibleCalendar.getSelectedDay();
        today = day.getYear() + "-" + day.getMonth() + "-" + day.getDay();

        calendar();

        eventAdapter = new EventAdapter(HomeActivity.this, eventList);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(eventAdapter);

        FPMatch.getInstance().InitMatch();

    }
    public void minitData() {
        try {

            Configuration config = getResources().getConfiguration();
            if (config.smallestScreenWidthDp >= 600) {

                Toast.makeText(HomeActivity.this, "Tablet", Toast.LENGTH_LONG).show();
                FPMatch.getInstance().InitMatch();
                if(SerialPortManager.getInstance().isOpen()){
                    try {
                        SerialPortManager.getInstance().openSerialPort();
                        ToastUtil.showToast(this, "Open Port��");

                    } catch (InvalidParameterException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!SerialPortManager.getInstance().isOpen()){
                    //ToastUtil.showToast(this, "Open Port Fail��");
                    return;
                }
            } else {
                Toast.makeText(HomeActivity.this, "Phone", Toast.LENGTH_LONG).show();
                FPMatch.getInstance().InitMatch();
                if(SerialPortManagerA5.getInstance().isOpen()){
                    try {
                        SerialPortManagerA5.getInstance().openSerialPort();
                        ToastUtil.showToast(this, "Open Port��");

                    } catch (InvalidParameterException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(!SerialPortManagerA5.getInstance().isOpen()){
                    //ToastUtil.showToast(this, "Open Port Fail��");
                    return;
                }
            }
            //**************************

        }catch(UnsatisfiedLinkError e){
            Log.e("--->",e.toString());
        }catch (Exception e){
        }
    }
    public void inits() {

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        collapsibleCalendar = findViewById(R.id.collapsibleCalendarView);
        removeLayout = (RelativeLayout) findViewById(R.id.removeLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cardViewAll = (CardView) findViewById(R.id.card_all);
        cardViewToday = (CardView) findViewById(R.id.card_today);
        cardViewMonth = (CardView) findViewById(R.id.card_month);
        cardViewYear = (CardView) findViewById(R.id.card_year);

        cardViewAll.setOnClickListener(this);
        cardViewToday.setOnClickListener(this);
        cardViewMonth.setOnClickListener(this);
        cardViewYear.setOnClickListener(this);
    }
    public void calendar() {
        System.out.println("Testing date " + collapsibleCalendar.getSelectedDay().getDay() + "/" + collapsibleCalendar.getSelectedDay().getMonth() + "/" + collapsibleCalendar.getSelectedDay().getYear());
        collapsibleCalendar.setCalendarListener(new CollapsibleCalendar.CalendarListener() {
            @Override
            public void onDaySelect() {
                day = collapsibleCalendar.getSelectedDay();

                Toast.makeText(HomeActivity.this, "onDaySelect Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay(), Toast.LENGTH_LONG).show();
                Log.e("onDaySelect:--> ", "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());

            }

            @Override
            public void onItemClick(View v) {
                day = collapsibleCalendar.getSelectedDay();
                selectDate = day.getYear() + "-" + (day.getMonth() + 1) + "-" + day.getDay();
                Date mDate, mStartDate, mEndDate;
                try {
                    ArrayList<EventPOJO> selectEventsList = new ArrayList<EventPOJO>();

                    mDate = df.parse(selectDate);
                    for (EventPOJO event : eventList) {
                        mStartDate = df.parse(event.getStartDate());
                        mEndDate = df.parse(event.getEnd_date());
                        if (isEventAvialibity(df, mStartDate, mEndDate, mDate)) {
                            selectEventsList.add(event);
                        }
                    }
                    cardViewAll.setCardBackgroundColor(getResources().getColor(R.color.blue2));
                    cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.blue2));
                    cardViewMonth.setCardBackgroundColor(getResources().getColor(R.color.blue2));
                    cardViewYear.setCardBackgroundColor(getResources().getColor(R.color.blue2));

                    eventAdapter = new EventAdapter(HomeActivity.this, selectEventsList);
                    eventAdapter.notifyDataSetChanged();
                    listView = (ListView) findViewById(R.id.list);
                    listView.setAdapter(eventAdapter);


                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onDataUpdate() {
                day = collapsibleCalendar.getSelectedDay();
                //               Toast.makeText(HomeActivity.this,"onDataUpdate Selected Day: "
                //                     + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay(),Toast.LENGTH_LONG).show();
                Log.e("onDataUpdate:--> ", "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());

            }

            @Override
            public void onMonthChange() {
                day = collapsibleCalendar.getSelectedDay();
                Log.e("onMonthChange:--> ", "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());

                //             Toast.makeText(HomeActivity.this,"onMonthChange Selected Day: "
                //                   + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay(),Toast.LENGTH_LONG).show();

            }

            @Override
            public void onWeekChange(int position) {
                day = collapsibleCalendar.getSelectedDay();
                //         Toast.makeText(HomeActivity.this,"postion: "+position,Toast.LENGTH_LONG).show();
                Log.e("onWeekChange:--> ", "Selected Day: "
                        + day.getYear() + "/" + (day.getMonth() + 1) + "/" + day.getDay());

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
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            PreferenceUtils.setSignIn(HomeActivity.this, false);
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        if (id == R.id.action_refresh) {
            progressBar.setVisibility(View.VISIBLE);
           // getAccessTokenGovNet(PreferenceUtils.getUrlLogin(this), PreferenceUtils.getAdminName(context), PreferenceUtils.getAdminPassword(context));

            GetAccessTokenTask aTask=new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                    PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext));
            aTask.execute();
        }

        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_member) {

            Intent intent = new Intent(HomeActivity.this, MemberListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_events) {
  /*
            Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(intent);
            finish();
*/
        } else if (id == R.id.nav_recording) {
            Intent intent = new Intent(HomeActivity.this, RecordingActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_setting) {
            Intent intenti = new Intent(HomeActivity.this,TestingActivity.class);
            startActivity(intenti);


        } else if(id==R.id.nav_upload){
            Intent intent = new Intent(mContext, UploadActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //BottNavigationBar
    @Override
    public void onTabSelected(int position) {
        lastSelectedPosition = position;
        //setMe
        setFragment(position);

    }
    @Override
    public void onTabUnselected(int position) {

    }
    @Override
    public void onTabReselected(int position) {
    }
    //View.OnclickListener
    @Override
    public void onClick(View view) {
        if (view == cardViewAll) {

            cardViewAll.setCardBackgroundColor(getResources().getColor(R.color.blue4));
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewMonth.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewYear.setCardBackgroundColor(getResources().getColor(R.color.blue2));

            eventAdapter = new EventAdapter(HomeActivity.this, eventList);
            eventAdapter.notifyDataSetChanged();
            listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(eventAdapter);
        }
        if (view == cardViewToday) {
            cardViewAll.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.blue4));
            cardViewMonth.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewYear.setCardBackgroundColor(getResources().getColor(R.color.blue2));

            Date mStartDate, mEndDate;
            try {
                ArrayList<EventPOJO> selectEventsList = new ArrayList<EventPOJO>();

                //   mDate = df.parse(selectDate);
                for (EventPOJO event : eventList) {
                    mStartDate = df.parse(event.getStartDate());
                    mEndDate = df.parse(event.getEnd_date());
                    if (isEventLive(df, mStartDate, mEndDate)) {
                        selectEventsList.add(event);
                    }
                }
                eventAdapter = new EventAdapter(HomeActivity.this, selectEventsList);
                eventAdapter.notifyDataSetChanged();
                listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(eventAdapter);


            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }


        }
        if (view == cardViewMonth) {
            cardViewAll.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewMonth.setCardBackgroundColor(getResources().getColor(R.color.blue4));
            cardViewYear.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            Date mStartDate, mEndDate;
            try {
                ArrayList<EventPOJO> selectEventsList = new ArrayList<EventPOJO>();

                //   mDate = df.parse(selectDate);
                for (EventPOJO event : eventList) {
                    mStartDate = df.parse(event.getStartDate());
                    mEndDate = df.parse(event.getEnd_date());
                    if (isEventMonth(df, mStartDate, mEndDate)) {
                        selectEventsList.add(event);
                    }
                }
                eventAdapter = new EventAdapter(HomeActivity.this, selectEventsList);
                eventAdapter.notifyDataSetChanged();
                listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(eventAdapter);


            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

        }
        if (view == cardViewYear) {
            cardViewAll.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewMonth.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewYear.setCardBackgroundColor(getResources().getColor(R.color.blue4));
            Date mStartDate, mEndDate;
            try {
                ArrayList<EventPOJO> selectEventsList = new ArrayList<EventPOJO>();

                //   mDate = df.parse(selectDate);
                for (EventPOJO event : eventList) {
                    mStartDate = df.parse(event.getStartDate());
                    mEndDate = df.parse(event.getEnd_date());
                    if (isEventYear(df, mStartDate, mEndDate)) {
                        selectEventsList.add(event);
                    }
                }
                eventAdapter = new EventAdapter(HomeActivity.this, selectEventsList);
                eventAdapter.notifyDataSetChanged();
                listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(eventAdapter);


            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }

        }
    }
    private void refresh() {
        bottomNavigationBar.clearAll();
        bottomNavigationBar.setFab(fab);
        setFragment(0);

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);


        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_overview, "OverView").setActiveColorResource(R.color.blue4))
                .addItem(new BottomNavigationItem(R.drawable.ic_attender, "Attender").setActiveColorResource(R.color.blue3))
                .addItem(new BottomNavigationItem(R.drawable.ic_report_1, "Reports").setActiveColorResource(R.color.blue2))
                .addItem(new BottomNavigationItem(R.drawable.ic_volunteers, "Volunteers").setActiveColorResource(R.color.blue3))
                .addItem(new BottomNavigationItem(R.drawable.ic_edit, "Edit").setActiveColorResource(R.color.blue4))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();
    }
    private void setFragment(int position) {

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
    public boolean isEventLive(DateFormat df, Date startDate, Date endDate) {
        //     Toast.makeText(HomeActivity.this,today,Toast.LENGTH_LONG).show();
        Date mDate = null;
        try {
            mDate = df.parse(today);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (startDate.compareTo(mDate) >= 0 && endDate.compareTo(mDate) <= 0) {
            return true;
        }
        return false;
    }
    public boolean isEventAvialibity(DateFormat df, Date startDate, Date endDate, Date onClickDate) {
        if (startDate.compareTo(onClickDate) >= 0 && endDate.compareTo(onClickDate) <= 0) {
            return true;
        }
        return false;
    }
    public boolean isEventMonth(DateFormat df, Date startDate, Date endDate) {
        Date mDate = null;
        try {
            mDate = df.parse(today);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if ((startDate.getMonth() == mDate.getMonth() && startDate.getYear() == mDate.getYear())
                || (endDate.getMonth() == mDate.getMonth() && endDate.getYear() == mDate.getYear())) {
            return true;
        }
        return false;
    }
    public boolean isEventYear(DateFormat df, Date startDate, Date endDate) {
        Date mDate = null;
        try {
            mDate = df.parse(today);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        if (startDate.getYear() == mDate.getYear() || endDate.getYear() == mDate.getYear()) {
            return true;
        }
        return false;
    }
    public void getAccessTokenVolley(String URL, String email, String password) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser_obj = new JSONParser();
                        String token = null;
                        try {
                            JSONObject object = (JSONObject) parser_obj.parse(response);
                            token = (String) object.get("result");
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //   Toast.makeText(SplashActivity.this,token,Toast.LENGTH_SHORT).show();
                        if (token.equalsIgnoreCase("wrong email or password.") || !PreferenceUtils.getSignIn(context)) {
                        } else {
                            PreferenceUtils.setToken(context, token);
                            /*
                            Intent intent = new Intent(SplashActivity.this, DataLoadingActivity.class);
                            intent.putExtra("token", "");
                            startActivity(intent);
                            finish();
                            */
                            //"http://52.172.221.235:8983/api/get_all_members"
                            memberDetailsVolley(PreferenceUtils.getUrlGetAllMembers(context), PreferenceUtils.getToken(context));
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        smr.addStringParam("email", email)
                .addStringParam("password", password);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);

    }
    public void memberDetailsVolley(String URL, String token) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(SplashActivity.this,response,Toast.LENGTH_SHORT).show();
                        dataBaseHelper.deleteAllMembers();
                        JSONParser parser = new JSONParser();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = (JSONArray) parser.parse(response);

                            for (int i = 0; i < jsonArray.size(); i++) {

                                MemberPOJO member = new MemberPOJO();

                                JSONObject object = (JSONObject) jsonArray.get(i);

                                member.setId(String.valueOf(object.get("id")));
                                member.setUserId(String.valueOf(object.get("user_id")));

                                member.setFirstName(isNull(object, "first_name"));
                                member.setMiddleName(isNull(object, "middle_name"));
                                member.setLastName(isNull(object, "last_name"));
                                member.setGender(isNull(object, "gender"));
                                member.setStatus(isNull(object, "status"));
                                member.setMaritalStatus(isNull(object, "marital_status"));
                                member.setDob(isNull(object, "dob", "0000-00-00"));
                                member.setHomePhone(isNull(object, "home_phone"));
                                member.setMobilePhone(isNull(object, "mobile_phone"));
                                member.setWorkPhone(isNull(object, "work_phone"));
                                member.setEmail(isNull(object, "email"));
                                member.setAddress(isNull(object, "address"));
                                member.setNotes(isNull(object, "notes"));
                                member.setRollNo(isNull(object, "No"));
                                member.setCreateAt(isNull(object, "created_at"));
                                member.setUpdateAt(isNull(object, "updated_at"));
                                member.setFingerPrint(isNull(object, "fingerprint"));


                                //remainber to do changes...
                                member.setPhotoURL(isNull(object, "photo", ""));

//                                memberList.add(member);
                                dataBaseHelper.insertMemberData(member);
                            }

                        } catch (ParseException e) {
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


        smr.addStringParam("token", token);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);
    }
    public void eventDetailsVolley(String URL, String token) {

        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        dataBaseHelper.deleteAllEvents();
                        JSONParser parser = new JSONParser();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = (JSONArray) parser.parse(response);

                            for (int i = 0; i < jsonArray.size(); i++) {

                                EventPOJO event = new EventPOJO();
                                JSONObject object = (JSONObject) jsonArray.get(i);
                                event.setId(String.valueOf(object.get("id")));
                                event.setBranchId(isNull(object, "branch_id"));
                                event.setUserId(isNull(object, "user_id"));
                                event.setParentId(isNull(object, "parent_id"));
                                event.setEventLocationId(isNull(object, "event_location_id"));
                                event.setEventCalenderId(isNull(object, "event_calendar_id"));
                                event.setName(isNull(object, "name"));
                                event.setCost(isNull(object, "cost"));
                                event.setAllDay(isNull(object, "all_day"));
                                event.setStartDate(isNull(object, "start_date"));
                                event.setStart_time(isNull(object, "start_time"));
                                event.setEnd_date(isNull(object, "end_date"));
                                event.setEnd_time(isNull(object, "end_time"));
                                event.setRecurring(isNull(object, "recurring"));
                                event.setRecurFrequency(isNull(object, "recur_frequency"));
                                event.setRecurStartDate(isNull(object, "recur_start_date"));
                                event.setRecurEndDate(isNull(object, "recur_end_date"));
                                event.setRecurNextDate(isNull(object, "recur_next_date"));
                                event.setRecurType(isNull(object, "recur_type"));
                                event.setCheckInType(isNull(object, "checkin_type"));
                                event.setTags(isNull(object, "tags"));
                                event.setIncludeCheckOut(isNull(object, "include_checkout"));
                                event.setFamilyCheckIn(isNull(object, "family_checkin"));
                                event.setFeatured_image(isNull(object, "featured_image"));
                                event.setGallery(isNull(object, "gallery"));
                                event.setFiles(isNull(object, "files"));
                                event.setYear(isNull(object, "year"));
                                event.setMonth(isNull(object, "month"));
                                event.setNotes(isNull(object, "notes"));
                                event.setCreatedAt(isNull(object, "created_at"));
                                event.setUpdatedAt(isNull(object, "updated_at"));
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
        smr.addStringParam("token", token);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);
    }
    public void getAccessTokenGovNet(String URL, String email, String password) {

        try {
            final String uploadId = UUID.randomUUID().toString();
            //Creating a multi part request

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("email", email)
                    .addParameter("password", password)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->", serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            Log.e("--->", serverResponse.getBodyAsString().toString());
                            JSONParser parser_obj = new JSONParser();
                            String token = null;
                            try {
                                JSONObject object = (JSONObject) parser_obj.parse(serverResponse.getBodyAsString().toString());
                                token = (String) object.get("result");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //   Toast.makeText(SplashActivity.this,token,Toast.LENGTH_SHORT).show();
                            if (token.equalsIgnoreCase("wrong email or password.") || !PreferenceUtils.getSignIn(context)) {
                            } else {
                                PreferenceUtils.setToken(context, token);
                            /*
                            Intent intent = new Intent(SplashActivity.this, DataLoadingActivity.class);
                            intent.putExtra("token", "");
                            startActivity(intent);
                            finish();
                            */
                                //"http://52.172.221.235:8983/api/get_all_members"
                                memberDetailsGovNet(PreferenceUtils.getUrlGetAllMembers(context), PreferenceUtils.getToken(context));
                            }

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->", "caln");
                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    public void memberDetailsGovNet(String URL, String token) {
        try {
            final String uploadId = UUID.randomUUID().toString();

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("token", token)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->", serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("---->", serverResponse.getBodyAsString().toString());
                            dataBaseHelper.deleteAllMembers();
                            JSONParser parser = new JSONParser();
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = (JSONArray) parser.parse(serverResponse.getBodyAsString().toString());

                                for (int i = 0; i < jsonArray.size(); i++) {

                                    MemberPOJO member = new MemberPOJO();

                                    JSONObject object = (JSONObject) jsonArray.get(i);

                                    member.setId(String.valueOf(object.get("id")));
                                    member.setUserId(String.valueOf(object.get("user_id")));

                                    member.setFirstName(isNull(object, "first_name"));
                                    member.setMiddleName(isNull(object, "middle_name"));
                                    member.setLastName(isNull(object, "last_name"));
                                    member.setGender(isNull(object, "gender"));
                                    member.setStatus(isNull(object, "status"));
                                    member.setMaritalStatus(isNull(object, "marital_status"));
                                    member.setDob(isNull(object, "dob", "0000-00-00"));
                                    member.setHomePhone(isNull(object, "home_phone"));
                                    member.setMobilePhone(isNull(object, "mobile_phone"));
                                    member.setWorkPhone(isNull(object, "work_phone"));
                                    member.setEmail(isNull(object, "email"));
                                    member.setAddress(isNull(object, "address"));
                                    member.setNotes(isNull(object, "notes"));
                                    member.setRollNo(isNull(object, "No"));
                                    member.setCreateAt(isNull(object, "created_at"));
                                    member.setUpdateAt(isNull(object, "updated_at"));
                                    member.setFingerPrint(isNull(object, "fingerprint"));


                                    //remainber to do changes...
                                    member.setPhotoURL(isNull(object, "photo", ""));

//                                memberList.add(member);
                                    dataBaseHelper.insertMemberData(member);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //"http://52.172.221.235:8983/api/get_all_events"
                            eventDetailsGovNet(PreferenceUtils.getUrlGetAllEvents(context), PreferenceUtils.getToken(context));

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->", "caln");
                        }
                    })
                    .startUpload();//Starting the upload

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    public void eventDetailsGovNet(String URL, String token) {
        try {
            final String uploadId = UUID.randomUUID().toString();

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("token", token)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->", serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("---->", serverResponse.getBodyAsString().toString());
                            dataBaseHelper.deleteAllEvents();
                            JSONParser parser = new JSONParser();
                            JSONArray jsonArray = null;
                            try {
                                jsonArray = (JSONArray) parser.parse(serverResponse.getBodyAsString().toString());

                                for (int i = 0; i < jsonArray.size(); i++) {

                                    EventPOJO event = new EventPOJO();
                                    JSONObject object = (JSONObject) jsonArray.get(i);
                                    event.setId(String.valueOf(object.get("id")));
                                    event.setBranchId(isNull(object, "branch_id"));
                                    event.setUserId(isNull(object, "user_id"));
                                    event.setParentId(isNull(object, "parent_id"));
                                    event.setEventLocationId(isNull(object, "event_location_id"));
                                    event.setEventCalenderId(isNull(object, "event_calendar_id"));
                                    event.setName(isNull(object, "name"));
                                    event.setCost(isNull(object, "cost"));
                                    event.setAllDay(isNull(object, "all_day"));
                                    event.setStartDate(isNull(object, "start_date"));
                                    event.setStart_time(isNull(object, "start_time"));
                                    event.setEnd_date(isNull(object, "end_date"));
                                    event.setEnd_time(isNull(object, "end_time"));
                                    event.setRecurring(isNull(object, "recurring"));
                                    event.setRecurFrequency(isNull(object, "recur_frequency"));
                                    event.setRecurStartDate(isNull(object, "recur_start_date"));
                                    event.setRecurEndDate(isNull(object, "recur_end_date"));
                                    event.setRecurNextDate(isNull(object, "recur_next_date"));
                                    event.setRecurType(isNull(object, "recur_type"));
                                    event.setCheckInType(isNull(object, "checkin_type"));
                                    event.setTags(isNull(object, "tags"));
                                    event.setIncludeCheckOut(isNull(object, "include_checkout"));
                                    event.setFamilyCheckIn(isNull(object, "family_checkin"));
                                    event.setFeatured_image(isNull(object, "featured_image"));
                                    event.setGallery(isNull(object, "gallery"));
                                    event.setFiles(isNull(object, "files"));
                                    event.setYear(isNull(object, "year"));
                                    event.setMonth(isNull(object, "month"));
                                    event.setNotes(isNull(object, "notes"));
                                    event.setCreatedAt(isNull(object, "created_at"));
                                    event.setUpdatedAt(isNull(object, "updated_at"));
                                    eventList.add(event);
                                    dataBaseHelper.insertEventData(event);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //           eventAdapter= new EventAdapter(mContext,eventList);
                            //         eventAdapter.notifyDataSetChanged();
                            //       listView.setAdapter(eventAdapter);
                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {
                            Log.e("--->", "caln");
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
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                progressBar.setVisibility(View.GONE);
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
                    member.setFingerPrint2(Utils.isNull(object,"fingerprint2"));

                    //remainber to do changes...
                    member.setPhotoURL(Utils.isNull(object,"photo",""));
                    Log.e("--->",member.toString());
                    dataBaseHelper.insertMemberData(member);
                }

            //"https://bwc.pentecostchurch.org/api/get_all_events"
            //"http://52.172.221.235:8983/api/get_all_events"
            GetAllEventsTask eTask = new GetAllEventsTask(mContext,PreferenceUtils.getUrlGetAllEvents(mContext),token);
            eTask.execute();

        } catch (Exception e) {
            Log.e("--->", e.toString());
            Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
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
                Log.e("--->THIS!T", output);
                dataBaseHelper.deleteAllEvents();
                JSONParser parser = new JSONParser();
                JSONArray jsonArray =(JSONArray)parser.parse(output);

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
                }
            eventList.clear();
            eventList.addAll(dataBaseHelper.getAllEvent());

            cardViewAll.setCardBackgroundColor(getResources().getColor(R.color.blue4));
            cardViewToday.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewMonth.setCardBackgroundColor(getResources().getColor(R.color.blue2));
            cardViewYear.setCardBackgroundColor(getResources().getColor(R.color.blue2));

            eventAdapter = new EventAdapter(HomeActivity.this, eventList);
            eventAdapter.notifyDataSetChanged();
            listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(eventAdapter);
            progressBar.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e("--->", e.toString());
            Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
            e.printStackTrace();
            progressBar.setVisibility(View.GONE);
            }
    }
        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);
        }
    }
}