package in.co.ashclan.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.BinderThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.UUID;

import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

public class DataLoadingActivity extends AppCompatActivity {
    String token;
    DataBaseHelper dataBaseHelper;
    ArrayList<MemberPOJO> memberList = new ArrayList<MemberPOJO>();
    ImageView imageView1,imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_loading);
        token = (String)getIntent().getStringExtra("token");
        dataBaseHelper=new DataBaseHelper(DataLoadingActivity.this);
        imageView1=(ImageView)findViewById(R.id.imageView1);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadMemberDetails(token);
            }
        });
    }

    public void downloadMemberDetails(String token){
        final String uploadId = UUID.randomUUID().toString();

        try {
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(this,
                    uploadId,"http://52.172.221.235:8983/api/get_all_members");
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;

            multipartUploadRequest.addParameter("token",token)
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {

                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
   //                         Toast.makeText(DataLoadingActivity.this,serverResponse.getBodyAsString().toString(),Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

//                            Toast.makeText(DataLoadingActivity.this,serverResponse.getBodyAsString(),Toast.LENGTH_LONG).show();
                            dataBaseHelper.deleteAllMembers();
                            JSONParser parser = new JSONParser();
                            JSONArray jsonArray = null;
                            try{
                                jsonArray = (JSONArray)parser.parse(serverResponse.getBodyAsString());

                                for(int i=0;i<jsonArray.size();i++){

                                    MemberPOJO member = new MemberPOJO();

                                    JSONObject object = (JSONObject)jsonArray.get(i);

                                    member.setId(String.valueOf(object.get("id")));
                                    member.setUserId(String.valueOf(object.get("user_id")));

                                    member.setFirstName(isNull(object,"first_name"));
                                    member.setMiddleName(isNull(object,"middle_name"));
                                    member.setLastName(isNull(object,"last_name"));
                                    member.setGender(isNull(object,"gender"));
                                    member.setStatus(isNull(object,"status"));
                                    member.setMaritalStatus(isNull(object,"marital_status"));
                                    member.setDob(isNull(object,"dob"));
                                    member.setHomePhone(isNull(object,"home_phone"));

                                    member.setMobilePhone(isNull(object,"mobile_phone"));
                                    member.setWorkPhone(isNull(object,"work_phone"));
                                    member.setEmail(isNull(object,"email"));
                                    member.setAddress(isNull(object,"address"));
                                    member.setNotes(isNull(object,"notes"));
                                    member.setRollNo(isNull(object,"No"));
                                    member.setCreateAt(isNull(object,"created_at"));
                                    member.setUpdateAt(isNull(object,"updated_at"));
                                    member.setFingerPrint(isNull(object,"fingerprint"));


                                    //remainber to do changes...
                                    member.setPhotoURL(isNull(object,"photo",""));

                                    memberList.add(member);
                                    dataBaseHelper.insertMemberData(member);
                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Intent intent = new Intent(DataLoadingActivity.this,HomeActivity.class);
                            intent.putExtra("member_list",memberList);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

        //                    Toast.makeText(DataLoadingActivity.this,"xxxxx",Toast.LENGTH_SHORT).show();
                            downloadMemberDetails("");

                        }
                    })

                    .startUpload();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public String isNull(JSONObject object, String parma){
        return object.get(parma)!=null?object.get(parma).toString():"";
    }
    public String isNull(JSONObject object, String parma,String dafualtStr){
        return object.get(parma)!=null?object.get(parma).toString():dafualtStr;
    }
}
