package in.co.ashclan.fingerprint;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.HashMap;

import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.ChangePasswordPOJO;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.PreferenceUtils;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    ChangePasswordPOJO changePasswordPOJO;
    ImageView ProfileImage;
    Button customeBtnSubmit;
    Context mcontext;
    TextInputEditText customeOldPassword,customNewPassword,
            customeConformPassword,customAdminFirstName,customAdminLastName,customAdminId;
    DataBaseHelper dataBaseHelper;
    String ImagePath;
    private ProgressDialog nDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mInit();
        mOnClick();
    }

    private void mOnClick() {
        ProfileImage.setOnClickListener(this);
        customeBtnSubmit.setOnClickListener(this);
    }

    private void mInit() {
        mcontext = SettingsActivity.this;
        ProfileImage           = (ImageView)findViewById(R.id.image_profilepic);
        customeOldPassword     = (TextInputEditText) findViewById(R.id.custom_old_password);
        customNewPassword      = (TextInputEditText) findViewById(R.id.custome_new_password);
        customeConformPassword = (TextInputEditText) findViewById(R.id.custome_confirm_password);
        customAdminFirstName   = (TextInputEditText) findViewById(R.id.custome_adminFirstName);
        customAdminLastName    = (TextInputEditText) findViewById(R.id.custome_adminLastName);
        customAdminId          = (TextInputEditText) findViewById(R.id.custom_adnimId);
        customeBtnSubmit       = (Button) findViewById(R.id.custome_btn_submit);

        dataBaseHelper = new DataBaseHelper(mcontext);

        customAdminFirstName.setText(PreferenceUtils.getAdminFirstName(mcontext));
        customAdminLastName.setText(PreferenceUtils.getAdminLastName(mcontext));
        customAdminId.setText(PreferenceUtils.getAdminEmail(mcontext));

        if(!PreferenceUtils.getAdminPhoto(mcontext).equals("")){
            try {
                Picasso.with(mcontext)
                        .load("file://"+PreferenceUtils.getAdminPhoto(mcontext))
                        .config(Bitmap.Config.RGB_565)
                        .fit()
                        .centerCrop()
                        .into(ProfileImage);

            }catch (Exception e){
                ProfileImage.setImageResource(R.drawable.ic_church);
                e.printStackTrace();
            }
        }else
        {
            ProfileImage.setImageResource(R.drawable.ic_church);
        }
    }

    @Override
    public void onClick(View view) {
        if(view == ProfileImage)
        {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)//enable image guidlines
                    .setAspectRatio(1,1)  //image will be suqare//
                    .start(this);
        }

        if(view == customeBtnSubmit)
        {
            ChangepasswpordMethod();
            Toast.makeText(mcontext, "Saved Data of Change Password....", Toast.LENGTH_SHORT).show();
        }


    }

    private void ChangepasswpordMethod() {
        changePasswordPOJO = new ChangePasswordPOJO();

        changePasswordPOJO.setUserid(PreferenceUtils.getUserId(mcontext));
        changePasswordPOJO.setOldpassword(customeOldPassword.getText().toString());
        changePasswordPOJO.setNewpassword(customNewPassword.getText().toString());
        changePasswordPOJO.setComformpassword(customeConformPassword.getText().toString());
        changePasswordPOJO.setAdminfirstname(customAdminFirstName.getText().toString());
        changePasswordPOJO.setAdminlastname(customAdminLastName.getText().toString());
        changePasswordPOJO.setAdminid(customAdminId.getText().toString());
       // changePasswordPOJO.setPhotoUrl(ProfileImage.getResources().toString());
        try{
            if(!getImagePath().equals("")||!getImagePath().equals(null))
            {
                changePasswordPOJO.setPhotoUrl(getImagePath());
            }else{
                changePasswordPOJO.setPhotoUrl("");
            }
            PreferenceUtils.setAdminPhoto(mcontext,getImagePath());

        }catch (Exception e)
        {
            e.printStackTrace();
        }



        dataBaseHelper.UpdatePassword(changePasswordPOJO);
      //  profileimage.setImageURI(Uri.parse(workerModel.getImageUrl().toString()));

        finish();


        /*Glide.with(mcontext)
                .load(changePasswordPOJO.getPhotoUrl().toString())
                .into(ProfileImage);
        setImagePath(changePasswordPOJO.getPhotoUrl());*/

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Uri imageUri = data.getData();
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                Uri resultUri = result.getUri();
                //set image choosed from gallery to image view
                ProfileImage.setImageURI(resultUri);
              //  ProfileImage.setImageURI(resultUri);
                setImagePath(resultUri.getPath());
            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getImagePath() {
        changePasswordPOJO.setPhotoUrl(ImagePath);
        return ImagePath;
    }
    private void setImagePath(String photoUrl) {
        ImagePath = photoUrl;
    }

    public class GetAccessTokenTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String email,password;


        GetAccessTokenTask(Context mContext,String URL,String email,String password) {
            this.mContext = mContext;
            this.email=email;
            this.password=password;
            this.URL = URL;

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

         /*   HashMap<String, String> postData = new HashMap<>();
            postData.put("email", email);
            postData.put("password", password);
            String url = "https://bwc.pentecostchurch.org/api/login";
            String urls = "http://52.172.221.235:8983/api/login";
            String json_output = performPostCall(url, postData);
            return json_output;*/
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

                    UpdatePasswrodTask updatePasswrodTask = new UpdatePasswrodTask(mContext,PreferenceUtils.getUrlUpdateUser(mContext),token,changePasswordPOJO);
                    updatePasswrodTask.execute();

                }else{
                    Toast.makeText(mContext,"Something went Wrong",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("--->", e.toString());
                //progressBar.setVisibility(View.GONE);
                e.printStackTrace();
            }

        }
        @Override
        protected void onCancelled() {
        }
    }

    public class UpdatePasswrodTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String token,password;
        private ChangePasswordPOJO passwordPOJO;

        UpdatePasswrodTask(Context mContext,String URL,String token,ChangePasswordPOJO eventPOJO) {
            this.mContext = mContext;
            this.URL=URL;
            this.passwordPOJO = eventPOJO;
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
            postData.put("id", passwordPOJO.getUserid());
            postData.put("email", passwordPOJO.getAdminid());
            postData.put("password", passwordPOJO.getNewpassword());
            postData.put("first_name", passwordPOJO.getAdminfirstname());
            postData.put("last_name", passwordPOJO.getAdminlastname());


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

                ChangePasswordPOJO event = new ChangePasswordPOJO();

                event.setUserid(String.valueOf(object.get("id")));
                event.setAdminid(isNull(object,"email"));
                event.setAdminfirstname(isNull(object,"first_name"));
                event.setAdminlastname(isNull(object,"last_name"));

                Log.e("D--->",event.toString());
                //   eventList.add(event);
                dataBaseHelper.UpdatePassword(event);
                nDialog.dismiss();
                Toast.makeText(mContext, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
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
}
