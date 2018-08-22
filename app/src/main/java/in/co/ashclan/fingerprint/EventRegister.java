package in.co.ashclan.fingerprint;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.CalendarPOJO;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.LocationPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class EventRegister extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
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
    ArrayList<CalendarPOJO> calendarArrayList;
    ArrayList<LocationPOJO> locationArrayList;
    EventPOJO eventPOJO = new EventPOJO();
    CalendarPOJO calendarPOJO = new CalendarPOJO();
    LocationPOJO locationPOJO = new LocationPOJO();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);
        mInit();

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

    }
    private void mInit() {
        mContext            = EventRegister.this;
        edtEventName        = (EditText)findViewById(R.id.edt_event_name);
        edtStartDate        = (EditText)findViewById(R.id.edt_start_date);
        edtStartTime        = (EditText)findViewById(R.id.edt_start_time);
        edtEndDate          = (EditText)findViewById(R.id.edt_end_date);
        edtEndTime          = (EditText)findViewById(R.id.edt_end_time);
        edtCost             = (EditText)findViewById(R.id.edt_cost);
        edtLongitude        = (EditText)findViewById(R.id.edt_longitude);
        edtLatitude         = (EditText)findViewById(R.id.edt_latitude);
        edtNote             = (EditText)findViewById(R.id.edt_note);
        edtRecurFrequency   = (EditText)findViewById(R.id.edt_recur_frequency);
        edtRecurStartFrom   = (EditText)findViewById(R.id.recur_start_from);
        edtRecurEndFrom     = (EditText)findViewById(R.id.recurr_end_from);
        chkAllDayEvent      = (CheckBox) findViewById(R.id.chk_allday_event);
        chkRecurringEvent   = (CheckBox) findViewById(R.id.chk_recurring_event);
        spnCalendar         = (MaterialSpinner) findViewById(R.id.spn_calendar);
        spnLocation         = (MaterialSpinner) findViewById(R.id.spn_location);
        spnRecurType        = (MaterialSpinner) findViewById(R.id.spn_RecurType);
        img_btnAddCalendar  = (ImageView)findViewById(R.id.img_btnCalendarAdd);
        img_btnAddLocation  = (ImageView)findViewById(R.id.img_btnLocationAdd);
        btnSubmit           = (Button)findViewById(R.id.btn_Event_submit);
        ReccuringLayout     = (LinearLayout)findViewById(R.id.linear_recurring);
        progressBar         = (ProgressBar)findViewById(R.id.progress_bar_member_register);

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
    private void insertIntoDatabase(JSONObject jsonObject) {

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
        EventRegister.GetAccessTokenTask getAccessTokenTask = new EventRegister.GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                        PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),"eventregister",eventPOJO);
        getAccessTokenTask.execute();

    }
    private void showDialogAddLocation() {
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setTitle("Add Event Location !...");
        alertDialog.setMessage("Please add correct Details");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_addlocationdialog,null);

        edtLocation = (EditText)view.findViewById(R.id.edt_add_location);

        alertDialog.setView(view);
        //alertDialog.setIcon(R.drawable.ic_menu_black_24dp);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                locationPOJO.setName(edtLocation.getText().toString());
                EventRegister.GetAccessTokenTask getAccessTokenTask = new EventRegister.GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
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
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setTitle("Add Event Calendar !...");
        alertDialog.setMessage("Please add correct Details");
        LayoutInflater layoutInflater = this.getLayoutInflater();
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

                EventRegister.GetAccessTokenTask getAccessTokenTask = new EventRegister.GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
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
    public static boolean isValidDate(String inDate) {
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,R.style.DialogTheme,
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
                            EventRegister.EventRegistrationTask eventRegistrationTask = new EventRegistrationTask(mContext,PreferenceUtils.getUrlEventRegistration(mContext),token,eventPOJO);
                            eventRegistrationTask.execute();
                            break;
                        case "location":
                            // Event Location asyntask
                            EventRegister.EventAddLocationTask eventAddLocationTask = new EventRegister.EventAddLocationTask(mContext,PreferenceUtils.getUrlAddLocation(mContext),token,locationPOJO);
                            eventAddLocationTask.execute();
                            break;
                        case "calendar":
                            // Event Calendar asyntask
                            EventRegister.EventAddCalendarTask eventAddCalendarTask = new EventRegister.EventAddCalendarTask(mContext,PreferenceUtils.getUrlAddCalendar(mContext),token,calendarPOJO);
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
    public class EventRegistrationTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String token,password;
        private EventPOJO eventPOJO;

        EventRegistrationTask(Context mContext,String URL,String token,EventPOJO eventPOJO) {
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
                dataBaseHelper.insertEventData(event);
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
}
