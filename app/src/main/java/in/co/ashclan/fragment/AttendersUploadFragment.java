package in.co.ashclan.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
//import android.app.Fragment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;

import android_serialport_api.SerialPortManager;
import in.co.ashclan.adpater.AttenderAdapter;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.fingerprint.AttendanceActivity;
import in.co.ashclan.fingerprint.AttenderActivity;
import in.co.ashclan.fingerprint.EventDetailsActivity;
import in.co.ashclan.fingerprint.HomeActivity;
import in.co.ashclan.fingerprint.R;
import in.co.ashclan.fingerprint.TestingActivity;
import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

@SuppressLint("ValidFragment")
public class AttendersUploadFragment extends Fragment implements View.OnClickListener {
   //   // private OnFragmentInteractionListener mListener;
    Activity activity;
    Context mContext;
    MemberAdapter memberAdapter;
    AttenderAdapter attenderAdapter;
    ArrayList<AttenderPOJO> personList = new ArrayList<AttenderPOJO>();
    ArrayList<EventAttendancePOJO> attendersList = new ArrayList<EventAttendancePOJO>();
    String eventId;
    private ListView fpListView;
    TextView txtNote,txtTotalAttendance,txtTotalMale,txtTotalFemale;
    DataBaseHelper dataBaseHelper;
    ContentLoadingProgressBar progressBar;
    FloatingActionButton floatingActionButton;
    private boolean bIsCancel = false;
    private ProgressDialog nDialog;
    String txtdate;
    private static final String TAG = "-->frag";

    @SuppressLint("ValidFragment")
    public AttendersUploadFragment(Activity activity, String eventId) {
        // Required empty public constructor
        this.activity = activity;
        this.eventId = eventId;
    }

    @SuppressLint("ValidFragment")
    public AttendersUploadFragment(Activity activity, String eventId,String txtdate) {
        // Required empty public constructor
        this.activity = activity;
        this.eventId = eventId;
        this.txtdate = txtdate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mContext = activity;
        mContext = getActivity().getApplicationContext();
        dataBaseHelper = new DataBaseHelper(mContext);
        personList = (ArrayList<AttenderPOJO>) dataBaseHelper.getAllTempAttender(eventId);
      /*  AttenderPOJO attenderPOJO1 = new AttenderPOJO("BWC1","demo","123456789","male","2","jungle","demo","khan","12/12/12","12/12/12","");
        personList.add(attenderPOJO1);
        personList.add(attenderPOJO1);
        personList.add(attenderPOJO1);
        personList.add(attenderPOJO1);
        personList.add(attenderPOJO1);
        personList.add(attenderPOJO1);*/
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attenders_upload, container, false);

        fpListView = (ListView) view.findViewById(R.id.attender_listView);
        txtNote = (TextView)view.findViewById(R.id.txt_message);
        txtTotalAttendance = (TextView)view.findViewById(R.id.total_attendance);
        txtTotalMale = (TextView)view.findViewById(R.id.total_male);
        txtTotalFemale = (TextView)view.findViewById(R.id.total_female);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_upload_attendance);
        progressBar = (ContentLoadingProgressBar)view.findViewById(R.id.fragment_progress_bar_attender);
        floatingActionButton.setEnabled(true);

        if(personList.size()!= 0){

            //personList.addAll(dataBaseHelper.getAllTempAttender(eventId));
            attenderAdapter = new AttenderAdapter(mContext,personList);
            attenderAdapter.notifyDataSetChanged();
            fpListView.setAdapter(attenderAdapter);

            txtTotalAttendance.setText("Total Attendance = " + personList.size());
            txtTotalMale.setText("Total Male's :- " + dataBaseHelper.getmaleCount(eventId));
            txtTotalFemale.setText("Total Female's :- " + dataBaseHelper.getfemaleCount(eventId));

           /* memberAdapter = new MemberAdapter(mContext,personList,"ic_person.png");
            memberAdapter.notifyDataSetChanged();
            // dataBaseHelper=new DataBaseHelper(mContext);
            fpListView.setAdapter(memberAdapter);*/
        }
        floatingActionButton.setOnClickListener(this);
        ScrollListViewToBottom();
        return view;
    }

    /*public void onButtonPressed(View view) {
            if (mListener != null) {
                mListener.onFragmentInteraction(view);
            }
        }*/
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
       // ((AttendanceActivity) activity).onAttachedToWindow();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    @Override
    public void onClick(View view) {
        if(view==floatingActionButton){

            attendersList = (ArrayList<EventAttendancePOJO>) dataBaseHelper.getAllTempAttendance(eventId);

        if(attendersList.size()!=0){
            try{
               // progressBar.setVisibility(View.VISIBLE);
                String memberIds="";
                //String eventId=getIntent().getStringExtra("eventId");
                for (EventAttendancePOJO member : attendersList){
                    //condition for check the id is existed into the database or not
                    if(!dataBaseHelper.isMemberAvailable(member.getMemberId(),eventId)) {
                        memberIds = memberIds + member.getMemberId() + "*" + member.getAttenTime() + ",";
                    }
                }
                memberIds = removeLastChar(memberIds);

                GetAccessTokenTask aTask=new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                        PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),memberIds,eventId);
                aTask.execute();
            }catch (Exception e)
            {
                Log.e("-->",e.toString());
            }

        }else{
            //Make SmackBar
            /*     Snackbar snackbar = Snackbar.make(view,"Nothing to Upload",Snackbar.LENGTH_LONG);
             *//* snackbar.setAction("Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            favoritesAdapter.restoreItem(deletefav,deleteIndex);
                            new Database(getBaseContext()).addToFavourites(deletefav);
                            loadListFavorites();
                        }
                    });*//*
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();*/
            Toast.makeText(activity, "Nothing to Upload", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* public interface OnFragmentInteractionListener {
         // TODO: Update argument type and name
         void onFragmentInteraction(View uri);
     }*/
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

            nDialog = new ProgressDialog(getActivity());
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
                   // progressBar.setVisibility(View.GONE);
                    CheckInTask checkInTask  = new CheckInTask(mContext, PreferenceUtils.getUrlAddCheckin(mContext),memberIds,eventId,token);
                    checkInTask.execute();
                }
            } catch (Exception e) {
                Log.e("--->", e.toString());
                Toast.makeText(mContext,"No Internet Access",Toast.LENGTH_LONG).show();
                e.printStackTrace();
                //progressBar.setVisibility(View.GONE);
                nDialog.dismiss();
            }
        }

        @Override
        protected void onCancelled() {
            //progressBar.setVisibility(View.GONE);
           // nDialog.dismiss();
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
           // progressBar.setVisibility(View.VISIBLE);
            Log.e("--->","URL = "+URL+ "  member_id = "+member_id+" event_id = "+event_id+" token = "+token);
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
                postData.put("atten_date",txtdate);
                //postData.put("event_id",attentime);
                String json_output = performPostCall(URL,postData);
                return json_output;
            }catch (Exception ex){
                Log.e("--->",ex.toString());
                Toast.makeText(mContext, "Please Try Again", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext,jsonObject.get("result").toString(),Toast.LENGTH_LONG).show();

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

                    dataBaseHelper.deleteAllTempEventAttendance(eventId);

                    progressBar.setVisibility(View.GONE);
                    nDialog.dismiss();
                }
            }catch (Exception ex){
                Log.e("--->",ex.toString());
                progressBar.setVisibility(View.GONE);
                nDialog.dismiss();
            }
            workExit();
        }
        @Override
        protected void onCancelled() {
            super.onCancelled();
            progressBar.setVisibility(View.GONE);
            nDialog.dismiss();
        }
    }
    public void workExit(){
        if(SerialPortManager.getInstance().isOpen()){
            bIsCancel=true;
            SerialPortManager.getInstance().closeSerialPort();
            //Toast.makeText(mContext, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
         /* Intent intent = new Intent(mContext,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
        }
        getActivity().finish();
    }
    public String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void ScrollListViewToBottom() {
        fpListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                try {
                    fpListView.setSelection(attenderAdapter.getCount() - 1);
                }catch (Exception e)
                {
                    Log.e(TAG, e.toString() );
                    e.printStackTrace();
                }

            }
        });
    }//scrolling
}
