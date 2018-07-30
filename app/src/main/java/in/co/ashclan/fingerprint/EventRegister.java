package in.co.ashclan.fingerprint;

import android.app.DatePickerDialog;
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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.EventPOJO;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_register);
        mInit();

        btnSubmit.setOnClickListener(this);
        img_btnAddLocation.setOnClickListener(this);
        img_btnAddCalendar.setOnClickListener(this);
        edtStartDate.setOnClickListener(this);
        edtEndDate.setOnClickListener(this);
        edtRecurStartFrom.setOnClickListener(this);
        edtRecurEndFrom.setOnClickListener(this);
        edtStartTime.setOnClickListener(this);
        edtEndTime.setOnClickListener(this);

        dataBaseHelper = new DataBaseHelper(this);


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
            if(utilsCheck()){
                insertIntoDatabase();
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
    private void insertIntoDatabase() {

    /*    EventPOJO event = new EventPOJO();

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
        dataBaseHelper.insertEventData(event);*/
    }
    private void showDialogAddLocation() {
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
        alertDialog.setTitle("Add Event Location !...");
        alertDialog.setMessage("Please add correct Details");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custom_addlocationdialog,null);

        edtLocation = (EditText)findViewById(R.id.edt_add_location);

        alertDialog.setView(view);
        //alertDialog.setIcon(R.drawable.ic_menu_black_24dp);

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
        edtCalendar = (EditText)findViewById(R.id.edt_calendar_name);
        spnColor = (MaterialSpinner)findViewById(R.id.spn_colors);

        alertDialog.setView(view);
        //alertDialog.setIcon(R.drawable.ic_menu_black_24dp);



        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                editText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();

    }
    private void showAlertDialog() {
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
            String url = "https://bwc.pentecostchurch.org/api/login";
            String urls = "http://52.172.221.235:8983/api/login";
            String json_output = performPostCall(url, postData);
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


                    PreferenceUtils.setToken(mContext,token);
                   /* switch (type) {
                        case "edit":
                            memberUpdateGovNet(PreferenceUtils.getUrlUpdateMember(mContext), memberDetails);
                            break;
                        case "register":
                            memberRegisterGovNet(PreferenceUtils.getUrlCreateMember(mContext), memberDetails);
                            break;
                    }*/
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

}
