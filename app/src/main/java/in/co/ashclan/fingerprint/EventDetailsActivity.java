package in.co.ashclan.fingerprint;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import in.co.ashclan.adpater.AttenderAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.model.EventPOJO;

public class EventDetailsActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener, View.OnClickListener{
    BottomNavigationBar bottomNavigationBar;
    ViewPager viewPager;
    int lastSelectedPosition = 0;
    Context mContext;
    MyViewPagerAdapter myViewPagerAdapter;
    MyReportViewPagerAdapter myReportViewPagerAdapter;
    MyAttenderViewPagerAdapter myAttenderViewPagerAdapter;
    private int[] layouts;
    DataBaseHelper dataBaseHelper;
    EventPOJO eventDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        mContext= this;
        init();
        refresh();

    }
    private void init(){
        eventDetails = (EventPOJO)getIntent().getSerializableExtra("event_details");
        layouts = new int[]{
                R.layout.event_pager_overview,
        };
        viewPager = (ViewPager)findViewById(R.id.events_view_pager);
        bottomNavigationBar = (BottomNavigationBar)findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar.setTabSelectedListener(this);

        dataBaseHelper = new DataBaseHelper(EventDetailsActivity.this);

    }



    private void refresh(){
        bottomNavigationBar.clearAll();

       setViewPager(0);

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
        Toast.makeText(mContext,position+"",Toast.LENGTH_LONG).show();

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
                    TextView overviewName,overviewDate,overviewCost,overviewCalender,overviewLocation,overviewNotes,overviewCreatedAt,overviewFeaturedImage;
                    overviewName=(TextView)view.findViewById(R.id.overview_event_name);
                    overviewDate=(TextView)view.findViewById(R.id.overview_event_date);
                    overviewCalender=(TextView)view.findViewById(R.id.overview_event_calender);
                    overviewCost=(TextView)view.findViewById(R.id.overview_event_cost);
                    overviewLocation=(TextView)view.findViewById(R.id.overview_event_location);
                    overviewFeaturedImage = (TextView)view.findViewById(R.id.overview_event_featured_image);
                    overviewNotes=(TextView)view.findViewById(R.id.overview_event_notes);
                    overviewCreatedAt=(TextView)view.findViewById(R.id.overview_event_created_at);

                    overviewName.setText(event.getName());
                    //more word.....
                    overviewDate.setText(event.getStartDate());
                    overviewCalender.setText("");
                    overviewCost.setText(event.getCost());
                    overviewLocation.setText(event.getEventLocationId());
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

            String event_id = eventDetails.getId().toString();

            DetailsList.addAll(dataBaseHelper.getAllAttender(event_id));

            AttenderAdapter attenderAdapter = new AttenderAdapter(EventDetailsActivity.this,DetailsList);
            listView.setAdapter(attenderAdapter);

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
            labels_Mstatus.add("Unknown");



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
    public class MyEditViewPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return 0;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return false;
        }
    }
}
