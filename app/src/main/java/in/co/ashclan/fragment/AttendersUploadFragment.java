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
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.fingerprint.AttendanceActivity;
import in.co.ashclan.fingerprint.AttenderActivity;
import in.co.ashclan.fingerprint.HomeActivity;
import in.co.ashclan.fingerprint.R;
import in.co.ashclan.fingerprint.TestingActivity;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

@SuppressLint("ValidFragment")
public class AttendersUploadFragment extends Fragment implements View.OnClickListener {
   // private OnFragmentInteractionListener mListener;
    Activity activity;
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
    private static final String TAG = "-->frag";

    @SuppressLint("ValidFragment")
    public AttendersUploadFragment(Activity activity, String eventId, ArrayList<MemberPOJO> personList) {
        // Required empty public constructor
        this.activity = activity;
        this.eventId = eventId;
        this.personList = personList;
    }
    public AttendersUploadFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mContext = activity;
        mContext = getActivity().getApplicationContext();
        dataBaseHelper = new DataBaseHelper(mContext);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attenders_upload, container, false);

        fpListView = (ListView) view.findViewById(R.id.attender_listView);
        txtNote = (TextView)view.findViewById(R.id.txt_message);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.fab_upload_attendance);
        progressBar = (ContentLoadingProgressBar)view.findViewById(R.id.fragment_progress_bar_attender);


        if(personList.size()!= 0){
            memberAdapter = new MemberAdapter(mContext,personList,"ic_person.png");
            memberAdapter.notifyDataSetChanged();
            // dataBaseHelper=new DataBaseHelper(mContext);
            fpListView.setAdapter(memberAdapter);
        }
        floatingActionButton.setOnClickListener(this);
       /* floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Toast.makeText(mContext, "This is Clicked", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Floating action button clicked" );
                Intent intent = new Intent(mContext,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();*//*

                if(personList.size()!=0){
                    try{
                        progressBar.setVisibility(View.VISIBLE);
                        String memberIds="";
                        //String eventId=getIntent().getStringExtra("eventId");
                        for (MemberPOJO member:personList){
                            memberIds=memberIds+member.getId()+",";
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
               *//*     Snackbar snackbar = Snackbar.make(view,"Nothing to Upload",Snackbar.LENGTH_LONG);
                   *//**//* snackbar.setAction("Cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            favoritesAdapter.restoreItem(deletefav,deleteIndex);
                            new Database(getBaseContext()).addToFavourites(deletefav);
                            loadListFavorites();
                        }
                    });*//**//*
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();*//*
                    Toast.makeText(activity, "Nothing to Upload", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
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
        if(personList.size()!=0){
            try{
                progressBar.setVisibility(View.VISIBLE);
                String memberIds="";
                //String eventId=getIntent().getStringExtra("eventId");
                for (MemberPOJO member:personList){
                    memberIds=memberIds+member.getId()+",";
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
            //progressBar.setVisibility(View.VISIBLE);
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
                    CheckInTask checkInTask  = new CheckInTask(mContext, PreferenceUtils.getUrlAddCheckin(mContext),memberIds,eventId,token);
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
    public void workExit(){
        if(SerialPortManager.getInstance().isOpen()){
            bIsCancel=true;
            SerialPortManager.getInstance().closeSerialPort();
            Toast.makeText(mContext, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();
         /* Intent intent = new Intent(mContext,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);*/
            getActivity().finish();
        }
    }
    public String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }
}
