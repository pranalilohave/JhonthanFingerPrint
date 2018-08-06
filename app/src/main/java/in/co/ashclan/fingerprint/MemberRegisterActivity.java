package in.co.ashclan.fingerprint;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadata;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.request.SimpleMultiPartRequest;
//import com.android.volley.toolbox.Volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.fgtit.fpcore.FPMatch;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadServiceBroadcastReceiver;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import android_serialport_api.AsyncFingerprint;
import android_serialport_api.AsyncFingerprintA5;
import android_serialport_api.SerialPortManager;
import android_serialport_api.SerialPortManagerA5;
import fr.ganfra.materialspinner.MaterialSpinner;
import in.co.ashclan.database.DataBaseHelper;
//import in.co.ashclan.fgtit.fpcore.FPMatch;
import in.co.ashclan.database.DataBaseHelperOffline;
import in.co.ashclan.model.EventPOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.Constants;
import in.co.ashclan.utils.PreferenceUtils;
import in.co.ashclan.utils.Utility;
import in.co.ashclan.utils.WebServiceCall;

import static in.co.ashclan.utils.WebServiceCall.performPostCall;
import static java.security.AccessController.getContext;

public class MemberRegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextFirstName,editTextMiddleName,editTextLastname,editTextEmail,editTextHomePhone,
            editTextMobilePhone,editTextWorkPhone,editTextDOB,editTextAddress,editTextDescription;
    private ImageView imageViewFingerPrint1,imageViewFingerPrint2,imageViewFingerPrint3;
    private MaterialSpinner msGender,msMartialStatus,msStatus;
    private Button buttonSubmit,buttonOffline;
    private ProgressBar progressBar;

    private byte[] model1=new byte[512];
    private byte[] model2=new byte[512];

    private boolean isEnroll1=false;
    private boolean isEnroll2=false;
    private boolean isUpEnroll1=false;
    private boolean isUpEnroll2=false;

    private boolean isFpDuplicate=false;

    private boolean isTablet=false;
    private boolean isPhone=false;

    private AsyncFingerprint registerFingerprint;
    private AsyncFingerprintA5 registerFingerprintA5;

    private Dialog fpDialog=null;
    private int iFinger=0;
    private int count;
    private ImageView fpImage;
    private TextView fpStatusText;

    DataBaseHelper dataBaseHelper;
    DataBaseHelperOffline dataBaseHelperOffline;

    private boolean bcheck=false;
    private boolean	bIsUpImage=true;
    private boolean	bIsCancel=false;

    List<MemberPOJO> list = new ArrayList<MemberPOJO>();
    MemberPOJO memberDetails = new MemberPOJO();
    String onOffLine="";
    private String fpStrByte1,fpStrByte2;
    private byte[] fpByte1;
    private byte[] fpByte2;
    private int mYear, mMonth, mDay;

    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, CROP_IMAGE = 2;
    private String userChoosenTask;
    private Bitmap bitmapImage;
    private File destination;
    private String imagePath;
    String type;

    ImageLoaderConfiguration loaderConfiguration;
    ImageLoader imageLoader = ImageLoader.getInstance();
    boolean editImage=false;

    Context mContext = MemberRegisterActivity.this;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_register);
        FPMatch.getInstance().InitMatch();
        inits();
        dataBaseHelper = new DataBaseHelper(this);
        dataBaseHelperOffline =new DataBaseHelperOffline((this));

        list = dataBaseHelper.getAllMembers();
        type = (String)getIntent().getStringExtra("type");

        if(PreferenceUtils.getInternetAccess(mContext)){
            onOffLine="";
        }else{
            onOffLine="";//"Offline";
        }

        switch (type){
            case "edit":

                MemberPOJO m = (MemberPOJO) getIntent().getSerializableExtra("member");
                memberDetails = dataBaseHelper.getMemberDetails(m.getId());

                editTextFirstName.setText(memberDetails.getFirstName());
                editTextMiddleName.setText(memberDetails.getMiddleName());
                editTextLastname.setText(memberDetails.getLastName());
                editTextEmail.setText(memberDetails.getEmail());
                editTextHomePhone.setText(memberDetails.getHomePhone());
                editTextMobilePhone.setText(memberDetails.getMobilePhone());
                editTextWorkPhone.setText(memberDetails.getWorkPhone());
                editTextDOB.setText(memberDetails.getDob());
                editTextAddress.setText(memberDetails.getAddress());
                editTextDescription.setText(memberDetails.getNotes());
                getGender(memberDetails.getGender());
                getStatus(memberDetails.getStatus());
                getMaritalStatus(memberDetails.getMaritalStatus());
                if(!memberDetails.getFingerPrint().equals("")){
                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.green));
                    isUpEnroll1=true;
                    isEnroll1=true;
                }
                if(!memberDetails.getFingerPrint1().equals("")){
                    imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.green));
                    isUpEnroll2=true;
                    isEnroll2=true;
                }

                //"http://52.172.221.235:8983/uploads/"
                if (null!=memberDetails.getPhotoURL()&&!memberDetails.getPhotoURL().equals("")) {
                    //editImage=true;
                    String imgURL =  PreferenceUtils.getUrlUploadImage(MemberRegisterActivity.this)+ memberDetails.getPhotoURL();
                   /* imageLoader.displayImage(imgURL, imageViewFingerPrint2, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                            //    imageLoader.displayImage("http://52.172.221.235:8983/uploads/" + defaultIcon, imageView);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            //     editImage = true;


                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {

                        }
                    });*/

                  /* Glide.with(mContext)
                            .load(memberDetails.getPhotoURL())
                            .into(imageViewFingerPrint2);*/
                    Picasso.get().load(imgURL).into(imageViewFingerPrint2);
                }

                buttonOffline.setVisibility(View.VISIBLE);
                buttonSubmit.setText("UPDATE "+onOffLine);
                break;

            case "register":
                buttonOffline.setVisibility(View.VISIBLE);
                buttonSubmit.setText("Submit "+onOffLine);
                break;
        }


        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {

            Toast.makeText(MemberRegisterActivity.this,"Tablet",Toast.LENGTH_LONG).show();
            isTablet=true;

            try {
                registerFingerprint = SerialPortManager.getInstance().getNewAsyncFingerprint();
                FPInitFP07();
            }catch (UnsatisfiedLinkError e){
                Log.e("--->",e.toString());
            }
        }
        else {
            Toast.makeText(MemberRegisterActivity.this,"Phone",Toast.LENGTH_LONG).show();
            isPhone=true;
            try {
                registerFingerprintA5 = SerialPortManagerA5.getInstance().getNewAsyncFingerprint();
                FPInitA5();
            }catch(UnsatisfiedLinkError e){
                Log.e("--->",e.toString());
            }
        }
    }
    public void inits(){
        editTextFirstName=(EditText)findViewById(R.id.first_name);
        editTextMiddleName=(EditText)findViewById(R.id.middle_name);
        editTextLastname=(EditText)findViewById(R.id.last_name);
        editTextEmail=(EditText)findViewById(R.id.email);
        editTextHomePhone=(EditText)findViewById(R.id.home_phone);
        editTextMobilePhone=(EditText)findViewById(R.id.mobile_phone);
        editTextWorkPhone=(EditText)findViewById(R.id.work_phone);
        editTextDOB=(EditText)findViewById(R.id.dob);
        editTextAddress=(EditText)findViewById(R.id.address);
        editTextDescription=(EditText)findViewById(R.id.description);

        imageViewFingerPrint1=(ImageView)findViewById(R.id.imageView1);
        imageViewFingerPrint2=(ImageView)findViewById(R.id.imageView2);
        imageViewFingerPrint3=(ImageView)findViewById(R.id.imageView3);

        msGender=(MaterialSpinner)findViewById(R.id.spinner_gender);
        msMartialStatus=(MaterialSpinner)findViewById(R.id.spinner_martial_status);
        msStatus=(MaterialSpinner)findViewById(R.id.spinner_status);

        buttonSubmit=(Button)findViewById(R.id.button_submit);
        buttonOffline=(Button)findViewById(R.id.button_offline_submit);

        progressBar=(ProgressBar)findViewById(R.id.progress_bar_member_register);


        imageViewFingerPrint1.setOnClickListener(this);
        imageViewFingerPrint2.setOnClickListener(this);
        imageViewFingerPrint3.setOnClickListener(this);

        editTextDOB.setOnClickListener(this);
        buttonSubmit.setOnClickListener(this);
        buttonOffline.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        if (view==imageViewFingerPrint1){
            isUpEnroll1=false;
            FPDialog(1);
        }
        if (view==imageViewFingerPrint3){
            isUpEnroll2=false;
            FPDialog(2);
        }
        if (view==imageViewFingerPrint2){
            //selectImage();
            cropImage();
        }
        if (view==editTextDOB){
            dateDialog();
        }
        if (view==buttonSubmit){
            submitMemberRegistration();

        }
        if (view==buttonOffline){
            switch (type){
                case "edit":
                    offlineSubmitMemberUpdate();
                    break;
                case "register":
                    offlineSubmitMemberRegistration();
                    break;
            }
        }
    }
    private void dateDialog(){
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

                        editTextDOB.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }
    public void FPDialog(int i){
        iFinger = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(MemberRegisterActivity.this);
        builder.setTitle("Registration FingerPrint");
        final LayoutInflater inflater = LayoutInflater.from(MemberRegisterActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_enrolfinger,null);
        fpImage = (ImageView) dialogView.findViewById(R.id.dialog_image);
        fpStatusText = (TextView) dialogView.findViewById(R.id.dialog_text);
        builder.setView(dialogView);
        builder.setCancelable(false);
     /*   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
       */
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
        fpDialog = builder.create();
        fpDialog.setCanceledOnTouchOutside(false);
        fpDialog.show();
        FPProcess();
    }
    public void FPProcess(){
        count=1;
        fpStatusText.setText("Please Press Finger");
        try {
            Thread.currentThread();
            Thread.sleep(200);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        if(isPhone) {
            registerFingerprintA5.FP_GetImage();
        }else if(isTablet){
            registerFingerprint.FP_GetImage();
        }
    }
    private void FPInitA5() {

        registerFingerprintA5.setOnGetImageListener(new AsyncFingerprintA5.OnGetImageListener() {
            @Override
            public void onGetImageSuccess() {
                if (!bIsCancel) {
                    if (bcheck) {
                        registerFingerprintA5.FP_GetImage();
                    } else {
                        if (bIsUpImage) {
                            registerFingerprintA5.FP_UpImage();
                            fpStatusText.setText(getString(R.string.txt_fpdisplay));
                        } else {
                            fpStatusText.setText(getString(R.string.txt_fpprocess));
                            registerFingerprintA5.FP_GenChar(count);
                        }
                    }
                }
            }

            @Override
            public void onGetImageFail() {
                if (!bIsCancel) {
                    if (bcheck) {
                        bcheck = false;
                        fpStatusText.setText(getString(R.string.txt_fpplace));
                        registerFingerprintA5.FP_GetImage();
                        count++;
                    } else {
                        registerFingerprintA5.FP_GetImage();
                    }
                }
            }
        });

        registerFingerprintA5.setOnUpImageListener(new AsyncFingerprintA5.OnUpImageListener() {
            @Override
            public void onUpImageSuccess(byte[] data) {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                fpImage.setImageBitmap(image);
                //fpImage.setBackgroundDrawable(new BitmapDrawable(image));
                registerFingerprintA5.FP_GenChar(count);
                fpStatusText.setText(getString(R.string.txt_fpprocess));
            }

            @Override
            public void onUpImageFail() {
            }
        });

        registerFingerprintA5.setOnGenCharListener(new AsyncFingerprintA5.OnGenCharListener() {
            @Override
            public void onGenCharSuccess(int bufferId) {
                if (bufferId == 1) {
                    bcheck = true;
                    fpStatusText.setText(getString(R.string.txt_fplift));
                    registerFingerprintA5.FP_GetImage();
                } else if (bufferId == 2) {
                    registerFingerprintA5.FP_RegModel();
                }
            }

            @Override
            public void onGenCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpfail));
            }
        });

        registerFingerprintA5.setOnRegModelListener(new AsyncFingerprintA5.OnRegModelListener() {

            @Override
            public void onRegModelSuccess() {
                registerFingerprintA5.FP_UpChar();
                fpStatusText.setText(getString(R.string.txt_fpenrolok));
            }

            @Override
            public void onRegModelFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail)+"2");
                fpDialog.cancel();

                if (iFinger == 1) {
                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                }else {
                    imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(MemberRegisterActivity.this,"Enrol Failed",Toast.LENGTH_LONG).show();
            }
        });

        registerFingerprintA5.setOnUpCharListener(new AsyncFingerprintA5.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {

                for (MemberPOJO member:list){
                    if(member.getFingerPrint()!= null){
                        byte[] byt=Base64.decode(member.getFingerPrint(),Base64.DEFAULT);
                        if (FPMatch.getInstance().MatchTemplate(model,byt) > 60){
                            fpStatusText.setText(getString(R.string.txt_fpduplicate));
                            imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                            fpDialog.cancel();
                            return;
                        }
                    }
                }
                if (iFinger == 1) {
                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.green));
                    System.arraycopy(model, 0, MemberRegisterActivity.this.model1, 0, 512);
                    isEnroll1 = true;
                } else {
                    imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.green));
                    System.arraycopy(model, 0, MemberRegisterActivity.this.model2, 0, 512);
                    isEnroll2 = true;
                }
                fpStatusText.setText(getString(R.string.txt_fpenrolok));
                fpDialog.cancel();
            }

            @Override
            public void onUpCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail));
                if (iFinger == 1) {
                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                }else {
                    imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                Toast.makeText(MemberRegisterActivity.this,"Enrol Failed",Toast.LENGTH_LONG).show();
                fpDialog.cancel();
            }
        });
    }
    private void FPInitFP07() {

        registerFingerprint.setOnGetImageListener(new AsyncFingerprint.OnGetImageListener() {
            @Override
            public void onGetImageSuccess() {
                if (!bIsCancel) {
                    if (bcheck) {
                        registerFingerprint.FP_GetImage();
                    } else {
                        if (bIsUpImage) {
                            registerFingerprint.FP_UpImage();
                            fpStatusText.setText(getString(R.string.txt_fpdisplay));
                        } else {
                            fpStatusText.setText(getString(R.string.txt_fpprocess));
                            registerFingerprint.FP_GenChar(count);
                        }
                    }
                }
            }

            @Override
            public void onGetImageFail() {
                if (!bIsCancel) {
                    if (bcheck) {
                        bcheck = false;
                        fpStatusText.setText(getString(R.string.txt_fpplace));
                        registerFingerprint.FP_GetImage();
                        count++;
                    } else {
                        registerFingerprint.FP_GetImage();
                    }
                }
            }
        });

        registerFingerprint.setOnUpImageListener(new AsyncFingerprint.OnUpImageListener() {
            @Override
            public void onUpImageSuccess(byte[] data) {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                fpImage.setImageBitmap(image);
                //fpImage.setBackgroundDrawable(new BitmapDrawable(image));
                registerFingerprint.FP_GenChar(count);
                fpStatusText.setText(getString(R.string.txt_fpprocess));
            }

            @Override
            public void onUpImageFail() {
            }
        });

        registerFingerprint.setOnGenCharListener(new AsyncFingerprint.OnGenCharListener() {
            @Override
            public void onGenCharSuccess(int bufferId) {
                if (bufferId == 1) {
                    bcheck = true;
                    fpStatusText.setText(getString(R.string.txt_fplift));
                    registerFingerprint.FP_GetImage();
                } else if (bufferId == 2) {
                    registerFingerprint.FP_RegModel();
                }
            }

            @Override
            public void onGenCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpfail));
            }
        });

        registerFingerprint.setOnRegModelListener(new AsyncFingerprint.OnRegModelListener() {

            @Override
            public void onRegModelSuccess() {
                registerFingerprint.FP_UpChar();
                fpStatusText.setText(getString(R.string.txt_fpenrolok));
            }

            @Override
            public void onRegModelFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail)+"2");

                if (iFinger == 1) {
                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                }else {
                    imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(MemberRegisterActivity.this,"Enrol Failed",Toast.LENGTH_LONG).show();
                fpDialog.cancel();

            }
        });

        registerFingerprint.setOnUpCharListener(new AsyncFingerprint.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {

                for (MemberPOJO member:list){
                    if(member.getFingerPrint()!= null){
                        byte[] byt=Base64.decode(member.getFingerPrint(),Base64.DEFAULT);
                        if (FPMatch.getInstance().MatchTemplate(model,byt) > 60){
                            fpStatusText.setText(getString(R.string.txt_fpduplicate));
                            if(iFinger==1) {
                                imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                            }else{
                                imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                            }
                            Toast.makeText(mContext,"Duplicate",Toast.LENGTH_LONG).show();
                            fpDialog.cancel();
                            return;
                        }
                    }

                    if(member.getFingerPrint1()!=null) {
                        byte[] byt=Base64.decode(member.getFingerPrint1(),Base64.DEFAULT);
                        if (FPMatch.getInstance().MatchTemplate(model,byt) > 60){
                            fpStatusText.setText(getString(R.string.txt_fpduplicate));
                            if(iFinger==1) {
                                imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                            }else{
                                imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                            }
                            Toast.makeText(mContext,"Duplicate",Toast.LENGTH_LONG).show();
                            fpDialog.cancel();
                            return;
                        }
                    }
                }
                if (iFinger == 1) {
                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.green));
                    System.arraycopy(model, 0, MemberRegisterActivity.this.model1, 0, 512);
                    isEnroll1 = true;
                } else {
                    imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.green));
                    System.arraycopy(model, 0, MemberRegisterActivity.this.model2, 0, 512);
                    isEnroll2 = true;
                }
                fpStatusText.setText(getString(R.string.txt_fpenrolok));
                fpDialog.cancel();
            }

            @Override
            public void onUpCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail));

                if (iFinger == 1) {
                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                }else {
                    imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(MemberRegisterActivity.this,"Enrol Failed",Toast.LENGTH_LONG).show();
                fpDialog.cancel();
            }
        });
    }
    public boolean utilsCheck(){

        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(editTextFirstName.getText())){
            editTextFirstName.setError("Please enter first Name");
            focusView=editTextFirstName;
            cancel=true;
        }

        if (TextUtils.isEmpty(editTextLastname.getText())){
            editTextLastname.setError("Please enter Last Name");
            focusView=editTextLastname;
            cancel=true;
        }
        /*
        if (TextUtils.isEmpty(editTextEmail.getText()) && !isEmailValid(String.valueOf(editTextEmail.getText())) ){
            editTextEmail.setError("Please enter Email");
            focusView=editTextEmail;
            cancel=true;
        }
        */
        /*
        if (TextUtils.isEmpty(editTextHomePhone.getText())){
            editTextHomePhone.setError("Please enter Home Phone");
            focusView=editTextHomePhone;
            cancel=true;
        }
        */
        /*
        if (TextUtils.isEmpty(editTextMobilePhone.getText())){
            editTextMobilePhone.setError("Please enter Mobile Phone");
            focusView=editTextMobilePhone;
            cancel=true;
        }
        */
        /*
        if (TextUtils.isEmpty(editTextWorkPhone.getText())){
            editTextWorkPhone.setError("Please enter Work Phone");
            focusView=editTextWorkPhone;
            cancel=true;
        }
        */

        if (TextUtils.isEmpty(editTextDOB.getText())&&!isValidDate(editTextDOB.getText().toString())){
            editTextDOB.setError("Please enter in dateformat");
            focusView=editTextDOB;
            cancel=true;
        }
        if(msGender.getSelectedItemPosition()==0){
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
        }

        if(!isEnroll1){
            imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
            cancel=true;
        }
        if(!isEnroll2){
            imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
            cancel=true;
        }

        switch (type) {
            case"edit":
                break;
            case "register":
                if (getImagePath() == "" || getImagePath() == null) {
                    imageViewFingerPrint2.setBackgroundColor(getResources().getColor(R.color.red));
                    cancel = true;
                }
                break;

        }
        return cancel;
    }
    private boolean isEmailValid(String email) {
        return email.contains("@")&&email.contains(".");
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
    public void cropImage(){
        Intent intent = new Intent(mContext,CropImageActivity.class);
        startActivityForResult(intent,CROP_IMAGE);
    }
    public void offlineSubmitMemberRegistration(){
        if(!utilsCheck()) {
            memberDetails.setFirstName(editTextFirstName.getText().toString());
            memberDetails.setLastName(editTextLastname.getText().toString());
            memberDetails.setMiddleName(editTextMiddleName.getText().toString());
            memberDetails.setEmail(editTextEmail.getText().toString());
            memberDetails.setGender(msGender.getSelectedItem().toString());//check out onces
            memberDetails.setMaritalStatus(msMartialStatus.getSelectedItem().toString());
            memberDetails.setStatus(msStatus.getSelectedItem().toString());
            memberDetails.setHomePhone(editTextHomePhone.getText().toString());
            memberDetails.setMobilePhone(editTextMobilePhone.getText().toString());
            memberDetails.setWorkPhone(editTextWorkPhone.getText().toString());
            memberDetails.setDob(editTextAddress.getText().toString());
            memberDetails.setAddress(editTextAddress.getText().toString());
            memberDetails.setNotes(editTextDescription.getText().toString());
            memberDetails.setDob(editTextDOB.getText().toString());
            memberDetails.setPhotoLocalPath(getImagePath());
            if (isEnroll1) {
                fpByte1 = new byte[model1.length];
                System.arraycopy(model1, 0, fpByte1, 0, model1.length);
                fpStrByte1 = Base64.encodeToString(fpByte1, Base64.DEFAULT);
                imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.green));

                memberDetails.setFingerPrint(fpStrByte1);
            }
            
            if (isEnroll2) {
                fpByte2 = new byte[model2.length];
                System.arraycopy(model2, 0, fpByte2, 0, model2.length);
                fpStrByte2 = Base64.encodeToString(fpByte2, Base64.DEFAULT);
                imageViewFingerPrint2.setColorFilter(getResources().getColor(R.color.green));

                memberDetails.setFingerPrint2(fpStrByte2);
            }

            progressBar.setVisibility(View.VISIBLE);

            memberDetails.setServerType(Constants.SUBMIT);
            dataBaseHelperOffline.insertOfflineMemberData(memberDetails);
            finish();
        }else {
            Toast.makeText(mContext,"something is missing",Toast.LENGTH_LONG).show();
        }
    }
    public void offlineSubmitMemberUpdate(){
        if (!utilsCheck()) {
            memberDetails.setFirstName(editTextFirstName.getText().toString());
            memberDetails.setLastName(editTextLastname.getText().toString());
            memberDetails.setMiddleName(editTextMiddleName.getText().toString());
            memberDetails.setEmail(editTextEmail.getText().toString());
            memberDetails.setGender(msGender.getSelectedItem().toString());//check out onces
            memberDetails.setMaritalStatus(msMartialStatus.getSelectedItem().toString());
            memberDetails.setStatus(msStatus.getSelectedItem().toString());
            memberDetails.setHomePhone(editTextHomePhone.getText().toString());
            memberDetails.setMobilePhone(editTextMobilePhone.getText().toString());
            memberDetails.setWorkPhone(editTextWorkPhone.getText().toString());
            memberDetails.setDob(editTextAddress.getText().toString());
            memberDetails.setAddress(editTextAddress.getText().toString());
            memberDetails.setNotes(editTextDescription.getText().toString());
            memberDetails.setDob(editTextDOB.getText().toString());
            memberDetails.setPhotoLocalPath(getImagePath());
            if (!isUpEnroll1) {
                fpByte1 = new byte[model1.length];
                System.arraycopy(model1, 0, fpByte1, 0, model1.length);
                fpStrByte1 = Base64.encodeToString(fpByte1, Base64.DEFAULT);
                imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.green));
                memberDetails.setFingerPrint(fpStrByte1);
            }
            if (!isUpEnroll2) {
                fpByte2 = new byte[model2.length];
                System.arraycopy(model2, 0, fpByte2, 0, model2.length);
                fpStrByte2 = Base64.encodeToString(fpByte2, Base64.DEFAULT);
                imageViewFingerPrint2.setColorFilter(getResources().getColor(R.color.green));
                memberDetails.setFingerPrint2(fpStrByte2);
            }

            progressBar.setVisibility(View.VISIBLE);


            if(editImage) {
                memberDetails.setServerType(Constants.UPDATEWITHIMAGE);
            }else {
                memberDetails.setServerType(Constants.UDATEWITHOUTIMAGE);
            }
            dataBaseHelperOffline.insertOfflineMemberData(memberDetails);

            finish();
        }else {
            Toast.makeText(mContext,"something is missing",Toast.LENGTH_LONG).show();
        }
    }
    public void submitMemberRegistration(){
        boolean isDuplicated=false;

        if (utilsCheck()){
            Toast.makeText(mContext,"something is missing",Toast.LENGTH_LONG).show();
        }else {
            memberDetails.setFirstName(editTextFirstName.getText().toString());
            memberDetails.setLastName(editTextLastname.getText().toString());
            memberDetails.setMiddleName(editTextMiddleName.getText().toString());
            memberDetails.setEmail(editTextEmail.getText().toString());
            memberDetails.setGender(msGender.getSelectedItem().toString());//check out onces
            memberDetails.setMaritalStatus(msMartialStatus.getSelectedItem().toString());
            memberDetails.setStatus(msStatus.getSelectedItem().toString());
            memberDetails.setHomePhone(editTextHomePhone.getText().toString());
            memberDetails.setMobilePhone(editTextMobilePhone.getText().toString());
            memberDetails.setWorkPhone(editTextWorkPhone.getText().toString());
            memberDetails.setDob(editTextAddress.getText().toString());
            memberDetails.setAddress(editTextAddress.getText().toString());
            memberDetails.setNotes(editTextDescription.getText().toString());
            memberDetails.setDob(editTextDOB.getText().toString());

            if (isEnroll1&&!isUpEnroll1) {
                fpByte1 = new byte[model1.length];
                System.arraycopy(model1, 0, fpByte1, 0, model1.length);

                fpStrByte1 = Base64.encodeToString(fpByte1, Base64.DEFAULT);

                imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.green));
                memberDetails.setFingerPrint(fpStrByte1);
                PreferenceUtils.setTestMatch(mContext,fpStrByte1);
            }
            if (isEnroll2&&!isUpEnroll2) {
                fpByte2 = new byte[model2.length];
                System.arraycopy(model2, 0, fpByte2, 0, model2.length);
                fpStrByte2 = Base64.encodeToString(fpByte2, Base64.DEFAULT);
                imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.green));

                memberDetails.setFingerPrint1(fpStrByte2);
            }

            progressBar.setVisibility(View.VISIBLE);
            //"http://52.172.221.235:8983/api/create_member"

            for (MemberPOJO member:list){
                byte[] tmp1=new byte[256];
                if(!member.getFingerPrint().equals("")){
                    byte[] tmp2 = Base64.decode(member.getFingerPrint(),Base64.DEFAULT);
                    if(tmp2!=null){
                        System.arraycopy(tmp2, 0, tmp1, 0, 256);
                        Log.e("---->",member.getId()+"  -->"+member.getFingerPrint()+" ><><><><><>< "+(FPMatch.getInstance().MatchTemplate(fpByte1,tmp1)>60));

                        if(FPMatch.getInstance().MatchTemplate(fpByte1,tmp1)>60){
                            Toast.makeText(MemberRegisterActivity.this,"inside",Toast.LENGTH_LONG).show();
                            Log.e("----> inside",member.getFingerPrint());
                            Log.e("----> inside",member.getId());

                            isDuplicated = true;
                            break;
                        }
                        if(FPMatch.getInstance().MatchTemplate(fpByte2,tmp1)>60){
                            Toast.makeText(MemberRegisterActivity.this,"inside",Toast.LENGTH_LONG).show();
                            Log.e("----> inside",member.getFingerPrint());
                            Log.e("----> inside",member.getId());

                            isDuplicated = true;
                            break;
                        }

                        System.arraycopy(tmp2, 256, tmp1, 0, 256);

                        if(FPMatch.getInstance().MatchTemplate(fpByte1,tmp1)>60){
                            isDuplicated=true;
                            break;
                        }

                        if(FPMatch.getInstance().MatchTemplate(fpByte2,tmp1)>60){
                            isDuplicated=true;
                            break;
                        }
                    }
                }
            }

            if(!isDuplicated) {
                // getAccessTokenGovNet(PreferenceUtils.getUrlLogin(mContext),PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext));

                GetAccessTokenTask aTask = new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                        PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),memberDetails);
                aTask.execute();
            }else {
                imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(MemberRegisterActivity.this,"Duplicated FingerPrint",Toast.LENGTH_LONG).show();
            }

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MemberRegisterActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(MemberRegisterActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
            else if (requestCode == CROP_IMAGE)
                onCropImageResult(data);
        }
    }
    private void onCropImageResult(Intent data) {
        //****************************
        //Bitmap bmp;
        byte[] byteArray = data.getByteArrayExtra("bytesArray");
        String imagePath = data.getStringExtra("ImagePath");
        //bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        //imageView.setImageBitmap(bmp);
        ///String str = data.getStringExtra("ImagePath");
        //Toast.makeText(mContext,str,Toast.LENGTH_LONG).show();
        //Log.e("--->",str);



        //******************************
        bitmapImage = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String fileName=System.currentTimeMillis() + ".jpg";
        destination = new File(Environment.getExternalStorageDirectory(),
                fileName);
        //    setImagePath(fileName);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(MemberRegisterActivity.this,destination.getAbsolutePath(),Toast.LENGTH_LONG).show();
        imageViewFingerPrint2.setImageBitmap(bitmapImage);
       // Picasso.get().load(imgURL).fit().into(imageViewFingerPrint2);
        //     memberDetails.setPhotoLocalPath(BitMapToString(bitmapImage));
        setImagePath(destination.getAbsolutePath());
        //Picasso.get().load(getImagePath()).into(imageViewFingerPrint2);

        /**************************************************************************************/
    }
    private void onCaptureImageResult(Intent data) {
        bitmapImage = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String fileName=System.currentTimeMillis() + ".jpg";
        destination = new File(Environment.getExternalStorageDirectory(),
                fileName);
        //    setImagePath(fileName);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(MemberRegisterActivity.this,destination.getAbsolutePath(),Toast.LENGTH_LONG).show();
        imageViewFingerPrint2.setImageBitmap(bitmapImage);
        //     memberDetails.setPhotoLocalPath(BitMapToString(bitmapImage));
        setImagePath(destination.getAbsolutePath());
        Picasso.get().load(getImagePath()).into(imageViewFingerPrint2);
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        bitmapImage=null;
        String path ="";
        if (data != null) {
            try {
                bitmapImage = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
                cursor.moveToFirst();
                String document_id = cursor.getString(0);
                document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
                cursor.close();

                cursor = getContentResolver().query(
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);


                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

                cursor.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Toast.makeText(MemberRegisterActivity.this,path,Toast.LENGTH_LONG).show();
        imageViewFingerPrint2.setImageBitmap(bitmapImage);
        //   memberDetails.setPhotoLocalPath(BitMapToString(bitmapImage));
        setImagePath(path);
        Picasso.get().load(path).into(imageViewFingerPrint2);
    }
    public void setImagePath(String path){
        editImage=true;
        imagePath=path;
    }
    public String getImagePath(){
        memberDetails.setPhotoLocalPath(imagePath);
        return imagePath;
    }
    public String isNull(JSONObject object, String parma){
        return object.get(parma)!=null?object.get(parma).toString():"";
    }
    public String isNull(JSONObject object, String parma,String dafualtStr){
        return object.get(parma)!=null?object.get(parma).toString():dafualtStr;
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public void getGender(String str) {
        List<String> l = Arrays.asList(getResources().getStringArray(R.array.array_gender));
        for (int i=0; i<l.size();i++){
            if(l.get(i).toLowerCase().equals(str.toLowerCase())){
                msGender.setSelection(i+1);
            }
        }
    }
    public void getStatus(String str){
        List<String> l = Arrays.asList(getResources().getStringArray(R.array.array_status));
        for (int i=0; i<l.size();i++){
            if(l.get(i).toLowerCase().equals(str.toLowerCase())){
                msStatus.setSelection(i+1);
            }
        }
    }
    public void getMaritalStatus(String str){
        List<String> l = Arrays.asList(getResources().getStringArray(R.array.array_marital_status));
        for (int i=0; i<l.size();i++){
            if(l.get(i).toLowerCase().equals(str.toLowerCase())){
                msMartialStatus.setSelection(i+1);
            }
        }
    }
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

                            PreferenceUtils.setToken(mContext,token);
                            switch (type) {
                                case "edit":
                                    memberUpdateGovNet(PreferenceUtils.getUrlUpdateMember(mContext), memberDetails);
                                    break;
                                case "register":
                                    memberRegisterGovNet(PreferenceUtils.getUrlCreateMember(mContext), memberDetails);
                                    break;
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
    public class GetAccessTokenTask extends AsyncTask<String, String, String> {

        private Context mContext;
        private String URL;
        private String email,password;
        private MemberPOJO memberPOJO;
        GetAccessTokenTask(Context mContext,String URL,String email,String password,MemberPOJO memberPOJO) {
            this.mContext = mContext;
            this.email=email;
            this.password=password;
            this.URL = URL;
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
                    switch (type) {
                        case "edit":
                            memberUpdateGovNet(PreferenceUtils.getUrlUpdateMember(mContext), memberDetails);
                            break;
                        case "register":
                            memberRegisterGovNet(PreferenceUtils.getUrlCreateMember(mContext), memberDetails);
                            break;
                    }

                }else{
                    Toast.makeText(mContext,"Something went Wrong",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("--->", e.toString());
                progressBar.setVisibility(View.GONE);
                buttonSubmit.setEnabled(true);
                e.printStackTrace();
            }

        }
        @Override
        protected void onCancelled() {
        }
    }
    public void memberRegisterGovNet(String URL, final MemberPOJO memberDetails){
        try {
            Log.e("---->",URL);
            Log.e("---->REG",memberDetails.toString());

            final String uploadId = UUID.randomUUID().toString();
            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver;
            multipartUploadRequest.addFileToUpload(getImagePath(),"photo" )
                    .addParameter("token", PreferenceUtils.getToken(MemberRegisterActivity.this))
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
                    .addParameter("fingerprint2", memberDetails.getFingerPrint1())
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .setDelegate(new UploadStatusDelegate() {
                        @Override
                        public void onProgress(Context context, UploadInfo uploadInfo) {
                        }

                        @Override
                        public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                            Log.e("---->",serverResponse.getBodyAsString().toString());
                            buttonSubmit.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(MemberRegisterActivity.this,"error",Toast.LENGTH_LONG).show();
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
                                memberRegister.setFingerPrint1(isNull(memberObject,"fingerprint2"));
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
    public void memberUpdateGovNet(String URL, final MemberPOJO memberDetails){
        try {
            final String uploadId = UUID.randomUUID().toString();

            Log.e("---->up",memberDetails.toString());

            MultipartUploadRequest multipartUploadRequest = new MultipartUploadRequest(mContext, uploadId, URL);
            UploadServiceBroadcastReceiver uploadServiceBroadcastReceiver = null;


            if (editImage) {
                multipartUploadRequest.addFileToUpload(getImagePath(), "photo")
                        .addParameter("token", PreferenceUtils.getToken(MemberRegisterActivity.this))
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
                        .addParameter("fingerprint2",memberDetails.getFingerPrint1())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Log.e("---->", serverResponse.getBodyAsString().toString());
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext,"fail",Toast.LENGTH_LONG).show();

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
                                    memberRegister.setFingerPrint1(isNull(memberObject,"fingerprint2"));
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

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Log.e("--->", "caln");
                            }
                        })
                        .startUpload(); //Starting the upload
            }else {
                multipartUploadRequest.addParameter("token", PreferenceUtils.getToken(MemberRegisterActivity.this))
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
                        .addParameter("fingerprint2",memberDetails.getFingerPrint1())
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .setDelegate(new UploadStatusDelegate() {
                            @Override
                            public void onProgress(Context context, UploadInfo uploadInfo) {
                            }

                            @Override
                            public void onError(Context context, UploadInfo uploadInfo, ServerResponse serverResponse, Exception exception) {
                                Log.e("---->", serverResponse.getBodyAsString().toString());
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(mContext,"fail",Toast.LENGTH_LONG).show();

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
                                    memberRegister.setFingerPrint1(isNull(memberObject,"fingerprint2"));
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
                                finish();

                            }

                            @Override
                            public void onCancelled(Context context, UploadInfo uploadInfo) {

                                Log.e("--->", "caln");
                            }
                        })
                        .startUpload(); //Starting the upload
            }
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

//*******************************************************
