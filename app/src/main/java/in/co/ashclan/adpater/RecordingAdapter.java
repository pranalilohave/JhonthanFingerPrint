package in.co.ashclan.adpater;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.fingerprint.R;
import in.co.ashclan.fingerprint.RecordingActivity;
import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.RecordingPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class RecordingAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<RecordingPOJO> list;
    RecordingPOJO recordingPOJO;
    TextView txt_RecordName,txt_RecordDate,txt_eventName;
    ImageView imgUpload;
    ImageView imgPlay;
    private ProgressBar progressBar;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    DataBaseHelper dataBaseHelper;

    private final Handler handler = new Handler();

    public RecordingAdapter(Context mContext, ArrayList<RecordingPOJO> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View vList;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.custom_recording_template,null);
        }else {
            vList = (View)view;
        }

        dataBaseHelper = new DataBaseHelper(mContext);
        txt_RecordName =(TextView)vList.findViewById(R.id.record_name);
        txt_RecordDate=(TextView)vList.findViewById(R.id.record_date);
        txt_eventName=(TextView)vList.findViewById(R.id.EventName);
        imgUpload = (ImageView)vList.findViewById(R.id.img_upload);
        progressBar=(ProgressBar)vList.findViewById(R.id.progress_bar_recording_register);

        imgPlay = (ImageView)vList.findViewById(R.id.img_play);
        imgPlay.setVisibility(View.GONE);

        imgUpload.setVisibility(View.VISIBLE);

        recordingPOJO = list.get(i);

        txt_RecordName.setText(recordingPOJO.getFilename());
        txt_RecordDate.setText(recordingPOJO.getEventDate());

        txt_eventName.setText(dataBaseHelper.strEventName(recordingPOJO.getEventid().toString()));

        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RecordingPOJO recording2 = list.get(i);
                Log.e("recording 2", recording2.toString());

                GetAccessTokenTask aTask = new GetAccessTokenTask(mContext, PreferenceUtils.getUrlLogin(mContext),
                        PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),recording2);
                aTask.execute();
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClick();
            }
        });



        mediaPlayer = MediaPlayer.create(mContext, Uri.parse(list.get(i).getFilePath().toString()));

        seekBar = (SeekBar) vList.findViewById(R.id.SeekBar01);

        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnTouchListener(new View.OnTouchListener() {@Override public boolean onTouch(View v, MotionEvent event) {
            seekChange(v);
            return false; }
        });

        return vList;
    }

    // This is event handler thumb moving event
    private void seekChange(View v){
        if(mediaPlayer.isPlaying()){
            SeekBar sb = (SeekBar)v;
            mediaPlayer.seekTo(sb.getProgress());
        }
    }

    private void buttonClick() {
            try{
                mediaPlayer.start();
                startPlayProgressUpdater();
            }catch (IllegalStateException e) {
                mediaPlayer.pause();
            }
    }

    public void startPlayProgressUpdater() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            Runnable notification = new Runnable() {
                public void run() {
                    startPlayProgressUpdater();
                }
            };
            handler.postDelayed(notification,100);
        }else{
            mediaPlayer.pause();
            seekBar.setProgress(0);
        }
    }

    public String datePattern(String dateStr) throws ParseException {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date MyDate = newDateFormat.parse(dateStr);
        newDateFormat.applyPattern("EEEE,d MMM yyyy");
        return newDateFormat.format(MyDate);
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

                    //for(RecordingPOJO recording : DetailsList) {

                        RecordingRegisterGovNet(PreferenceUtils.getUrlRecordingRegistration(mContext), recordingPOJO);

                    //}
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
            Log.e("---->REC",recordingPOJO.toString());

            final String uploadId = UUID.randomUUID().toString();

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;

            Log.e("---->RECORDING",recordingPOJO.getFilePath().toString());

            multipartUploadRequest.addFileToUpload(recordingPOJO.getFilePath(),"fileToUpload" )
                    .addParameter("token", PreferenceUtils.getToken(mContext))
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
                            Toast.makeText(mContext,"error",Toast.LENGTH_LONG).show();
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

                                Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();
                                imgPlay.setVisibility(View.VISIBLE);
                                imgUpload.setVisibility(View.INVISIBLE);
                                //Log.e("-->UP", result.toString() );

                               /* Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();

                                JSONObject memberObject = (JSONObject) jsonObject.get("recording");

                                recording.setEventid(isNull(memberObject,"event_id"));
                                recording.setUserid(isNull(memberObject,"user_id"));
                                recording.setFilename(isNull(memberObject,"file_name"));
                                recording.setEventDate(isNull(memberObject,"event_date"));
                                recording.setCreatedat(isNull(memberObject,"created_at"));
                                recording.setUpdatedat(isNull(memberObject,"updated_at"));

                                dataBaseHelper.insertRecordingData(recording);*/
                                progressBar.setVisibility(View.INVISIBLE);

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
            Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String isNull(JSONObject object, String parma){
        return object.get(parma)!=null?object.get(parma).toString():"";
    }

}
