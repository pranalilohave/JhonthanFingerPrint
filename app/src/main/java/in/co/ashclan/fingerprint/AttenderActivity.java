package in.co.ashclan.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

import android_serialport_api.SerialPortManager;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class AttenderActivity extends AppCompatActivity {
    Context mContext;
    MemberAdapter memberAdapter;
    ArrayList<MemberPOJO> personList = new ArrayList<MemberPOJO>();
    String eventId;
    private ListView fpListView;
    TextView txtNote;
    DataBaseHelper dataBaseHelper;
    ContentLoadingProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    private boolean bIsCancel = false;
    private static final String TAG = "-->";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attender);

        mContext = AttenderActivity.this;
        dataBaseHelper = new DataBaseHelper(mContext);
        fpListView = (ListView) findViewById(R.id.attender_listView);
        txtNote = (TextView)findViewById(R.id.txt_message);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab_upload_attendance);

        progressBar = (ContentLoadingProgressBar)findViewById(R.id.progress_bar_attender);

        if(getIntent()!=null){
            try{
            eventId = getIntent().getStringExtra("eventId");
            personList = (ArrayList<MemberPOJO>) getIntent().getSerializableExtra("person");

            if(personList.size()>0){
                memberAdapter = new MemberAdapter(mContext,personList,"ic_person.png");
                memberAdapter.notifyDataSetChanged();
                // dataBaseHelper=new DataBaseHelper(mContext);
                fpListView.setAdapter(memberAdapter);
            }

            }catch (Exception e) {
                Log.e(TAG, "onCreate:"+e.toString() );
            }
        }else{
            txtNote.setVisibility(View.VISIBLE);
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String memberIds="";
                    String eventId=getIntent().getStringExtra("eventId");
                    for (MemberPOJO member:personList){
                        memberIds=memberIds+member.getId()+",";
                    }
                    memberIds = removeLastChar(memberIds);
                    AttenderActivity.GetAccessTokenTask aTask=new AttenderActivity.GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                            PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),memberIds,eventId);
                    aTask.execute();
                }catch (Exception e)
                {
                    Log.e("-->",e.toString());
                }
                /*if(personList.size()<0){

                }else {
                    Toast.makeText(mContext, "Nothing to Upload", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    public class GetAccessTokenTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String email,password;
        private String memberIds,eventId;

        GetAccessTokenTask(Context mContext,String URL,String email,String password,String memberIds,String eventId) {
            this.mContext = mContext;
            this.email=email;
            this.password=password;
            this.URL=URL;

            this.memberIds=memberIds;
            this.eventId=eventId;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
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
                    //HomeActivity.GetAllMemberTask mTask = new HomeActivity.GetAllMemberTask(mContext,PreferenceUtils.getUrlGetAllMembers(mContext),token);
                    // mTask.execute();
                    progressBar.setVisibility(View.GONE);
                    AttenderActivity.CheckInTask checkInTask  = new AttenderActivity.CheckInTask(mContext, PreferenceUtils.getUrlAddCheckin(mContext),memberIds,eventId,token);
                    checkInTask.execute();
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
    public class CheckInTask extends AsyncTask<String,String,String> {

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
            progressBar.setVisibility(View.VISIBLE);
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

                //JSONObject jsonResultObject=(JSONObject)jsonObject.get("result");
                //Toast.makeText(mContext,jsonObject.get("result").toString(),Toast.LENGTH_LONG).show();

                JSONArray jsonAttendanceArray=(JSONArray)jsonObject.get("attendance");

                for(int i=0;i<jsonAttendanceArray.size();i++){
                    EventAttendancePOJO eventAttendance=new EventAttendancePOJO();
                    JSONObject object = (JSONObject)jsonAttendanceArray.get(i);
                    eventAttendance.setEventId(isNull(object,"event_id"));
                    eventAttendance.setUserId(isNull(object,"user_id"));
                    eventAttendance.setAnonymous(isNull(object,"anonymous"));
                    eventAttendance.setMemberId(isNull(object,"member_id"));
                    eventAttendance.setDate(isNull(object,"date"));
                    eventAttendance.setCreatedAt(isNull(object,"created_at"));
                    eventAttendance.setUpdatedAt(isNull(object,"updated_at"));
                    eventAttendance.setAttenDate(isNull(object,"atten_date"));
                    eventAttendance.setAttenTime(isNull(object,"atten_time"));
                    dataBaseHelper.insertEventAttendaceData(eventAttendance);
                    progressBar.setVisibility(View.GONE);
                }
            }catch (Exception ex){
                Log.e("--->",ex.toString());
                progressBar.setVisibility(View.GONE);
            }
            workExit();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar.setVisibility(View.GONE);
        }
    }
    private void workExit(){
        if(SerialPortManager.getInstance().isOpen()){
            bIsCancel=true;
            SerialPortManager.getInstance().closeSerialPort();
            //startActivity(new Intent(mContext,AttendanceActivity.class));
            this.finish();
        }
    }
    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    //    startActivity(new Intent(mContext,AttendanceActivity.class));
        this.finish();
    }
}
