package in.co.ashclan.fingerprint;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import in.co.ashclan.adpater.EventAttendanceAdapter;
import in.co.ashclan.adpater.RecordingAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.RecordingPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class RecordingActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<RecordingPOJO> DetailsList = new ArrayList<>();
    DataBaseHelper dataBaseHelper;
    FloatingActionButton fab ;
    Context mContext;
    private ProgressBar progressBar;
    RecordingPOJO recordingPOJO;
    public int listPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        getSupportActionBar().setTitle("Recording");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mContext = RecordingActivity.this;
        progressBar=(ProgressBar)findViewById(R.id.progress_bar_recording_register);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        fab = (FloatingActionButton)findViewById(R.id.fab_upload_recording);

        listView = (ListView)findViewById(R.id.record_list);
        DetailsList.addAll(dataBaseHelper.getAllRecordings());
        RecordingAdapter recordingAdapter = new RecordingAdapter(RecordingActivity.this,DetailsList);
        listView.setAdapter(recordingAdapter);
        final boolean[] tickMarkVisibileListPosition = new boolean[100000];

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

             /*  RecordingPOJO recording1 = DetailsList.get(i);

                RecordingActivity.GetAccessTokenTask aTask = new RecordingActivity.GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                        PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),recording1);
                aTask.execute();*/

              listPosition = i - listView.getFirstVisiblePosition();
                if (listView.getChildAt(listPosition).findViewById(R.id.img_upload).getVisibility() == View.INVISIBLE)
                {
                    listView.getChildAt(listPosition).findViewById(R.id.img_upload).setVisibility(View.VISIBLE);
                    tickMarkVisibileListPosition[i]=Boolean.TRUE;
                }
                else
                {
                    listView.getChildAt(listPosition).findViewById(R.id.img_upload).setVisibility(View.INVISIBLE);
                    tickMarkVisibileListPosition[i]=Boolean.FALSE;
                }
                listView.getChildAt(listPosition).setSelected(true);

            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RecordingActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class GetAccessTokenTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String email,password;
        private String memberIds,eventId;
        public RecordingPOJO recordingPOJO;

        GetAccessTokenTask(Context mContext,String URL,String email,String password,RecordingPOJO recordingPOJO) {
            this.mContext = mContext;
            this.email=email;
            this.password=password;
            this.URL=URL;
            this.recordingPOJO = recordingPOJO;
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
                Log.e("--->", output);
                JSONParser parser = new JSONParser();

                JSONObject object = (JSONObject)parser.parse(output.toString());
                token = (String)object.get("result");
                Log.e("--->",token);

                if(!token.equalsIgnoreCase("wrong email or password.")){
                        RecordingRegisterGovNet(PreferenceUtils.getUrlRecordingRegistration(mContext), recordingPOJO);
                }else{
                    Toast.makeText(mContext,"Something went Wrong",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("--->", e.toString());
                progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }

        @Override
        protected void onCancelled() {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void RecordingRegisterGovNet(String URL, final RecordingPOJO recordingPOJO){
        try {
            Log.e("---->",URL);
            Log.e("---->REG",recordingPOJO.toString());

            final String uploadId = UUID.randomUUID().toString();

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;

            multipartUploadRequest.addFileToUpload(recordingPOJO.getFilePath(),"fileToUpload" )
                    .addParameter("token", PreferenceUtils.getToken(RecordingActivity.this))
                    .addParameter("event_id", recordingPOJO.getEventid())
                    .addParameter("user_id", recordingPOJO.getUserid())
                    .addParameter("event_date", recordingPOJO.getEventDate())

                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                            Toast.makeText(RecordingActivity.this,"error",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("--->",serverResponse.getBodyAsString().toString());

                            JSONParser parser = new JSONParser();
                            RecordingPOJO recording = new RecordingPOJO();
                            JSONObject jsonObject=null;
                            try{
                                jsonObject = (JSONObject)parser.parse(serverResponse.getBodyAsString().toString());
                                String result = (String) jsonObject.get("result");
                                Toast.makeText(RecordingActivity.this,result,Toast.LENGTH_SHORT).show();

                                JSONObject memberObject = (JSONObject) jsonObject.get("recording");

                                recording.setEventid(isNull(memberObject,"event_id"));
                                recording.setUserid(isNull(memberObject,"user_id"));
                                recording.setFilename(isNull(memberObject,"file_name"));
                                recording.setEventDate(isNull(memberObject,"event_date"));
                                recording.setCreatedat(isNull(memberObject,"created_at"));
                                recording.setUpdatedat(isNull(memberObject,"updated_at"));

                                dataBaseHelper.insertRecordingData(recording);

                                progressBar.setVisibility(View.INVISIBLE);

                                finish();

                            }catch (Exception ex){
                                ex.printStackTrace();
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

    public String isNull(JSONObject object, String parma){
        return object.get(parma)!=null?object.get(parma).toString():"";
    }
}


