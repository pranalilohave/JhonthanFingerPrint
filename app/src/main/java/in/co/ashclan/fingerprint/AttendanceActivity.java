package in.co.ashclan.fingerprint;

import android.Manifest;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
/*import android.app.FragmentManager;
import android.app.FragmentTransaction;*/
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.internal.NavigationMenu;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.fgtit.fpcore.FPMatch;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import android_serialport_api.AsyncFingerprint;
import android_serialport_api.AsyncFingerprintA5;
import android_serialport_api.SerialPortManager;
import android_serialport_api.SerialPortManagerA5;
import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.ashclanrecorder.AndroidAudioRecorder;
import in.co.ashclan.ashclanrecorder.model.AudioChannel;
import in.co.ashclan.ashclanrecorder.model.AudioSampleRate;
import in.co.ashclan.ashclanrecorder.model.AudioSource;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.database.test.Attendance;
import in.co.ashclan.fragment.AttendersUploadFragment;
import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.model.RecordingPOJO;
import in.co.ashclan.utils.ActivityList;
import in.co.ashclan.utils.PreferenceUtils;
import in.co.ashclan.utils.Util;
import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static in.co.ashclan.utils.Utils.isNull;
import static in.co.ashclan.utils.WebServiceCall.performPostCall;

public class AttendanceActivity extends AppCompatActivity implements AttendersUploadFragment.OnFragmentInteractionListener{

    private static final int REQUEST_RECORD_AUDIO = 0;
    String fileName ;
  // private static final String  AUDIO_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/recorded_audio.wav";
   private String  AUDIO_FILE_PATH ;

    private TextView fpTextView;
    //private ListView fpListView;
    private  TextView txt_date,txt_service,txt_dialog;
    private TextClock txt_time;
    RelativeLayout relativeLayout;
    //FrameLayout relativeLayout1;
    ImageLoaderConfiguration loaderConfiguration;
    ImageLoader imageLoader = ImageLoader.getInstance();
    public boolean isFragment = false;

    MemberPOJO memberPOJO;

   // private AsyncFingerprint vFingerprint;
    //private AsyncFingerprintA5 vFingerprintA5;

    Context mcontext;

    ImageView fpMatchImage,imagefinger;

    private boolean bIsCancel = false;
    private boolean bfpWork = false;

    private Timer startTimer;
    private TimerTask startTask;
    private Handler startHandler;


    private Boolean isTablet = false;
    private Boolean isPhone = false;
    Context mContext;
    DataBaseHelper dataBaseHelper;


    private boolean isMatch=false;
    private List<MemberPOJO> membersList = new ArrayList<MemberPOJO>();

    //**********************************
    private boolean bcheck=false;
    private boolean	bIsUpImage=true;
    private int count;

    //************************************
    MemberAdapter memberAdapter;
    ArrayList<MemberPOJO> personList = new ArrayList<MemberPOJO>();
    //************************************************
    private AsyncFingerprint registerFingerprint;
    private AsyncFingerprintA5 registerFingerprintA5;
    public Dialog fpDialog=null;
    public Dialog detailDialog=null;
    private int iFinger=0;
    private ImageView fpImage;
    private TextView fpStatusText;
    public FloatingActionButton fab;

    private boolean isEnroll1=false;
    private boolean isEnroll2=false;
    private boolean isUpEnroll1=false;
    private boolean isUpEnroll2=false;

    ImageView imageUpload,imageRecorder;

    ContentLoadingProgressBar progressBar;
    //**************** dialog member
    TextView txt_attender_name;
    TextView txt_attender_Time;
    ImageView ImgAttendant;
    ImageView imgclose;
    io.github.yavski.fabspeeddial.FabSpeedDial fabSpeedDial;
    AttendersUploadFragment attendersUploadFragment;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String formattedDate;
    String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        initRecord();

        // getActionBar().setTitle("");

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

//     toolbar.setOverflowIcon(getResources().getDrawable(R.drawable.ic_menu_2));

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        mContext = AttendanceActivity.this;
        dataBaseHelper = new DataBaseHelper(mContext);
        //fpListView = (ListView) findViewById(R.id.listView);
        fpTextView = (TextView) findViewById(R.id.text_fing);
        fpMatchImage = (ImageView) findViewById(R.id.match_image);
        imagefinger = (ImageView) findViewById(R.id.img_finger);
        relativeLayout = (RelativeLayout)findViewById(R.id.relativeLayout_main);
        //relativeLayout1 = (FrameLayout)findViewById(R.id.fragment_upload);

      //  imageUpload=(ImageView)findViewById(R.id.image_upload);
       // imageRecorder=(ImageView)findViewById(R.id.image_recorder);
        membersList.addAll(dataBaseHelper.getAllMembers());
        progressBar = (ContentLoadingProgressBar)findViewById(R.id.progress_bar_attendence);

        txt_date = (TextView)findViewById(R.id.txt_date);
        txt_time = (TextClock) findViewById(R.id.txt_Time);
        txt_service = (TextView)findViewById(R.id.txt_service);
//        txt_dialog = (TextView)findViewById(R.id.txt_dialog);
        String eventname = getIntent().getStringExtra("eventname");

        txt_date.setText(formattedDate);
        txt_service.setText(eventname);

        eventId=getIntent().getStringExtra("eventId");
        fab = (FloatingActionButton) findViewById(R.id.fab_dialog) ;
        fabSpeedDial = (io.github.yavski.fabspeeddial.FabSpeedDial)findViewById(R.id.fabspeed);

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                //TODO: Start some activity
                switch (menuItem.getItemId())
                {
                    case R.id.action_attender :
                        //startActivity(new Intent(mContext,QRCodeReaderActivity.class));
                        //showAlertDialog();
                        // AttenderFragment(personList,eventId);

                        isFragment = true;

                        //attendersUploadFragment = new AttendersUploadFragment(AttendanceActivity.this,eventId,formattedDate.toString());
                        fragmentManager = getSupportFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        attendersUploadFragment = new AttendersUploadFragment();

                        Bundle bundle = new Bundle();
                        bundle.putString("event_id", eventId);
                        // set Fragmentclass Arguments
                        attendersUploadFragment.setArguments(bundle);
                        fragmentTransaction.add(R.id.fragment_upload, attendersUploadFragment);
                        //fragmentTransaction.attach(attendersUploadFragment);
                        //relativeLayout1.setVisibility(View.VISIBLE);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.show(attendersUploadFragment);
                        fragmentTransaction.commit();

                         /* Intent i = new Intent(AttendanceActivity.this,AttenderActivity.class);
                        i.putExtra("person",personList);
                        i.putExtra("eventId",eventId);
                        startActivity(i);*/

                        break;
                    case R.id.action_record:
                        recordAudio();
                        break;
                    case R.id.action_member:
                        Intent intent = new Intent(AttendanceActivity.this , MemberRegisterActivity.class);
                        intent.putExtra("type", "register");
                        startActivity(intent);
                        workExit();
                        break;
                    case R.id.action_home:
                          /*  //Toast.makeText(mContext, "Cancel...", Toast.LENGTH_SHORT).show();
                            if(SerialPortManager.getInstance().isOpen()){
                                bIsCancel=true;
                                SerialPortManager.getInstance().closeSerialPort();
                            }
                            finish();*/
                            workExit();
                        //startActivity(new Intent(mContext,HomeActivity.class));
                        break;
                }
                return false;
            }
        });

        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {

            Toast.makeText(mContext,"Tablet",Toast.LENGTH_LONG).show();
            isTablet=true;

            try {
                FPMatch.getInstance().InitMatch();
                registerFingerprint = SerialPortManager.getInstance().getNewAsyncFingerprint();
                //FPInitFP07();
                FPmInitFP07();
                FPProcess1();
            }catch (UnsatisfiedLinkError e){
                Log.e("--->",e.toString());
            }
        }
        else {
            Toast.makeText(mContext,"Phone",Toast.LENGTH_LONG).show();
            isPhone=true;
            try {
                FPMatch.getInstance().InitMatch();
                registerFingerprintA5 = SerialPortManagerA5.getInstance().getNewAsyncFingerprint();
                //FPInitA5();
                FPmInitA5();
                FPProcess1();
            }catch(UnsatisfiedLinkError e){
                Log.e("--->",e.toString());
            }
        }

       /* imageRecorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recordAudio();
            }
        });*/

        /*imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String memberIds="";
                String eventId=getIntent().getStringExtra("eventId");
                for (MemberPOJO member:personList){
                    memberIds=memberIds+member.getId()+",";
                }
                memberIds = removeLastChar(memberIds);

                GetAccessTokenTask aTask=new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                        PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),memberIds,eventId);
                aTask.execute();
            }
        });*/

        fpMatchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                   // FPDialog(1);
            }
        });
        /***********************now*************************/
      /*  memberAdapter = new MemberAdapter(mContext,personList,"ic_person.png");
        memberAdapter.notifyDataSetChanged();
        // dataBaseHelper=new DataBaseHelper(mContext);
        fpListView.setAdapter(memberAdapter);*/
        /****************************************************/

       /* fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog();
            }
        });*/
    }
    private void setHomeAction() {
        String memberIds="";
        String eventId=getIntent().getStringExtra("eventId");
        for (MemberPOJO member:personList){
            memberIds=memberIds+member.getId()+",";
        }
        memberIds = removeLastChar(memberIds);
        GetAccessTokenTask aTask=new GetAccessTokenTask(mContext,PreferenceUtils.getUrlLogin(mContext),
                PreferenceUtils.getAdminName(mContext),PreferenceUtils.getAdminPassword(mContext),memberIds,eventId);
        aTask.execute();

        //startActivity(new Intent(mContext,HomeActivity.class));
        finish();
    }
    private String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void workExit(){
        if(SerialPortManager.getInstance().isOpen()){
            bIsCancel=true;
            SerialPortManager.getInstance().closeSerialPort();
        }
        finish();
    }

    public void FPDialog(int i){
        iFinger = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
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
       fpStatusText.setText("Place your finger on the Sensor until your name appears");
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
                Toast.makeText(mContext,"Please Try Again",Toast.LENGTH_LONG).show();
                fpDialog.cancel();

                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
            }
        });

        registerFingerprintA5.setOnUpCharListener(new AsyncFingerprintA5.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {

                for (MemberPOJO member:membersList){
                    if(!member.getFingerPrint().equals("")) {
                        byte[] byt = Base64.decode(member.getFingerPrint(), Base64.DEFAULT);
                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model, byt)));
                        if (FPMatch.getInstance().MatchTemplate(model, byt) > 60) {
                            //fpTextView.setText(getString(R.string.txt_fpmatching) + " " + member.getFirstName());
                            member.setAttenTime(txt_time.getText().toString());
                            AddPerson(member);
                            isMatch=true;
                            break;
                        }
                    }
                    if(!member.getFingerPrint1().equals("")){
                        byte[] byt2=Base64.decode(member.getFingerPrint1(),Base64.DEFAULT);
                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model,byt2)));
                        if (FPMatch.getInstance().MatchTemplate(model,byt2)>40){
                            //fpTextView.setText(getString(R.string.txt_fpmatching)+" "+member.getFirstName());
                            member.setAttenTime(txt_time.getText().toString());
                            AddPerson(member);
                            isMatch=true;
                            break;
                        }
                    }
                }

                if(isMatch){
                    Toast.makeText(mContext,"Match OK",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext,"No Match Found",Toast.LENGTH_LONG).show();
                }
                isMatch=false;

                fpStatusText.setText(getString(R.string.txt_fpenrolok));
                fpDialog.cancel();
            }

            @Override
            public void onUpCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail));
                Toast.makeText(mContext,"Please try again",Toast.LENGTH_LONG).show();
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

                Toast.makeText(mContext,"Please Try Again",Toast.LENGTH_LONG).show();
                fpDialog.cancel();
            }
        });

        registerFingerprint.setOnUpCharListener(new AsyncFingerprint.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {


                MemberPOJO m = new MemberPOJO();
                for (MemberPOJO member:membersList){
/*
                    if(!member.getFingerPrint().equals("")) {
                        byte[] byt = Base64.decode(member.getFingerPrint(), Base64.DEFAULT);
                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model, byt)));
                        if (FPMatch.getInstance().MatchTemplate(model, byt) > 60) {
//                            fpTextView.setText(getString(R.string.txt_fpmatching) + " " + member.getFirstName());
                            AddPerson(member);
                            isMatch=true;
                            break;
                        }
                    }
*/
                    if(!member.getFingerPrint1().equals("")){
                        byte[] byt2=Base64.decode(member.getFingerPrint1(),Base64.DEFAULT);
                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model,byt2)));
                        if (FPMatch.getInstance().MatchTemplate(model,byt2)>40){
                   //         fpTextView.setText(getString(R.string.txt_fpmatching)+" "+member.getFirstName());
                            member.setAttenTime(txt_time.getText().toString());
                            AddPerson(member);
                            m=member;
                            isMatch=true;
                            break;
                        }
                    }
                }

                if(isMatch){
                    Toast.makeText(mContext,"Match OK",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext,"No Match Found",Toast.LENGTH_LONG).show();
                    fpDialog.cancel();
                }
                isMatch=false;
                fpStatusText.setText(getString(R.string.txt_fpenrolok));

                showMemberDialog(m);
            }

            @Override
            public void onUpCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail));
                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(mContext,"Please Try Again",Toast.LENGTH_LONG).show();
                fpDialog.cancel();
            }
        });
    }

    //**********************************
    private void FPProcess1(){
        if(!bfpWork){
            try {
                Thread.currentThread();
                Thread.sleep(500);
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            fpTextView.setText("Place your finger on the Sensor until your name appears");
            imagefinger.setImageDrawable(getResources().getDrawable(R.drawable.ic_finger));
            //tvFpStatus.setText(getString(R.string.txt_fpplace));
            if(isPhone) {
                registerFingerprintA5.FP_GetImage();
            } else if(isTablet) {
                registerFingerprint.FP_GetImage();
            }
         //   vFingerprint.FP_GetImage();
            bfpWork=true;
        }
    }

    private void FPmInitFP07(){
        /*scan the finger and get finger print image*/
        registerFingerprint.setOnGetImageListener(new AsyncFingerprint.OnGetImageListener() {
            @Override
            public void onGetImageSuccess() {
                if(ActivityList.getInstance().IsUpImage){
                    registerFingerprint.FP_UpImage();
                    fpTextView.setText(getString(R.string.txt_fpdisplay));
                }else{
                    fpTextView.setText(getString(R.string.txt_fpprocess));
                    registerFingerprint.FP_GenChar(1);
                }
            }

            @Override
            public void onGetImageFail() {
                if(!bIsCancel){
                    registerFingerprint.FP_GetImage();
                    //SignLocalActivity.this.AddStatus("Error");
                }else{
                    Toast.makeText(mContext, "Cancel OK", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*set finger print image on imageView*/
        registerFingerprint.setOnUpImageListener(new AsyncFingerprint.OnUpImageListener() {
            @Override
            public void onUpImageSuccess(byte[] data) {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                imagefinger.setImageBitmap(image);
                //fpImage.setBackgroundDrawable(new BitmapDrawable(image));
                fpTextView.setText(getString(R.string.txt_fpprocess));
                registerFingerprint.FP_GenChar(1);
            }

            @Override
            public void onUpImageFail() {
                bfpWork=false;
                TimerStart();
            }
        });

        registerFingerprint.setOnGenCharListener(new AsyncFingerprint.OnGenCharListener() {
            @Override
            public void onGenCharSuccess(int bufferId) {
                fpTextView.setText(getString(R.string.txt_fpidentify));
                registerFingerprint.FP_UpChar();
            }

            @Override
            public void onGenCharFail() {
                fpTextView.setText(getString(R.string.txt_fpfail));
            }
        });

        registerFingerprint.setOnUpCharListener(new AsyncFingerprint.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {
                MemberPOJO m = new MemberPOJO();
                for (MemberPOJO member:membersList){
                    if(!member.getFingerPrint1().equals("")){


                        byte[] byt2=Base64.decode(member.getFingerPrint1(),Base64.DEFAULT);



                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model,byt2)));
                        if (FPMatch.getInstance().MatchTemplate(model,byt2)>40){
                            //         fpTextView.setText(getString(R.string.txt_fpmatching)+" "+member.getFirstName());
                            member.setAttenTime(txt_time.getText().toString());
                            AddPerson(member);
                            m=member;
                            isMatch=true;
                            break;
                        }
                    }


                    if(!member.getFingerPrint().equals("")){
                        byte[] byt2=Base64.decode(member.getFingerPrint(),Base64.DEFAULT);
                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model,byt2)));
                        if (FPMatch.getInstance().MatchTemplate(model,byt2)>40){
                            //         fpTextView.setText(getString(R.string.txt_fpmatching)+" "+member.getFirstName());
                            member.setAttenTime(txt_time.getText().toString());
                            AddPerson(member);
                            m=member;
                            isMatch=true;
                            break;
                        }
                    }

                }

                if(isMatch){
                    showMemberDialog(m);
                    Toast.makeText(mContext,"Match OK",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext,"No Match Found",Toast.LENGTH_LONG).show();
                    //fpDialog.cancel();
                }
                isMatch=false;
                fpTextView.setText(getString(R.string.txt_fpenrolok));

                bfpWork=false;
                TimerStart();
            }

            @Override
            public void onUpCharFail() {
                fpTextView.setText(getString(R.string.txt_fpmatchfail)+":-1");
                bfpWork=false;
                TimerStart();
            }
        });

    }

    private void FPmInitA5(){
        /*scan the finger and get finger print image*/
        registerFingerprintA5.setOnGetImageListener(new AsyncFingerprintA5.OnGetImageListener() {
            @Override
            public void onGetImageSuccess() {
                if(ActivityList.getInstance().IsUpImage){
                    registerFingerprintA5.FP_UpImage();
                    fpTextView.setText(getString(R.string.txt_fpdisplay));
                }else{
                    fpTextView.setText(getString(R.string.txt_fpprocess));
                    registerFingerprintA5.FP_GenChar(1);
                }
            }

            @Override
            public void onGetImageFail() {
                if(!bIsCancel){
                    registerFingerprintA5.FP_GetImage();
                    //SignLocalActivity.this.AddStatus("Error");
                }else{
                    Toast.makeText(mContext, "Cancel OK", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*set finger print image on imageView*/
        registerFingerprintA5.setOnUpImageListener(new AsyncFingerprintA5.OnUpImageListener() {
            @Override
            public void onUpImageSuccess(byte[] data) {
                Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                imagefinger.setImageBitmap(image);
                //fpImage.setBackgroundDrawable(new BitmapDrawable(image));
                fpTextView.setText(getString(R.string.txt_fpprocess));
                registerFingerprintA5.FP_GenChar(1);
            }

            @Override
            public void onUpImageFail() {
                bfpWork=false;
                TimerStart();
            }
        });

        registerFingerprintA5.setOnGenCharListener(new AsyncFingerprintA5.OnGenCharListener() {
            @Override
            public void onGenCharSuccess(int bufferId) {
                fpTextView.setText(getString(R.string.txt_fpidentify));
                registerFingerprintA5.FP_UpChar();
            }

            @Override
            public void onGenCharFail() {
                fpTextView.setText(getString(R.string.txt_fpfail));
            }
        });

        registerFingerprintA5.setOnUpCharListener(new AsyncFingerprintA5.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {

                MemberPOJO m = new MemberPOJO();
                for (MemberPOJO member:membersList){
                    if(!member.getFingerPrint1().equals("")){
                        byte[] byt2=Base64.decode(member.getFingerPrint1(),Base64.DEFAULT);
                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model,byt2)));
                        if (FPMatch.getInstance().MatchTemplate(model,byt2)>40){
                            //         fpTextView.setText(getString(R.string.txt_fpmatching)+" "+member.getFirstName());
                            member.setAttenTime(txt_time.getText().toString());
                            AddPerson(member);
                            m=member;
                            isMatch=true;
                            break;
                        }
                    }


                    if(!member.getFingerPrint().equals("")){
                        byte[] byt2=Base64.decode(member.getFingerPrint(),Base64.DEFAULT);
                        Log.e("--->MATCH1B", String.valueOf(FPMatch.getInstance().MatchTemplate(model,byt2)));
                        if (FPMatch.getInstance().MatchTemplate(model,byt2)>40){
                            //         fpTextView.setText(getString(R.string.txt_fpmatching)+" "+member.getFirstName());
                            member.setAttenTime(txt_time.getText().toString());
                            AddPerson(member);
                            m=member;
                            isMatch=true;
                            break;
                        }
                    }

                }

                if(isMatch){
                    showMemberDialog(m);
                    Toast.makeText(mContext,"Match OK",Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(mContext,"No Match Found",Toast.LENGTH_LONG).show();
                    //fpDialog.cancel();
                }
                isMatch=false;
                fpTextView.setText(getString(R.string.txt_fpenrolok));

                bfpWork=false;
                TimerStart();
            }

            @Override
            public void onUpCharFail() {
                fpTextView.setText(getString(R.string.txt_fpmatchfail)+":-1");
                bfpWork=false;
                TimerStart();
            }
        });

    }

    //***********************************************************************************************************************
    public void TimerStart(){
        if(bIsCancel)
            return;
        if(startTimer==null){
            startTimer = new Timer();
            startHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    TimeStop();
                   // FPProcess();
                    FPProcess1();
                }
            };
            startTask = new TimerTask() {
                @Override
                public void run() {
                    Message message = new Message();
                    message.what = 1;
                    startHandler.sendMessage(message);
                }
            };
            startTimer.schedule(startTask, 1000, 1000);
        }
    }

    public void TimeStop(){
        if (startTimer!=null)
        {
            startTimer.cancel();
            startTimer = null;
            startTask.cancel();
            startTask=null;
        }
    }

    public String getTime(){
        Date dt = new Date();
        int yyyy = dt.getYear()+1900;
        int MM = dt.getMonth()+1;
        int dd = dt.getDay()+1;
        int hours = dt.getHours();


        int minutes = dt.getMinutes();
        int seconds = dt.getSeconds();

        String curTime = yyyy+"-"+xformat(MM)+"-"+xformat(dd)+" "+xformat(hours) + ":" + xformat(minutes) + ":" + xformat(seconds);
        return curTime;
    }

    public String xformat(int x){
        if(x<10){
            return "0"+x;
        }else {
            return ""+x;
        }
    }

    //************************************************************************
    private void AddPerson(MemberPOJO member){
        if (dataBaseHelper.isTEMPMemberAvailable(member.getId(),eventId)){
            Toast.makeText(mContext,"Already Attended",Toast.LENGTH_LONG).show();
        }else{
            //personList.add(member);
            //addintoTempAttendenceList(personList);
            String eventId=getIntent().getStringExtra("eventId");

          /*  if(!dataBaseHelper.isTEMPMemberAvailable(member.getId(),eventId))
            {*/
                EventAttendancePOJO attender = new EventAttendancePOJO();

                attender.setEventId(eventId);
                attender.setUserId(member.getUserId().toString());
                attender.setMemberId(member.getId());
                attender.setAnonymous("0");
                attender.setDate(txt_date.getText().toString());
                attender.setCreatedAt(member.getCreateAt());
                attender.setUpdatedAt(member.getUpdateAt());
                attender.setAttenDate(txt_date.getText().toString());
                attender.setAttenTime(member.getAttenTime());

                dataBaseHelper.insertTempEventAttendaceData(attender);

            //}

           // memberAdapter.notifyDataSetChanged();
        }
       // ScrollListViewToBottom();
    }

    private void addintoTempAttendenceList(ArrayList<MemberPOJO> personList) {

        EventAttendancePOJO attender = new EventAttendancePOJO();

        String eventId=getIntent().getStringExtra("eventId");

        for(int i = 0;i<personList.size();i++){

            attender.setEventId(eventId);
            attender.setUserId(personList.get(i).getUserId());
            attender.setMemberId(personList.get(i).getId());
            attender.setAnonymous("0");
            attender.setDate(personList.get(i).getDob());
            attender.setCreatedAt(personList.get(i).getCreateAt());
            attender.setUpdatedAt(personList.get(i).getUpdateAt());
            attender.setAttenDate(txt_date.getText().toString());
            attender.setAttenTime(personList.get(i).getAttenTime());

            dataBaseHelper.insertTempEventAttendaceData(attender);
        }
    }

   /* private void ScrollListViewToBottom() {
        fpListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                fpListView.setSelection(memberAdapter.getCount() - 1);
            }
        });
    }//scrolling*/

    @Override
    public void onFragmentInteraction(View uri) {
    }

    //**********************************************************************************
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
                CheckInTask checkInTask  = new CheckInTask(mContext,PreferenceUtils.getUrlAddCheckin(mContext),memberIds,eventId,token);
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

    //**************************************************************
    private void initRecord() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimaryDark)));
        }

        Util.requestPermission(this, Manifest.permission.RECORD_AUDIO);
        Util.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Audio recorded successfully!", Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Audio was not recorded", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void recordAudio() {

        fileName = System.currentTimeMillis() + ".wav";
        AUDIO_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/"+fileName;
        Uri uri = Uri.parse(AUDIO_FILE_PATH);
        AndroidAudioRecorder.with(this)
                // Required
                .setFilePath(AUDIO_FILE_PATH)
                .setColor(ContextCompat.getColor(this, R.color.recorder_bg))
                .setRequestCode(REQUEST_RECORD_AUDIO)

                // Optional
                .setSource(AudioSource.MIC)
                .setChannel(AudioChannel.STEREO)
                .setSampleRate(AudioSampleRate.HZ_48000)
                .setAutoStart(false)
                .setKeepDisplayOn(true)

                // Start recording
                //7507609658
                .record();
        RecordingPOJO recordingPOJO = new RecordingPOJO();
        //recordingPOJO.setEventid(eventId);
        recordingPOJO.setEventid("25");
        recordingPOJO.setEventDate(txt_date.getText().toString());
        //recordingPOJO.setFilename(uri.getPath().toString());
        recordingPOJO.setFilename(fileName);
        recordingPOJO.setFilePath(uri.toString());

        dataBaseHelper.insertRecordingData(recordingPOJO);
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_record) {
            recordAudio();
        }
        if (id == R.id.home){
            workExit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){

            String eventId = getIntent().getStringExtra("eventId");

            if(isFragment){

                //Toast.makeText(mcontext, "this is clicked", Toast.LENGTH_SHORT).show();

                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
               // attendersUploadFragment = new AttendersUploadFragment(AttendanceActivity.this,eventId);
                //attendersUploadFragment = new AttendersUploadFragment();
                //fragmentTransaction.detach(attendersUploadFragment);
                fragmentManager.popBackStack();

                //relativeLayout1.setVisibility(View.GONE);
               //fragmentTransaction.commit();
                isFragment = false;
            }else
            {
              /*  if(personList.size()!= 0)
                {
                    Toast.makeText(mContext, "Please Upload All Attendance Data", Toast.LENGTH_SHORT).show();
                }else {*/
                    Toast.makeText(mContext, "Cancel...", Toast.LENGTH_SHORT).show();
                    //finish();
                    workExit();
                //}
            }
            return true;
        } else if(keyCode == KeyEvent.KEYCODE_HOME){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showAlertDialog() {
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
//        alertDialog.setTitle("List of Enrolled Person's !...");
//        alertDialog.setMessage("Please Upload All Data");

        LayoutInflater layoutInflater = this.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.custome_dialog_fragment,null);

        final ListView fpListView;
        final ImageView ImageView_close;
        fpListView = (ListView) view.findViewById(R.id.dialog_listView);
        ImageView_close = (ImageView) view.findViewById(R.id.imageView_close);


        memberAdapter = new MemberAdapter(mContext,personList,"ic_person.png");
        memberAdapter.notifyDataSetChanged();
        // dataBaseHelper=new DataBaseHelper(mContext);
        fpListView.setAdapter(memberAdapter);

        alertDialog.setView(view);
        alertDialog.setIcon(R.drawable.ic_menu_black_24dp);

        final android.support.v7.app.AlertDialog show=  alertDialog.show();

        ImageView_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                show.dismiss();
            }
        });


//        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                dialog.dismiss();
////            }
////        });
////        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
////            @Override
////            public void onClick(DialogInterface dialog, int which) {
////                dialog.dismiss();
////            }
////        });
    }

    public void showMemberDialog(MemberPOJO memberPOJO) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        View dialogView = inflater.inflate(R.layout.custom_dialog_matchfound,null);

        txt_attender_name = dialogView.findViewById(R.id.txt_attendant_name);
        txt_attender_Time = dialogView.findViewById(R.id.txt_attendant_clock);
        ImgAttendant = dialogView.findViewById(R.id.img_attendant_name);
        imgclose = dialogView.findViewById(R.id.imageView_custom_close);


        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageOnLoading(R.drawable.ic_person)
                .showImageForEmptyUri(R.drawable.ic_person)
                .showImageOnFail(R.drawable.ic_person)
                .build();

        loaderConfiguration = new ImageLoaderConfiguration.Builder(AttendanceActivity.this)
                .defaultDisplayImageOptions(imageOptions).build();
        imageLoader.init(loaderConfiguration);


        builder.setView(dialogView);
        builder.setCancelable(false);

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
        txt_attender_name.setText(memberPOJO.getFirstName() + " " + memberPOJO.getLastName());
        txt_attender_Time.setText(txt_time.getText().toString());


        if (null!=memberPOJO.getPhotoURL()) {
            //    "http://52.172.221.235:8983/uploads/"
            String imgURL = PreferenceUtils.getUrlUploadImage(mContext) + memberPOJO.getPhotoURL();
            try {
                imageLoader.displayImage(imgURL, ImgAttendant, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        //    imageLoader.displayImage("http://52.172.221.235:8983/uploads/" + defaultIcon, imageView);
                    }
                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        // profileImageView.setImageBitmap();
                        ImgAttendant.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_image_1));
                    }
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Log.e("--->", imageUri);
                        Log.e("--->", loadedImage.toString());
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {

                    }
                });
            }catch (Exception ex){
                ImgAttendant.setImageResource(R.drawable.ic_profile_image_1);
                ex.printStackTrace();
            }
        }
        imgclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fpDialog.dismiss();
                detailDialog.dismiss();
            }
        });

        detailDialog = builder.create();
        detailDialog.setCanceledOnTouchOutside(false);
        detailDialog.show();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                detailDialog.dismiss();
            }
        },1000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.atendence_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_attendance) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_recording) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        if (id == R.id.action_add_register) {
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }




}


