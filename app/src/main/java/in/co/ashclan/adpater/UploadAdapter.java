package in.co.ashclan.adpater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import android_serialport_api.SerialPortManager;
import android_serialport_api.SerialPortManagerA5;
import de.hdodenhof.circleimageview.CircleImageView;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.fingerprint.LoginActivity;
import in.co.ashclan.fingerprint.MemberRegisterActivity;
import in.co.ashclan.fingerprint.R;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;

public class UploadAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MemberPOJO> list;
    String defaultIcon;
    ImageLoaderConfiguration loaderConfiguration;
    ImageLoader imageLoader = ImageLoader.getInstance();
    ImageView imageView, uploadImage;

    TextView textViewName,textViewLocation,textViewGender,
            textViewAge,textViewPhone,textViewRollno;

    DataBaseHelper dataBaseHelper;
    MemberPOJO member;

    public UploadAdapter(Context context,ArrayList<MemberPOJO> list,String defaultIcon){
        mContext = context;
        this.defaultIcon = defaultIcon;
        this.list=list;

        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageOnLoading(R.drawable.ic_person)
                .showImageForEmptyUri(R.drawable.ic_person)
                .showImageOnFail(R.drawable.ic_person)
                .build();

        loaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(imageOptions).build();
        imageLoader.init(loaderConfiguration);
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
        return Long.parseLong(list.get(i).getId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vList;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.member_details,null);
        }else {
            vList = (View)view;
        }
        dataBaseHelper = new DataBaseHelper(mContext);
        imageView=(CircleImageView)vList.findViewById(R.id.list_imgDoc);
        uploadImage = (ImageView)vList.findViewById(R.id.upload_img);

        textViewName=(TextView)vList.findViewById(R.id.md_name);
        textViewLocation=(TextView)vList.findViewById(R.id.md_location);
        textViewGender=(TextView)vList.findViewById(R.id.md_gender);
        textViewPhone=(TextView)vList.findViewById(R.id.md_phone);
        textViewAge=(TextView)vList.findViewById(R.id.md_age);
        textViewRollno=(TextView)vList.findViewById(R.id.md_rollno);

        member = list.get(i);
        textViewName.setText(member.getFirstName()+" "+member.getLastName());
        textViewLocation.setText(member.getAddress());
        textViewGender.setText("Gender: "+member.getGender());
        textViewPhone.setText(member.getMobilePhone());
        textViewRollno.setText(member.getRollNo());

        if(member.getDob()!=null) {
            String birthdateStr = member.getDob();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            try {
                Date birthdate = df.parse(birthdateStr);
                textViewAge.setText("Age: "+String.valueOf(calculateAge(birthdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (null!=member.getPhotoURL()){
            String imgURL= PreferenceUtils.getUrlUploadImage(mContext)+member.getPhotoURL();
            try {
                imageLoader.displayImage(imgURL, imageView, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        //    imageLoader.displayImage("http://52.172.221.235:8983/uploads/" + defaultIcon, imageView);
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }catch (Exception e){
                imageView.setImageResource(R.drawable.ic_profile_image_1);
                e.printStackTrace();
            }
        }

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            getAccessTokenGovNet(PreferenceUtils.getUrlLogin(mContext),PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext));
            }
        });


        return vList;
    }

    public int calculateAge(Date birthdate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthdate);
        Calendar today = Calendar.getInstance();

        int yearDifference = today.get(Calendar.YEAR)
                - birth.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
            yearDifference--;
        } else {
            if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < birth
                    .get(Calendar.DAY_OF_MONTH)) {
                yearDifference--;
            }

        }
        return yearDifference;
    }

    public void getAccessTokenGovNet(String URL, final String mEmail, final String mPassword){
        Log.e("--->",URL);
        try {
            final String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addParameter("email",mEmail)
                    .addParameter("password",mPassword)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Toast.makeText(mContext,"offline",Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                            Log.e("--->",serverResponse.getBodyAsString().toString());

                            JSONParser parser_obj = new JSONParser();
                            String token = null;
                            try {
                                JSONObject object = (JSONObject) parser_obj.parse(serverResponse.getBodyAsString().toString());
                                token = (String)object.get("result");
                            } catch (org.json.simple.parser.ParseException e) {
                                e.printStackTrace();
                            }
                           //     memberDetailsGovNet(PreferenceUtils.getUrlGetAllMembers(mContext), PreferenceUtils.getToken(mContext));

                            if(member.getServerType().equals("submit")){
                                memberRegisterGovNet(PreferenceUtils.getUrlCreateMember(mContext),member);
                            }else if(member.getServerType().equals("")){
                         //       memberRegisterGovNet(PreferenceUtils.getUrlCreateMember(mContext),member);
                            }else {
                                Log.e("--->","aaaaaa");
                            }

                        }
                        @Override
                        public void onCancelled(Context context, UploadInfo uploadInfo) {

                        }
                    })
                    .startUpload(); //Starting the upload

        } catch (Exception exc) {
            Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }


/*
    public void memberRegisterVolley(String URL, final MemberPOJO memberDetails){
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONParser parser = new JSONParser();
                        MemberPOJO memberRegister = new MemberPOJO();
                        JSONObject jsonObject=null;
                        try{
                            jsonObject = (JSONObject)parser.parse(response);
                            String result = (String) jsonObject.get("result");
                            Toast.makeText(MemberRegisterActivity.this,result,Toast.LENGTH_SHORT).show();

                            JSONObject memberObject = (JSONObject) jsonObject.get("member");
                            memberRegister.setFirstName(isNull(memberObject,"first_name"));
                            memberRegister.setMiddleName(isNull(memberObject,"middle_name"));
                            memberRegister.setLastName(isNull(memberObject,"last_name"));
                            memberRegister.setEmail(isNull(memberObject,"email"));
                            memberRegister.setUserId(isNull(memberObject,"user_id"));
                            memberRegister.setRollNo(isNull(memberObject,"No"));
                            memberRegister.setGender(isNull(memberObject,"gender"));
                            memberRegister.setMaritalStatus(isNull(memberObject,"marital_status"));
                            memberRegister.setStatus(isNull(memberObject,"status"));
                            memberRegister.setHomePhone(isNull(memberObject,"home_phone"));
                            memberRegister.setMobilePhone(isNull(memberObject,"mobile_phone"));
                            memberRegister.setWorkPhone(isNull(memberObject,"work_phone"));
                            memberRegister.setFingerPrint(isNull(memberObject,"fingerprint"));
                            memberRegister.setDob(isNull(memberObject,"dob"));
                            memberRegister.setPhotoURL(isNull(memberObject,"photo"));
                            memberRegister.setAddress(isNull(memberObject,"address"));
                            memberRegister.setUpdateAt(isNull(memberObject,"updated_at"));
                            memberRegister.setCreateAt(isNull(memberObject,"created_at"));
                            memberRegister.setId(isNull(memberObject,"id"));
                            memberRegister.setNotes(isNull(memberObject,"notes"));
                            dataBaseHelper.insertMemberData(memberRegister);

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }

                        if (isPhone){
                            SerialPortManagerA5.getInstance().closeSerialPort();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                        if (isTablet) {
                            SerialPortManager.getInstance().closeSerialPort();
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                buttonSubmit.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MemberRegisterActivity.this,"error",Toast.LENGTH_LONG).show();

            }
        });
        smr.addFile("photo", getImagePath())
                .addStringParam("token", PreferenceUtils.getToken(MemberRegisterActivity.this))
                .addStringParam("first_name", memberDetails.getFirstName())
                .addStringParam("middle_name", memberDetails.getMiddleName())
                .addStringParam("last_name", memberDetails.getLastName())
                .addStringParam("gender", memberDetails.getGender())
                .addStringParam("marital_status", memberDetails.getMaritalStatus())
                .addStringParam("status", memberDetails.getStatus())
                .addStringParam("mobile_phone", memberDetails.getMobilePhone())
                .addStringParam("dob", memberDetails.getDob())
                .addStringParam("address", memberDetails.getAddress())
                .addStringParam("fingerprint", memberDetails.getFingerPrint())
                .addStringParam("home_phone", memberDetails.getHomePhone())
                .addStringParam("work_phone", memberDetails.getWorkPhone())
                .addStringParam("email", memberDetails.getEmail())
                .addStringParam("notes", memberDetails.getNotes())
                .addStringParam("fingerprint_1", "sdf2");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);

    }
    public void memberUpdateVolley(String URL,MemberPOJO memberDetails) {
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        JSONParser parser = new JSONParser();
                        MemberPOJO memberRegister = new MemberPOJO();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = (JSONObject) parser.parse(response);
                            String result = (String) jsonObject.get("result");
                            Toast.makeText(MemberRegisterActivity.this, result, Toast.LENGTH_SHORT).show();

                            JSONObject memberObject = (JSONObject) jsonObject.get("member");

                            memberRegister.setFirstName(isNull(memberObject, "first_name"));
                            memberRegister.setMiddleName(isNull(memberObject, "middle_name"));
                            memberRegister.setLastName(isNull(memberObject, "last_name"));
                            memberRegister.setEmail(isNull(memberObject, "email"));
                            memberRegister.setUserId(isNull(memberObject, "user_id"));
                            memberRegister.setRollNo(isNull(memberObject, "No"));
                            memberRegister.setGender(isNull(memberObject, "gender"));
                            memberRegister.setMaritalStatus(isNull(memberObject, "marital_status"));
                            memberRegister.setStatus(isNull(memberObject, "status"));
                            memberRegister.setHomePhone(isNull(memberObject, "home_phone"));
                            memberRegister.setMobilePhone(isNull(memberObject, "mobile_phone"));
                            memberRegister.setWorkPhone(isNull(memberObject, "work_phone"));
                            memberRegister.setFingerPrint(isNull(memberObject, "fingerprint"));
                            memberRegister.setDob(isNull(memberObject, "dob"));
                            memberRegister.setPhotoURL(isNull(memberObject, "photo"));
                            memberRegister.setAddress(isNull(memberObject, "address"));
                            memberRegister.setUpdateAt(isNull(memberObject, "updated_at"));
                            memberRegister.setCreateAt(isNull(memberObject, "created_at"));
                            memberRegister.setId(isNull(memberObject, "id"));
                            memberRegister.setNotes(isNull(memberObject, "notes"));
                            //    dataBaseHelper.insertMemberData(memberRegister);
                            dataBaseHelper.updateMemberData(memberRegister);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        Intent intent = new Intent();
                        intent.putExtra("memberRegister", memberRegister);
                        if (isPhone) {
                            SerialPortManagerA5.getInstance().closeSerialPort();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);

                        }
                        if (isTablet) {
                            SerialPortManager.getInstance().closeSerialPort();
                            setResult(Activity.RESULT_OK, intent);
                            finish();
                            progressBar.setVisibility(View.INVISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MemberRegisterActivity.this, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

        if (editImage) {
            smr.addFile("photo", getImagePath())
                    .addStringParam("token", PreferenceUtils.getToken(MemberRegisterActivity.this))
                    .addStringParam("first_name", memberDetails.getFirstName())
                    .addStringParam("middle_name", memberDetails.getMiddleName())
                    .addStringParam("last_name", memberDetails.getLastName())
                    .addStringParam("gender", memberDetails.getGender())
                    .addStringParam("marital_status", memberDetails.getMaritalStatus())
                    .addStringParam("status", memberDetails.getStatus())
                    .addStringParam("mobile_phone", memberDetails.getMobilePhone())
                    .addStringParam("dob", memberDetails.getDob())
                    .addStringParam("address", memberDetails.getAddress())
                    .addStringParam("fingerprint", memberDetails.getFingerPrint())
                    .addStringParam("home_phone", memberDetails.getHomePhone())
                    .addStringParam("work_phone", memberDetails.getWorkPhone())
                    .addStringParam("email", memberDetails.getEmail())
                    .addStringParam("notes", memberDetails.getNotes())
                    .addStringParam("id", memberDetails.getId());
        } else {
            smr.addStringParam("token", PreferenceUtils.getToken(MemberRegisterActivity.this))
                    .addStringParam("first_name", memberDetails.getFirstName())
                    .addStringParam("middle_name", memberDetails.getMiddleName())
                    .addStringParam("last_name", memberDetails.getLastName())
                    .addStringParam("gender", memberDetails.getGender())
                    .addStringParam("marital_status", memberDetails.getMaritalStatus())
                    .addStringParam("status", memberDetails.getStatus())
                    .addStringParam("mobile_phone", memberDetails.getMobilePhone())
                    .addStringParam("dob", memberDetails.getDob())
                    .addStringParam("address", memberDetails.getAddress())
                    .addStringParam("fingerprint", memberDetails.getFingerPrint())
                    .addStringParam("home_phone", memberDetails.getHomePhone())
                    .addStringParam("work_phone", memberDetails.getWorkPhone())
                    .addStringParam("email", memberDetails.getEmail())
                    .addStringParam("notes", memberDetails.getNotes())
                    .addStringParam("id", memberDetails.getId());

        }
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(smr);
    }
*/
    public void memberRegisterGovNet(String URL, final MemberPOJO memberDetails){
        try {
            Log.e("---->",URL);

            final String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addFileToUpload(memberDetails.getPhotoLocalPath(),"photo" )
                    .addParameter("token", PreferenceUtils.getToken(mContext))
                    .addParameter("first_name", memberDetails.getFirstName())
                    .addParameter("middle_name", memberDetails.getMiddleName())
                    .addParameter("last_name", memberDetails.getLastName())
                    .addParameter("gender", memberDetails.getGender())
                    .addParameter("marital_status", memberDetails.getMaritalStatus())
                    .addParameter("status", memberDetails.getStatus())
                    .addParameter("mobile_phone", memberDetails.getMobilePhone())
                    .addParameter("dob", memberDetails.getDob())
                    .addParameter("address", memberDetails.getAddress())
                    .addParameter("fingerprint", memberDetails.getFingerPrint())
                    .addParameter("home_phone", memberDetails.getHomePhone())
                    .addParameter("work_phone", memberDetails.getWorkPhone())
                    .addParameter("email", memberDetails.getEmail())
                    .addParameter("notes", memberDetails.getNotes())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                        }

                        @Override
                        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
                            Log.e("--->",serverResponse.getBodyAsString().toString());

                            JSONParser parser = new JSONParser();
                            MemberPOJO memberRegister = new MemberPOJO();
                            JSONObject jsonObject=null;
                            try{
                                jsonObject = (JSONObject)parser.parse(serverResponse.getBodyAsString().toString());
                                String result = (String) jsonObject.get("result");
                                Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();

                                JSONObject memberObject = (JSONObject) jsonObject.get("member");
                                memberRegister.setFirstName(isNull(memberObject,"first_name"));
                                memberRegister.setMiddleName(isNull(memberObject,"middle_name"));
                                memberRegister.setLastName(isNull(memberObject,"last_name"));
                                memberRegister.setEmail(isNull(memberObject,"email"));
                                memberRegister.setUserId(isNull(memberObject,"user_id"));
                                memberRegister.setRollNo(isNull(memberObject,"No"));
                                memberRegister.setGender(isNull(memberObject,"gender"));
                                memberRegister.setMaritalStatus(isNull(memberObject,"marital_status"));
                                memberRegister.setStatus(isNull(memberObject,"status"));
                                memberRegister.setHomePhone(isNull(memberObject,"home_phone"));
                                memberRegister.setMobilePhone(isNull(memberObject,"mobile_phone"));
                                memberRegister.setWorkPhone(isNull(memberObject,"work_phone"));
                                memberRegister.setFingerPrint(isNull(memberObject,"fingerprint"));
                                memberRegister.setDob(isNull(memberObject,"dob"));
                                memberRegister.setPhotoURL(isNull(memberObject,"photo"));
                                memberRegister.setAddress(isNull(memberObject,"address"));
                                memberRegister.setUpdateAt(isNull(memberObject,"updated_at"));
                                memberRegister.setCreateAt(isNull(memberObject,"created_at"));
                                memberRegister.setId(isNull(memberObject,"id"));
                                memberRegister.setNotes(isNull(memberObject,"notes"));
                                dataBaseHelper.insertMemberData(memberRegister);

                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
/*
                            if (isPhone){
                                SerialPortManagerA5.getInstance().closeSerialPort();
                                finish();
                                progressBar.setVisibility(View.INVISIBLE);

                            }
                            if (isTablet) {
                                SerialPortManager.getInstance().closeSerialPort();
                                finish();
                                progressBar.setVisibility(View.INVISIBLE);
                            }

*/

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

    public void memberUpdateGovNet(String URL, final MemberPOJO memberDetails){
        try {
            final String uploadId = UUID.randomUUID().toString();


            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver = null;

                multipartUploadRequest.addFileToUpload(memberDetails.getPhotoLocalPath(), "photo")
                        .addParameter("token", PreferenceUtils.getToken(mContext))
                        .addParameter("first_name", memberDetails.getFirstName())
                        .addParameter("middle_name", memberDetails.getMiddleName())
                        .addParameter("last_name", memberDetails.getLastName())
                        .addParameter("gender", memberDetails.getGender())
                        .addParameter("marital_status", memberDetails.getMaritalStatus())
                        .addParameter("status", memberDetails.getStatus())
                        .addParameter("mobile_phone", memberDetails.getMobilePhone())
                        .addParameter("dob", memberDetails.getDob())
                        .addParameter("address", memberDetails.getAddress())
                        .addParameter("fingerprint", memberDetails.getFingerPrint())
                        .addParameter("home_phone", memberDetails.getHomePhone())
                        .addParameter("work_phone", memberDetails.getWorkPhone())
                        .addParameter("email", memberDetails.getEmail())
                        .addParameter("notes", memberDetails.getNotes())
                        .addParameter("id", memberDetails.getId())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Log.e("---->", serverResponse.getBodyAsString().toString());
                            }

                            @Override
                            public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {

                                Log.e("--->", serverResponse.getBodyAsString().toString());
                                JSONParser parser = new JSONParser();
                                MemberPOJO memberRegister = new MemberPOJO();
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = (JSONObject) parser.parse(serverResponse.getBodyAsString().toString());
                                    String result = (String) jsonObject.get("result");
                                    Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                                    JSONObject memberObject = (JSONObject) jsonObject.get("member");
                                    memberRegister.setFirstName(isNull(memberObject, "first_name"));
                                    memberRegister.setMiddleName(isNull(memberObject, "middle_name"));
                                    memberRegister.setLastName(isNull(memberObject, "last_name"));
                                    memberRegister.setEmail(isNull(memberObject, "email"));
                                    memberRegister.setUserId(isNull(memberObject, "user_id"));
                                    memberRegister.setRollNo(isNull(memberObject, "No"));
                                    memberRegister.setGender(isNull(memberObject, "gender"));
                                    memberRegister.setMaritalStatus(isNull(memberObject, "marital_status"));
                                    memberRegister.setStatus(isNull(memberObject, "status"));
                                    memberRegister.setHomePhone(isNull(memberObject, "home_phone"));
                                    memberRegister.setMobilePhone(isNull(memberObject, "mobile_phone"));
                                    memberRegister.setWorkPhone(isNull(memberObject, "work_phone"));
                                    memberRegister.setFingerPrint(isNull(memberObject, "fingerprint"));
                                    memberRegister.setDob(isNull(memberObject, "dob"));
                                    memberRegister.setPhotoURL(isNull(memberObject, "photo"));
                                    memberRegister.setAddress(isNull(memberObject, "address"));
                                    memberRegister.setUpdateAt(isNull(memberObject, "updated_at"));
                                    memberRegister.setCreateAt(isNull(memberObject, "created_at"));
                                    memberRegister.setId(isNull(memberObject, "id"));
                                    memberRegister.setNotes(isNull(memberObject, "notes"));
                                    //    dataBaseHelper.insertMemberData(memberRegister);
                                    dataBaseHelper.updateMemberData(memberRegister);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                Intent intent = new Intent();
                                intent.putExtra("memberRegister", memberRegister);
                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Log.e("--->", "caln");
                            }
                        })
                        .startUpload(); //Starting the upload
        } catch (Exception exc) {
            Toast.makeText(mContext, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


}
