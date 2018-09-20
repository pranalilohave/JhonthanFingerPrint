package in.co.ashclan.fingerprint;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.ashclan.adpater.AttenderAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.model.CalendarPOJO;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.LocationPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class EventDetailsActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener{
    BottomNavigationBar bottomNavigationBar;
    ViewPager viewPager;
    int lastSelectedPosition = 0;
    Context mContext;
    MyViewPagerAdapter myViewPagerAdapter;
    MyReportViewPagerAdapter myReportViewPagerAdapter;
    MyAttenderViewPagerAdapter myAttenderViewPagerAdapter;
    MyEditViewPagerAdapter myEditViewPagerAdapter;
    private int[] layouts;
    DataBaseHelper dataBaseHelper;
    EventPOJO eventDetails;

    String total = null;
    public Menu menu;
    MenuItem Item;
    TextView txtTotal;
    ArrayList<AttenderPOJO> DetailsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        mContext= this;
        init();
        refresh();

        String event_id = eventDetails.getId().toString();
        DetailsList.addAll(dataBaseHelper.getAllAttender(event_id));
        total = "Total Attendees = " + DetailsList.size();

       // txtTotal.setText(total);

    }

    public void init(){
        eventDetails = (EventPOJO)getIntent().getSerializableExtra("event_details");
        layouts = new int[]{
                R.layout.event_pager_overview,
        };
        viewPager = (ViewPager)findViewById(R.id.events_view_pager);
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);
        dataBaseHelper = new DataBaseHelper(EventDetailsActivity.this);
        Item = (MenuItem)findViewById(R.id.action_total);
        txtTotal = (TextView)findViewById(R.id.txtTotal_attendance);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.total_attenders, menu);
         updateMenuTitles(menu);
         return true;
    }

    private void updateMenuTitles(Menu menu) {
        MenuItem bedMenuItem = menu.findItem(R.id.action_total);
        bedMenuItem.setTitle(total);
    }

    private void refresh(){
        bottomNavigationBar.clearAll();

       setViewPager(0);

        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_DEFAULT);


        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_overview, "OverView").setActiveColorResource(R.color.blue4))
                .addItem(new BottomNavigationItem(R.drawable.ic_attender, "Attendees").setActiveColorResource(R.color.blue3))
                .addItem(new BottomNavigationItem(R.drawable.ic_report_1, "Reports").setActiveColorResource(R.color.blue2))
                .addItem(new BottomNavigationItem(R.drawable.ic_volunteers, "Volunteers").setActiveColorResource(R.color.blue3))
                .addItem(new BottomNavigationItem(R.drawable.ic_edit, "Edit").setActiveColorResource(R.color.blue4))
                .setFirstSelectedPosition(lastSelectedPosition)
                .initialise();

    }
    private void setViewPager(int index){

        switch (index){
            case 0:
                myViewPagerAdapter = new MyViewPagerAdapter(mContext,eventDetails);
                viewPager.setAdapter(myViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 1:
                myAttenderViewPagerAdapter = new MyAttenderViewPagerAdapter(mContext);
                viewPager.setAdapter(myAttenderViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 2:
                myReportViewPagerAdapter = new MyReportViewPagerAdapter(mContext);
                viewPager.setAdapter(myReportViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 4:
                myEditViewPagerAdapter = new MyEditViewPagerAdapter(mContext);
                viewPager.setAdapter(myEditViewPagerAdapter);
                viewPager.getAdapter().notifyDataSetChanged();
                break;
            case 3:
                break;
        }

    }
    private void viewPagerOverView(){
    }
    @Override
    public void onClick(View view) {

    }
    @Override
    public void onTabSelected(int position) {
       // Toast.makeText(mContext,position+"",Toast.LENGTH_LONG).show();

        lastSelectedPosition = position;
        setViewPager(position);
        Log.e("--->",String.valueOf(position));
    }
    @Override
    public void onTabUnselected(int position) {
        Log.e("--->",String.valueOf(position));
    }
    @Override
    public void onTabReselected(int position) {
        Log.e("--->",String.valueOf(position));
    }
    public int getPosition(){
        return viewPager.getCurrentItem();
    }

    //**********************************Overview ViewPagerAdapter ******************************//
    public class MyViewPagerAdapter extends PagerAdapter{


        private LayoutInflater inflater;
        Context vContext;
        EventPOJO event;
        public MyViewPagerAdapter(){

        }
        public MyViewPagerAdapter(Context vContext,EventPOJO event){
            this.vContext = vContext;
            this.event = event;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            switch (position){
                case 0:
                    view = inflater.inflate(layouts[position],container,false);
                    TextView overviewEndDate,overviewName,overviewDate,overviewCost,overviewCalender,overviewLocation,overviewNotes,overviewCreatedAt,overviewFeaturedImage;
                    overviewName=(TextView)view.findViewById(R.id.overview_event_name);
                    overviewDate=(TextView)view.findViewById(R.id.overview_event_date);
                    overviewEndDate=(TextView)view.findViewById(R.id.overview_event_enddate);
                    overviewCalender=(TextView)view.findViewById(R.id.overview_event_calender);
                    overviewCost=(TextView)view.findViewById(R.id.overview_event_cost);
                    overviewLocation=(TextView)view.findViewById(R.id.overview_event_location);
                    overviewFeaturedImage = (TextView)view.findViewById(R.id.overview_event_featured_image);
                    overviewNotes=(TextView)view.findViewById(R.id.overview_event_notes);
                    overviewCreatedAt=(TextView)view.findViewById(R.id.overview_event_created_at);

                    overviewName.setText(event.getName());
                    //more word.....
                    overviewDate.setText("StartDate : "+event.getStartDate()+"("+event.getStart_time()+")");
                    overviewEndDate.setText("EndDate : "+event.getEnd_date()+"("+event.getEnd_time()+")");
                    overviewCalender.setText(new DataBaseHelper(mContext).getCalendarName(event.getEventCalenderId()));
                    overviewCost.setText(event.getCost());
                    overviewLocation.setText(new DataBaseHelper(mContext).getLocationName(event.getEventLocationId()));
                    overviewNotes.setText(event.getNotes());
                    overviewFeaturedImage.setText("");
                    overviewCreatedAt.setText(event.getCreatedAt());
                    container.addView(view);
                    break;
                case 1:
                    view = inflater.inflate(layouts[position],container,false);
                    container.addView(view);



                    break;
                case 2:

                    view = inflater.inflate(layouts[position],container,false);
                    container.addView(view);

                    break;

            }

            //View view = inflater.inflate(R.layout.event_pager_overview,container,false);
            //container.addView(view);
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
    //**********************************ATTENDER ViewPagerAdapter ******************************//
    public class MyAttenderViewPagerAdapter extends PagerAdapter{
        private LayoutInflater inflater;
        Context vContext;
        ArrayList<AttenderPOJO> DetailsList = new ArrayList<>();
        AttenderPOJO attenderPOJO;
        TextView txtTotalAttendance;

        public MyAttenderViewPagerAdapter(Context vContext,AttenderPOJO attenderPOJO){
            this.vContext = vContext;
            this.attenderPOJO = attenderPOJO;
        }
        public MyAttenderViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            view = inflater.inflate(R.layout.fragment_attender,container,false);
            container.addView(view);
            ListView listView = (ListView)view.findViewById(R.id.list_attender);
            txtTotalAttendance = (TextView)view.findViewById(R.id.txtTotal_attendance);
            String event_id = eventDetails.getId().toString();
            DetailsList.addAll(dataBaseHelper.getAllAttender(event_id));

            AttenderAdapter attenderAdapter = new AttenderAdapter(EventDetailsActivity.this,DetailsList);
            listView.setAdapter(attenderAdapter);

            txtTotalAttendance.setText("Total Attendees = " + DetailsList.size());
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
    //**********************************REPORTS ViewPagerAdapter ******************************//
    public class MyReportViewPagerAdapter extends PagerAdapter{

        private LayoutInflater inflater;
        Context vContext;
        ArrayList<AttenderPOJO> DetailsList = new ArrayList<>();
        AttenderPOJO attenderPOJO;
        BarChart barChart_gender,barChart_age,barChart_martialstatus,barChart_status;

        public MyReportViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }


        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view =null;
            view = inflater.inflate(R.layout.fragment_report,container,false);
            container.addView(view);

            String event_id = eventDetails.getId().toString();
            DetailsList.addAll(dataBaseHelper.getAllAttender(event_id));

            barChart_gender = (BarChart) view.findViewById(R.id.barchart_gender);
            barChart_age = (BarChart) view.findViewById(R.id.barchart_age);
            barChart_martialstatus = (BarChart) view.findViewById(R.id.barchart_maritalstatus);
            barChart_status = (BarChart) view.findViewById(R.id.barchart_status);


            setBarcharttgender(event_id);
            setBarchartAge(event_id);
            setBarcharMaritalStatus(event_id);
            setBarchartStatus(event_id);

//        HorizontalBarChart barChart = (HorizontalBarChart) findViewById(R.id.barchart);
//
//        // create BarEntry for Bar Group 1
//        ArrayList<BarEntry> bargroup1 = new ArrayList<>();
//        bargroup1.add(new BarEntry(8f, 0));
//        bargroup1.add(new BarEntry(2f, 1));
//        bargroup1.add(new BarEntry(5f, 2));
//        bargroup1.add(new BarEntry(20f, 3));
//        bargroup1.add(new BarEntry(15f, 4));
//        bargroup1.add(new BarEntry(19f, 5));
//
//        // create BarEntry for Bar Group 1
//        ArrayList<BarEntry> bargroup2 = new ArrayList<>();
//        bargroup2.add(new BarEntry(6f, 0));
//        bargroup2.add(new BarEntry(10f, 1));
//        bargroup2.add(new BarEntry(5f, 2));
//        bargroup2.add(new BarEntry(25f, 3));
//        bargroup2.add(new BarEntry(4f, 4));
//        bargroup2.add(new BarEntry(17f, 5));
//
//        BarDataSet barDataSet1 = new BarDataSet(bargroup1, "Bar Group 1");  // creating dataset for group1
//
//        //barDataSet1.setColor(Color.rgb(0, 155, 0));
//        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        BarDataSet barDataSet2 = new BarDataSet(bargroup2, "Brand 2"); // creating dataset for group1
//        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);
//
//        ArrayList<String> labels = new ArrayList<String>();
//        labels.add("2016");
//        labels.add("2015");
//        labels.add("2014");
//        labels.add("2013");
//        labels.add("2012");
//        labels.add("2011");
//
//        ArrayList<BarDataSet> dataSets = new ArrayList<>();  // combined all dataset into an arraylist
//        dataSets.add(barDataSet1);
//        dataSets.add(barDataSet2);
//        BarData data = new BarData(labels, dataSets); // initialize the Bardata with argument labels and dataSet
//        barChart.setData(data);

//        barChart.setDescription("Set Bar Chart Description");  // set the description
//
//        barChart.animateY(5000);


            return view;
        }

        private void setBarchartStatus(String Id) {


            float attenders,visitors,members,Inactive,unknown;

            attenders = (Integer)dataBaseHelper.AttendersCount(Id);
            visitors = (Integer)dataBaseHelper.VisitorsCount(Id);
            members = (Integer)dataBaseHelper.membersCount(Id);
            Inactive = (Integer)dataBaseHelper.InactiveCount(Id);
            unknown = (Integer)dataBaseHelper.unknownStatusCount(Id);

            ArrayList<BarEntry> entries_status = new ArrayList<>();
            entries_status.add(new BarEntry(attenders, 0));
            entries_status.add(new BarEntry(visitors, 1));
            entries_status.add(new BarEntry(members, 2));
            entries_status.add(new BarEntry(Inactive, 3));
            entries_status.add(new BarEntry(unknown, 4));



            BarDataSet bardataset_Status = new BarDataSet(entries_status, "Status");

            ArrayList<String> labels_status = new ArrayList<String>();
            labels_status.add("Attenders");
            labels_status.add("Visitors");
            labels_status.add("Member");
            labels_status.add("Inactive");
            labels_status.add("Unknown");

            BarData data_age = new BarData(labels_status, bardataset_Status);
            barChart_status.setData(data_age); // set the data and list of lables into chart
            barChart_status.setDescription("PentaCost - BWC");  // set the description
            // bardataset_age.setColors(ColorTemplate.COLORFUL_COLORS);
            barChart_status.animateY(2000);

        }

        private void setBarcharMaritalStatus(String Id) {

            float married,engaged,separated,widowed,divorced,single,unknown;

            married = (Integer)dataBaseHelper.MarriedCount(Id);
            engaged = (Integer)dataBaseHelper.EngagedCount(Id);
            separated = (Integer)dataBaseHelper.SeparatedCount(Id);
            widowed = (Integer)dataBaseHelper.widowedCount(Id);
            divorced = (Integer)dataBaseHelper.DivorcedCount(Id);
            single = (Integer)dataBaseHelper.SingleCount(Id);
            unknown = (Integer)dataBaseHelper.UnknownMaritalCount(Id);


            ArrayList<BarEntry> entries_mstatus = new ArrayList<>();
            entries_mstatus.add(new BarEntry(married, 0));
            entries_mstatus.add(new BarEntry(engaged, 1));
            entries_mstatus.add(new BarEntry(separated, 2));
            entries_mstatus.add(new BarEntry(widowed, 3));
            entries_mstatus.add(new BarEntry(divorced, 4));
            entries_mstatus.add(new BarEntry(single, 5));
            entries_mstatus.add(new BarEntry(unknown, 6));


            BarDataSet bardataset_martialStatus = new BarDataSet(entries_mstatus, "Marital Status");

            ArrayList<String> labels_Mstatus = new ArrayList<String>();
            labels_Mstatus.add("Married");
            labels_Mstatus.add("Engaged");
            labels_Mstatus.add("Separated");
            labels_Mstatus.add("Widowed");
            labels_Mstatus.add("Divorced");
            labels_Mstatus.add("Single");
            labels_Mstatus.add("Child");



            BarData data_age = new BarData(labels_Mstatus, bardataset_martialStatus);
            barChart_martialstatus.setData(data_age); // set the data and list of lables into chart
            barChart_martialstatus.setDescription("PentaCost - BWC");  // set the description
            // bardataset_age.setColors(ColorTemplate.COLORFUL_COLORS);
            barChart_martialstatus.animateY(2000);

        }

        private void setBarchartAge(String Id) {

            float under6,to612,to1318,to1929,to3049,to5064,to6579,to80plus,unknown;

            float[] myage = dataBaseHelper.age(Id);
            ArrayList<BarEntry> entries_age = new ArrayList<>();

            for(int i = 0;i<myage.length;i++)
            {
                Log.e("setBarchart",myage[i]+"");
            }


            entries_age.add(new BarEntry(myage[0], 0));
            entries_age.add(new BarEntry(myage[1], 1));
            entries_age.add(new BarEntry(myage[2], 2));
            entries_age.add(new BarEntry(myage[3], 3));
            entries_age.add(new BarEntry(myage[4], 4));
            entries_age.add(new BarEntry(myage[5], 5));
            entries_age.add(new BarEntry(myage[6], 6));
            entries_age.add(new BarEntry(myage[7], 7));
            entries_age.add(new BarEntry(myage[8], 8));


            BarDataSet bardataset_age = new BarDataSet(entries_age, "Age");

            ArrayList<String> labels_age = new ArrayList<String>();
            labels_age.add("under6");
            labels_age.add("6-12");
            labels_age.add("13-18");
            labels_age.add("19-29");
            labels_age.add("30-49");
            labels_age.add("50-64");
            labels_age.add("65-79");
            labels_age.add("80+");
            labels_age.add("Unknown");


            BarData data_age = new BarData(labels_age, bardataset_age);
            barChart_age.setData(data_age); // set the data and list of lables into chart
            barChart_age.setDescription("PentaCost - BWC");  // set the description
           // bardataset_age.setColors(ColorTemplate.COLORFUL_COLORS);
            barChart_age.animateY(2000);

        }

        private void setBarcharttgender(String Id) {

            float male,female,unknown;

            male = (Integer)dataBaseHelper.maleCount(Id);
            female = (Integer)dataBaseHelper.femaleCount(Id);
            unknown = (Integer)dataBaseHelper.UnknownGenderCount(Id);

            ArrayList<BarEntry> entries_gender = new ArrayList<>();
            entries_gender.add(new BarEntry(male, 0));
            entries_gender.add(new BarEntry(female, 1));
            entries_gender.add(new BarEntry(unknown, 2));


            BarDataSet bardataset = new BarDataSet(entries_gender, "Gender");

            ArrayList<String> labels_gender = new ArrayList<String>();
            labels_gender.add("Male");
            labels_gender.add("Female");
            labels_gender.add("Unknown");


            BarData data = new BarData(labels_gender, bardataset);
            barChart_gender.setData(data); // set the data and list of lables into chart
            barChart_gender.setDescription("PentaCost - BWC");  // set the description
           // bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
            barChart_gender.animateY(2000);

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
    //**********************************VOLUNTEERS ViewPagerAdapter ****************************//
    public class MyVolunteersViewPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }
    }
    //**********************************EDIT ViewPagerAdapter ********************************//
    public class MyEditViewPagerAdapter extends PagerAdapter implements View.OnClickListener{

        private LayoutInflater inflater;
        Context vContext;
        ArrayList<AttenderPOJO> DetailsList = new ArrayList<>();
        AttenderPOJO attenderPOJO;
        EditText edtEventName,edtStartDate,edtStartTime,edtEndDate,edtEndTime,edtCost,edtLongitude,edtLatitude,edtNote;
        EditText edtRecurFrequency,edtRecurStartFrom,edtRecurEndFrom,edtLocation,edtCalendar;
        CheckBox chkAllDayEvent,chkRecurringEvent;
        MaterialSpinner spnCalendar,spnLocation,spnRecurType,spnColor;
        ImageView img_btnAddCalendar,img_btnAddLocation;
        Button btnSubmit;
        private ProgressBar progressBar;
        LinearLayout ReccuringLayout;
        private int mYear, mMonth, mDay;
        DataBaseHelper dataBaseHelper;
        private ProgressDialog nDialog;
        ArrayList<CalendarPOJO> calendarArrayList = new ArrayList<CalendarPOJO>();
        ArrayList<LocationPOJO> locationArrayList = new ArrayList<LocationPOJO>();
        EventPOJO eventPOJO = new EventPOJO();
        CalendarPOJO calendarPOJO = new CalendarPOJO();
        LocationPOJO locationPOJO = new LocationPOJO();
        LayoutInflater layoutInflater;
        String event_id;

        public MyEditViewPagerAdapter(Context vContext){
            this.vContext = vContext;
        }

        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater = getLayoutInflater();
            View view =null;
            view = inflater.inflate(R.layout.activity_event_register,container,false);
            container.addView(view);

            event_id = eventDetails.getId().toString();
            mInit(view);

            dataBaseHelper = new DataBaseHelper(mContext);
            loadCalendarSpinner();
            loadLocationSpinner();

            btnSubmit.setOnClickListener(this);
            img_btnAddLocation.setOnClickListener(this);
            img_btnAddCalendar.setOnClickListener(this);
            edtStartDate.setOnClickListener(this);
            edtEndDate.setOnClickListener(this);
            edtRecurStartFrom.setOnClickListener(this);
            edtRecurEndFrom.setOnClickListener(this);
            edtStartTime.setOnClickListener(this);
            edtEndTime.setOnClickListener(this);


            chkRecurringEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(chkRecurringEvent.isChecked())
                    {
                        ReccuringLayout.setVisibility(View.VISIBLE);
                    }else
                    {
                        ReccuringLayout.setVisibility(View.GONE);
                    }
                }
            });

            eventPOJO = (EventPOJO) dataBaseHelper.getAllEvent(event_id);

            setAllEventData();

            return view;
        }

        private void mInit(View v) {

            edtEventName        = (EditText)v.findViewById(R.id.edt_event_name);
            edtStartDate        = (EditText)v.findViewById(R.id.edt_start_date);
            edtStartTime        = (EditText)v.findViewById(R.id.edt_start_time);
            edtEndDate          = (EditText)v.findViewById(R.id.edt_end_date);
            edtEndTime          = (EditText)v.findViewById(R.id.edt_end_time);
            edtCost             = (EditText)v.findViewById(R.id.edt_cost);
            edtLongitude        = (EditText)v.findViewById(R.id.edt_longitude);
            edtLatitude         = (EditText)v.findViewById(R.id.edt_latitude);
            edtNote             = (EditText)v.findViewById(R.id.edt_note);
            edtRecurFrequency   = (EditText)v.findViewById(R.id.edt_recur_frequency);
            edtRecurStartFrom   = (EditText)v.findViewById(R.id.recur_start_from);
            edtRecurEndFrom     = (EditText)v.findViewById(R.id.recurr_end_from);
            chkAllDayEvent      = (CheckBox)v.findViewById(R.id.chk_allday_event);
            chkRecurringEvent   = (CheckBox)v.findViewById(R.id.chk_recurring_event);
            spnCalendar         = (MaterialSpinner) v.findViewById(R.id.spn_calendar);
            spnLocation         = (MaterialSpinner) v.findViewById(R.id.spn_location);
            spnRecurType        = (MaterialSpinner) v.findViewById(R.id.spn_RecurType);
            img_btnAddCalendar  = (ImageView)v.findViewById(R.id.img_btnCalendarAdd);
            img_btnAddLocation  = (ImageView)v.findViewById(R.id.img_btnLocationAdd);
            btnSubmit           = (Button)v.findViewById(R.id.btn_Event_submit);
            btnSubmit.setText("Update");
            ReccuringLayout     = (LinearLayout)v.findViewById(R.id.linear_recurring);
            progressBar         = (ProgressBar)v.findViewById(R.id.progress_bar_member_register);

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

        @Override
        public void onClick(View view) {
            if(view == btnSubmit) {
                if(!utilsCheck()){

                    getAllEventData();

                }
            }
            if(view == img_btnAddCalendar){
                showDialogAddCalendar();
            }
            if (view == img_btnAddLocation){
                showDialogAddLocation();
            }
            if(view == edtStartDate){
                dateDialog(edtStartDate);
            }
            if(view== edtEndDate){
                dateDialog(edtEndDate);
            }
            if (view==edtRecurStartFrom){
                dateDialog(edtRecurStartFrom);
            }
            if (view == edtRecurEndFrom){
                dateDialog(edtRecurEndFrom);
            }
            if(view == edtStartTime) {
                timeDialog(edtStartTime);
            }
            if(view == edtEndTime){
                timeDialog(edtEndTime);
            }

        }
        public void setAllEventData(){

            // event.setId(String.valueOf(object.get("id")));
 /*       eventPOJO.setBranchId(eventPOJO.getBranchId().toString());
        eventPOJO.setUserId(eventPOJO.getUserId().toString());
        eventPOJO.setParentId(eventPOJO.getParentId().toString());*/


            getCalendar(eventPOJO.getEventCalenderId());
            getLocation(eventPOJO.getEventLocationId());

            edtEventName.setText(eventPOJO.getName().toString());
            edtCost.setText(eventPOJO.getCost().toString());

            if(eventPOJO.getAllDay().equals("1"))
            {
                chkAllDayEvent.setChecked(true);
                chkRecurringEvent.setChecked(false);

            }else if(eventPOJO.getRecurring().equals("1")&&eventPOJO.getAllDay().equals("1"))
            {
                chkAllDayEvent.setChecked(true);
                chkRecurringEvent.setChecked(true);

                edtRecurFrequency.setText(eventPOJO.getRecurFrequency().toString());
                //spinner
                getReccuring(eventPOJO.getRecurType().toString());
                edtRecurStartFrom.setText(eventPOJO.getRecurStartDate().toString());
                edtRecurEndFrom.setText(eventPOJO.getRecurEndDate().toString());


            }else if(eventPOJO.getRecurring().equals("1"))
            {
                chkAllDayEvent.setChecked(true);
                chkRecurringEvent.setChecked(false);

                edtRecurFrequency.setText(eventPOJO.getRecurFrequency().toString());
                //spinner
                getReccuring(eventPOJO.getRecurType().toString());
                edtRecurStartFrom.setText(eventPOJO.getRecurStartDate().toString());
                edtRecurEndFrom.setText(eventPOJO.getRecurEndDate().toString());

            }else
            {
                eventPOJO.setAllDay("0");
                eventPOJO.setRecurring("0");
            }

            edtStartDate.setText(eventPOJO.getStartDate().toString());
            edtStartTime.setText(eventPOJO.getStart_time().toString());
            edtEndDate.setText(eventPOJO.getEnd_date().toString());
            edtEndTime.setText(eventPOJO.getEnd_time().toString());
            edtNote.setText(eventPOJO.getNotes().toString());
            //event.setCreatedAt(isNull(object,"created_at"));
            // event.setUpdatedAt(isNull(object,"updated_at"));
            Log.e("setevent--->",eventPOJO.toString());
        }
        public void getAllEventData(){

            EventPOJO eventPOJO = new EventPOJO();

            // event.setId(String.valueOf(object.get("id")));
 /*       eventPOJO.setBranchId(eventPOJO.getBranchId().toString());
        eventPOJO.setUserId(eventPOJO.getUserId().toString());
        eventPOJO.setParentId(eventPOJO.getParentId().toString());*/


            eventPOJO.setEventLocationId(dataBaseHelper.getLocationId(spnLocation.getSelectedItem().toString().trim()));
            eventPOJO.setEventCalenderId(dataBaseHelper.getCalendarId(spnCalendar.getSelectedItem().toString().trim()));

            eventPOJO.setName(edtEventName.getText().toString());
            eventPOJO.setCost(edtCost.getText().toString());

            if(chkAllDayEvent.isChecked())
            {
                eventPOJO.setAllDay("1");
                eventPOJO.setRecurring("0");
            }else if(chkRecurringEvent.isChecked() && chkAllDayEvent.isChecked())
            {
                eventPOJO.setAllDay("1");
                eventPOJO.setRecurring("1");

                if (TextUtils.isEmpty(edtRecurFrequency.getText())){
                    edtRecurFrequency.setError("Please Enter Frequency");
                    return;
                }
                else if(spnRecurType.getSelectedItemPosition()==0){
                    spnRecurType.setError("Please Select Recurring Type");
                }
                else if(TextUtils.isEmpty(edtRecurStartFrom.getText())&&!isValidDate(edtRecurStartFrom.getText().toString())){
                    edtRecurFrequency.setError("Please Select Start Date");
                    return;
                }
                else if(TextUtils.isEmpty(edtRecurStartFrom.getText())&&!isValidDate(edtRecurStartFrom.getText().toString())){
                    edtRecurStartFrom.setError("Please Select End Date");
                    return;
                }
                else{
                    eventPOJO.setRecurFrequency(edtRecurFrequency.getText().toString());
                    eventPOJO.setRecurStartDate(edtRecurStartFrom.getText().toString());
                    eventPOJO.setRecurEndDate(edtRecurEndFrom.getText().toString());
                    eventPOJO.setRecurType(spnRecurType.getSelectedItem().toString().trim());
                }

            }else if(chkRecurringEvent.isChecked())
            {
                eventPOJO.setAllDay("0");
                eventPOJO.setRecurring("1");

                if (TextUtils.isEmpty(edtRecurFrequency.getText())){
                    edtRecurFrequency.setError("Please Enter Frequency");
                    return;
                }
                else if(spnRecurType.getSelectedItemPosition()==0){
                    spnRecurType.setError("Please Select Recurring Type");
                }
                else if(TextUtils.isEmpty(edtRecurStartFrom.getText())&&!isValidDate(edtRecurStartFrom.getText().toString())){
                    edtRecurStartFrom.setError("Please Select Start Date");
                    return;
                }
                else if(TextUtils.isEmpty(edtRecurEndFrom.getText())&&!isValidDate(edtRecurEndFrom.getText().toString())){
                    edtRecurEndFrom.setError("Please Select End Date");
                    return;
                }
                else{
                    eventPOJO.setRecurFrequency(edtRecurFrequency.getText().toString());
                    eventPOJO.setRecurStartDate(edtRecurStartFrom.getText().toString());
                    eventPOJO.setRecurEndDate(edtRecurEndFrom.getText().toString());
                    eventPOJO.setRecurType(spnRecurType.getSelectedItem().toString().trim());
                }

            }else
            {
                eventPOJO.setAllDay("0");
                eventPOJO.setRecurring("0");
            }

            eventPOJO.setStartDate(edtStartDate.getText().toString());
            eventPOJO.setStart_time(edtStartTime.getText().toString());
            eventPOJO.setEnd_date(edtEndDate.getText().toString());
            eventPOJO.setEnd_time(edtEndTime.getText().toString());
            eventPOJO.setCheckInType("everyone");

            //event.setRecurFrequency(edtRecurFrequency.getText().toString());
            //event.setRecurStartDate(edtRecurStartFrom.getText().toString());
            //event.setRecurEndDate(edtRecurEndFrom.getText().toString());
            //event.setRecurNextDate();
            //event.setRecurType(spnRecurType.getSelectedItem().toString().trim());
            // event.setTags(edtNote.getText().toString());
            // event.setIncludeCheckOut(isNull(object,"include_checkout"));
            // event.setFamilyCheckIn(isNull(object,"family_checkin"));//event.setFeatured_image(isNull(object,"featured_image"));
            // event.setGallery(isNull(object,"gallery"));
            // event.setFiles(isNull(object,"files"));
            // event.setYear(isNull(object,"year"));
            // event.setMonth(isNull(object,"month"));

            eventPOJO.setNotes(edtNote.getText().toString());
            //event.setCreatedAt(isNull(object,"created_at"));
            // event.setUpdatedAt(isNull(object,"updated_at"));
            Log.e("D--->",eventPOJO.toString());
            // eventList.add(event);
            GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask(mContext, PreferenceUtils.getUrlLogin(mContext),
                    PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),"eventregister",eventPOJO);
            getAccessTokenTask.execute();

        }
        private void showDialogAddLocation() {
            final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext);
            alertDialog.setTitle("Add Event Location !...");
            alertDialog.setMessage("Please add correct Details");

            View view = layoutInflater.inflate(R.layout.custom_addlocationdialog,null);

            edtLocation = (EditText)view.findViewById(R.id.edt_add_location);

            alertDialog.setView(view);
            //alertDialog.setIcon(R.drawable.ic_menu_black_24dp);

            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    locationPOJO.setName(edtLocation.getText().toString());
                    GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                            PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),"location",locationPOJO);
                    getAccessTokenTask.execute();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
        private void showDialogAddCalendar() {
            final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(mContext);
            alertDialog.setTitle("Add Event Calendar !...");
            alertDialog.setMessage("Please add correct Details");

            View view = layoutInflater.inflate(R.layout.custom_calendardialog,null);

            edtCalendar = (EditText)view.findViewById(R.id.edt_calendar_name);
            spnColor = (MaterialSpinner)view.findViewById(R.id.spn_colors);

            alertDialog.setView(view);
            //alertDialog.setIcon(R.drawable.ic_menu_black_24dp);


            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    calendarPOJO.setName(edtCalendar.getText().toString());
                    calendarPOJO.setColor(spnColor.getSelectedItem().toString().trim());
                    GetAccessTokenTask getAccessTokenTask = new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                            PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),"calendar",calendarPOJO);
                    getAccessTokenTask.execute();
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
        public boolean utilsCheck(){

            boolean cancel = false;
            View focusView = null;

            if (TextUtils.isEmpty(edtEventName.getText())){
                edtEventName.setError("Please enter Event Name");
                focusView=edtEventName;
                cancel=true;
            }
            if (TextUtils.isEmpty(edtStartDate.getText())&&!isValidDate(edtStartDate.getText().toString())){
                edtStartDate.setError("Please enter in dateformat");
                focusView=edtStartDate;
                cancel=true;
            }
            if (TextUtils.isEmpty(edtEndDate.getText())&&!isValidDate(edtEndDate.getText().toString())){
                edtEndDate.setError("Please enter in dateformat");
                focusView=edtEndDate;
                cancel=true;
            }
            //Spinner Validation
            if(spnCalendar.getSelectedItemPosition()==0){
                spnCalendar.setError("Please Select Gender");
                focusView=spnCalendar;
                cancel=true;
            }

            if (TextUtils.isEmpty(edtStartTime.getText())){
                edtStartTime.setError("Please enter Event Start Time");
                focusView=edtStartTime;
                cancel=true;
            }
            if (TextUtils.isEmpty(edtEndTime.getText())){
                edtEndTime.setError("Please enter Event End Time");
                focusView=edtEndTime;
                cancel=true;
            }
            if (TextUtils.isEmpty(edtCost.getText())){
                edtCost.setError("Please enter Event End Time");
                focusView=edtCost;
                cancel=true;
            }

            //Spinner Validation
            if(spnLocation.getSelectedItemPosition()==0){
                spnLocation.setError("Please Select Location");
                focusView=spnLocation;
                cancel=true;
            }


        /*if (TextUtils.isEmpty(edtRecurStartFrom.getText())&&!isValidDate(edtRecurStartFrom.getText().toString())){
            edtRecurStartFrom.setError("Please enter in dateformat");
            focusView=edtRecurStartFrom;
            cancel=true;
        }
        if (TextUtils.isEmpty(edtRecurEndFrom.getText())&&!isValidDate(edtRecurEndFrom.getText().toString())){
            edtRecurEndFrom.setError("Please enter in dateformat");
            focusView=edtRecurEndFrom;
            cancel=true;
        }
        */
       /* if(msGender.getSelectedItemPosition()==0){
            msGender.setError("Please Select Gender");
            focusView=msGender;
            cancel=true;
        }
        if(msMartialStatus.getSelectedItemPosition()==0){
            msMartialStatus.setError("Please Select Martial status");
            focusView=msMartialStatus;
            cancel=true;
        }
        if(msStatus.getSelectedItemPosition()==0){
            msStatus.setError("Please Select Status");
            focusView=msStatus;
            cancel=true;
        }*/

            return cancel;
        }
        public boolean isValidDate(String inDate) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dateFormat.setLenient(false);
            try {
                dateFormat.parse(inDate.trim());
            } catch (ParseException pe) {
                return false;
            }
            return true;
        }
        private void dateDialog(final EditText editText){
            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,R.style.DialogTheme,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            editText.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        private void timeDialog(final EditText editText) {
            // TODO Auto-generated method stub
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);
            final int sec = mcurrentTime.get(Calendar.SECOND);
            TimePickerDialog mTimePicker;
            mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    editText.setText( selectedHour + ":" + selectedMinute +":"+ sec);
                }
            }, hour, minute, true);//Yes 24 hour time

            mTimePicker.setTitle("Select Time");
            mTimePicker.show();

        }
        public class GetAccessTokenTask extends AsyncTask<String, String, String> {

            private Context mContext;
            private String URL;
            private String email,password;
            private EventPOJO eventPOJO;
            private LocationPOJO locationPOJO;
            private CalendarPOJO calendarPOJO;
            private MemberPOJO memberPOJO;
            String type;

            GetAccessTokenTask(Context mContext,String URL,String email,String password,String type,EventPOJO eventPOJO) {
                this.mContext = mContext;
                this.email=email;
                this.password=password;
                this.URL=URL;
                this.type = type;
                this.eventPOJO = eventPOJO;
            }
            GetAccessTokenTask(Context mContext,String URL,String email,String password,String type,CalendarPOJO calendarPOJO) {
                this.mContext = mContext;
                this.email=email;
                this.password=password;
                this.URL=URL;
                this.type = type;
                this.calendarPOJO = calendarPOJO;
            }
            GetAccessTokenTask(Context mContext,String URL,String email,String password,String type,LocationPOJO locationPOJO) {
                this.mContext = mContext;
                this.email=email;
                this.password=password;
                this.URL=URL;
                this.type = type;
                this.locationPOJO = locationPOJO;
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
                    Log.e("--->a", output);
                    JSONParser parser = new JSONParser();

                    JSONObject object = (JSONObject)parser.parse(output.toString());
                    token = (String)object.get("result");
                    Log.e("--->aa",token);

                    if(!token.equalsIgnoreCase("wrong email or password.")){
                        PreferenceUtils.setToken(mContext,token);
                        switch (type) {
                            case "eventregister":
                                // Event registration asyntask
                                EventUpdateTask eventUpdateTask = new EventUpdateTask(mContext,PreferenceUtils.getUrlEventUpdate(mContext),token,eventPOJO);
                                eventUpdateTask.execute();
                                break;
                            case "location":
                                // Event Location asyntask
                                EventAddLocationTask eventAddLocationTask = new EventAddLocationTask(mContext,PreferenceUtils.getUrlAddLocation(mContext),token,locationPOJO);
                                eventAddLocationTask.execute();
                                break;
                            case "calendar":
                                // Event Calendar asyntask
                                EventAddCalendarTask eventAddCalendarTask = new EventAddCalendarTask(mContext,PreferenceUtils.getUrlAddCalendar(mContext),token,calendarPOJO);
                                eventAddCalendarTask.execute();
                                break;
                        }
                    }else{
                        Toast.makeText(mContext,"Something went Wrong",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e("--->", e.toString());
                /*progressBar.setVisibility(View.GONE);
                buttonSubmit.setEnabled(true);*/
                    e.printStackTrace();
                }

            }
            @Override
            protected void onCancelled() {
            }
        }
        public class EventUpdateTask extends AsyncTask<String, String, String> {

            private Context mContext;
            private String URL;
            private String token;
            private EventPOJO eventPOJO;

            EventUpdateTask(Context mContext,String URL,String token,EventPOJO eventPOJO) {
                this.mContext = mContext;
                this.URL=URL;
                this.eventPOJO = eventPOJO;
                this.token = token;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                nDialog = new ProgressDialog(mContext); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
                nDialog.setMessage("Loading..");
                nDialog.setTitle("Uploading Data");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(false);
                nDialog.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> postData = new HashMap<>();

                postData.put("token", token);
                postData.put("id", event_id);
                postData.put("event_location_id", eventPOJO.getEventLocationId());
                postData.put("event_calendar_id", eventPOJO.getEventCalenderId());
                postData.put("cost", eventPOJO.getCost());
                postData.put("name", eventPOJO.getName());
                postData.put("start_date", eventPOJO.getStartDate());
                postData.put("end_date", eventPOJO.getEnd_date());
                postData.put("notes", eventPOJO.getNotes());

                postData.put("all_day", eventPOJO.getAllDay());
                postData.put("recurring", eventPOJO.getRecurring());
                postData.put("start_time", eventPOJO.getStart_time());
                postData.put("end_time", eventPOJO.getEnd_time());
                postData.put("recur_frequency", eventPOJO.getRecurFrequency());
                postData.put("recur_start_date", eventPOJO.getRecurStartDate());
                postData.put("recur_end_date", eventPOJO.getRecurEndDate());
                postData.put("recur_type", eventPOJO.getRecurType());

                String json_output = performPostCall(URL, postData);
                return json_output;
            }

            @Override
            protected void onPostExecute(String output) {
                String token=null;
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject)parser.parse(output);

                    Log.e("--->",output.toString());
                    //JSONArray jsonCalendarArray = (JSONArray)jsonObject.get("calendars");
                    JSONObject object = (JSONObject)jsonObject.get("event");

                    EventPOJO event = new EventPOJO();

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
                    dataBaseHelper.UpdateEventData(event);
                    nDialog.dismiss();
                    Toast.makeText(mContext, "Event Created Successfully", Toast.LENGTH_SHORT).show();
                    finish();

                } catch (Exception e) {
                    Log.e("--->", e.toString());
                /*progressBar.setVisibility(View.GONE);
                buttonSubmit.setEnabled(true);*/
                    e.printStackTrace();
                }

            }
            @Override
            protected void onCancelled() {
            }
        }
        public class EventAddCalendarTask extends AsyncTask<String, String, String> {

            private Context mContext;
            private String URL;
            private String token;
            private CalendarPOJO calendarPOJO;

            EventAddCalendarTask(Context mContext,String URL,String token,CalendarPOJO calendarPOJO) {
                this.mContext = mContext;
                this.URL=URL;
                this.calendarPOJO = calendarPOJO;
                this.token = token;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                nDialog = new ProgressDialog(mContext); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
                nDialog.setMessage("Loading..");
                nDialog.setTitle("Uploading Data");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(false);
                nDialog.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> postData = new HashMap<>();

                postData.put("token", PreferenceUtils.getToken(mContext));
                postData.put("name", calendarPOJO.getName());
                postData.put("color", calendarPOJO.getColor());

                String json_output = performPostCall(URL, postData);
                return json_output;
            }

            @Override
            protected void onPostExecute(String output) {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject)parser.parse(output);
                    //JSONArray jsonCalendarArray = (JSONArray)jsonObject.get("calendars");

                    JSONObject object = (JSONObject)jsonObject.get("calendars");

                    CalendarPOJO calendar = new CalendarPOJO();
                    calendar.setCalendar_id(String.valueOf(object.get("id")));
                    calendar.setBranch_id(isNull(object,"branch_id"));
                    calendar.setUser_id(isNull(object,"user_id"));
                    calendar.setName(isNull(object,"name"));
                    calendar.setColor(isNull(object,"color"));

                    Log.e("E--->",calendar.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertEventCalendarData(calendar);
                    nDialog.dismiss();
                    Toast.makeText(mContext, "Calendar Created Successfully", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Log.e("--->error1", e.toString());

                /*progressBar.setVisibility(View.GONE);
                buttonSubmit.setEnabled(true);*/
                    e.printStackTrace();
                }

            }
            @Override
            protected void onCancelled() {
            }
        }
        public class EventAddLocationTask extends AsyncTask<String, String, String> {

            private Context mContext;
            private String URL;
            private String token;
            private LocationPOJO locationPOJO;

            EventAddLocationTask(Context mContext,String URL,String token,LocationPOJO locationPOJO) {
                this.mContext = mContext;
                this.URL=URL;
                this.locationPOJO = locationPOJO;
                this.token = token;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                nDialog = new ProgressDialog(mContext); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
                nDialog.setMessage("Loading..");
                nDialog.setTitle("Uploading Data");
                nDialog.setIndeterminate(false);
                nDialog.setCancelable(false);
                nDialog.show();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected String doInBackground(String... params) {

                HashMap<String, String> postData = new HashMap<>();

                postData.put("token", PreferenceUtils.getToken(mContext));
                postData.put("name", locationPOJO.getName());

                String json_output = performPostCall(URL, postData);
                return json_output;
            }

            @Override
            protected void onPostExecute(String output) {
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject jsonObject = (JSONObject)parser.parse(output);
                    //JSONArray jsonCalendarArray = (JSONArray)jsonObject.get("calendars");

                    JSONObject object = (JSONObject)jsonObject.get("Locations");

                    LocationPOJO location = new LocationPOJO();

                    location.setUser_id(isNull(object,"user_id"));
                    location.setName(isNull(object,"name"));
                    location.setLocatio_id(String.valueOf(object.get("id")));

                    Log.e("E--->",location.toString());
                    //   eventList.add(event);
                    dataBaseHelper.insertEventLocationData(location);
                    nDialog.dismiss();
                    Toast.makeText(mContext, "Location Created Successfully", Toast.LENGTH_SHORT).show();
                    loadLocationSpinner();

                } catch (Exception e) {
                    Log.e("--->error1", e.toString());

                /*progressBar.setVisibility(View.GONE);
                buttonSubmit.setEnabled(true);*/
                    e.printStackTrace();
                }

            }
            @Override
            protected void onCancelled() {
            }
        }
        private void loadCalendarSpinner() {
            calendarArrayList = (ArrayList<CalendarPOJO>) dataBaseHelper.getAllCalendar();
            List<String> labels = new ArrayList<String>();
            for(int i = 0; i<calendarArrayList.size(); i++)
            {
                labels.add(calendarArrayList.get(i).getName().toString());
            }
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>
                    (mContext,android.R.layout.simple_list_item_1,labels);
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adp1.notifyDataSetChanged();
            spnCalendar.setAdapter(adp1); // spn new table
        }
        private void loadLocationSpinner() {
            locationArrayList = (ArrayList<LocationPOJO>) dataBaseHelper.getAllLocations();
            List<String> labels = new ArrayList<String>();
            for(int i = 0; i<locationArrayList.size(); i++)
            {
                labels.add(locationArrayList.get(i).getName().toString());
            }
            ArrayAdapter<String> adp1 = new ArrayAdapter<String>
                    (mContext,android.R.layout.simple_list_item_1,labels);
            adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adp1.notifyDataSetChanged();
            spnLocation.setAdapter(adp1); // spn new table
        }
        private void getCalendar(String calendarID) {

            calendarArrayList = (ArrayList<CalendarPOJO>) dataBaseHelper.getAllCalendar();

            for(int i = 0; i<calendarArrayList.size(); i++)
            {
                if(calendarArrayList.get(i).getCalendar_id().equals(calendarID)) {
                    spnCalendar.setSelection(i+1);
                }
            }
        }
        private void getLocation(String eventLocationId) {

            locationArrayList = (ArrayList<LocationPOJO>) dataBaseHelper.getAllLocations();
            for(int i = 0; i<locationArrayList.size(); i++)
            {
                if(locationArrayList.get(i).getLocatio_id().equals(eventLocationId)) {
                    spnLocation.setSelection(i+1);
                }
            }
        }
        public void getReccuring(String str) {
            List<String> l = Arrays.asList(getResources().getStringArray(R.array.reccurType));
            for (int i=0; i<l.size();i++){
                if(l.get(i).toLowerCase().equals(str.toLowerCase())){
                    spnRecurType.setSelection(i+1);
                }
            }
        }
    }


}
