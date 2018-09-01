package in.co.ashclan.fingerprint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fgtit.fpcore.FPMatch;

import android_serialport_api.AsyncFingerprint;
import android_serialport_api.AsyncFingerprintA5;
import android_serialport_api.SerialPortManager;
import android_serialport_api.SerialPortManagerA5;
import in.co.ashclan.model.MemberPOJO;

public class TestingActivity extends AppCompatActivity {


    private ImageView fpImage;
    private TextView fpStatusText;
    private Dialog fpDialog = null;
    private int iFinger = 0;

    private AsyncFingerprint registerFingerprint;
    private AsyncFingerprintA5 registerFingerprintA5;
    boolean isTablet = false;
    boolean isPhone = false;
    private int count;
    private Context mContext;
    private boolean bIsCancel = false;
    private boolean bIsUpImage = true;
    private boolean bcheck = false;
    FloatingActionButton flotingbtn;
    String eventId,eventname;


// java.lang.ArrayIndexOutOfBoundsException: src.length=131072 srcPos=28581 dst.length=51200 dstPos=28507 length=65288
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
       mContext = TestingActivity.this;
        eventId = (String)getIntent().getSerializableExtra("eventId") ;
        eventname = (String)getIntent().getSerializableExtra("eventname") ;
        flotingbtn =(FloatingActionButton) findViewById(R.id.testing);


        Configuration config = getResources().getConfiguration();
        if (config.smallestScreenWidthDp >= 600) {

            Toast.makeText(TestingActivity.this, "Tablet", Toast.LENGTH_LONG).show();
            isTablet = true;

            try {
                registerFingerprint = SerialPortManager.getInstance().getNewAsyncFingerprint();
                FPInitFP07();
            } catch (UnsatisfiedLinkError e) {
                Log.e("--->", e.toString());
            }
        } else {
            Toast.makeText(mContext, "Phone", Toast.LENGTH_LONG).show();
            isPhone = true;
            try {
                registerFingerprintA5 = SerialPortManagerA5.getInstance().getNewAsyncFingerprint();
                FPInitA5();
            } catch (UnsatisfiedLinkError e) {
                Log.e("--->", e.toString());
            }
        }
        flotingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FPDialog(1);

            }
        });
    }

    public void FPDialog(int i) {
        iFinger = i;
        AlertDialog.Builder builder = new AlertDialog.Builder(TestingActivity.this);
        builder.setTitle("Note: If the FingerPrint Scanner is not working please Restart the Device");
        final LayoutInflater inflater = LayoutInflater.from(TestingActivity.this);
        View dialogView = inflater.inflate(R.layout.dialog_enrolfinger, null);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fpDialog != null && fpDialog.isShowing()) {
            fpDialog.dismiss();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (fpDialog != null && fpDialog.isShowing()) {
            fpDialog.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(fpDialog != null)
            fpDialog.dismiss();
    }

    public void FPProcess() {
        count = 1;
        fpStatusText.setText("Please Press Finger");
        try {
            Thread.currentThread();
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (isPhone) {
            registerFingerprintA5.FP_GetImage();
        } else if (isTablet) {
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
                fpStatusText.setText(getString(R.string.txt_fpenrolfail) + "2");
                fpDialog.cancel();

                if (iFinger == 1) {
                    //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                } else {
                    //imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(mContext, "Enrol Failed", Toast.LENGTH_LONG).show();
            }
        });

        registerFingerprintA5.setOnUpCharListener(new AsyncFingerprintA5.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {



                Intent intent = new Intent(mContext, AttendanceActivity.class);
                intent.putExtra("eventId",eventId);
                intent.putExtra("eventname",eventname);
                startActivity(intent);


//                fpStatusText.setText(getString(R.string.txt_fpenrolok));
   //             fpDialog.cancel();
            }

            @Override
            public void onUpCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail));
                if (iFinger == 1) {
//                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                } else {
                    //                  imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                Toast.makeText(mContext, "Enrol Failed", Toast.LENGTH_LONG).show();
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
                fpStatusText.setText(getString(R.string.txt_fpenrolfail) + "2");

                if (iFinger == 1) {
                    //    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                } else {
                    //   imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(mContext, "Enrol Failed", Toast.LENGTH_LONG).show();
                fpDialog.cancel();

            }
        });

        registerFingerprint.setOnUpCharListener(new AsyncFingerprint.OnUpCharListener() {

            @Override
            public void onUpCharSuccess(byte[] model) {

                fpDialog.cancel();

                Intent intent = new Intent(mContext, AttendanceActivity.class);
                intent.putExtra("eventId",eventId);
                intent.putExtra("eventname",eventname);
                startActivity(intent);
                finish();
                //fpStatusText.setText(getString(R.string.txt_fpenrolok));
                // fpDialog.cancel();
            }

            @Override
            public void onUpCharFail() {
                fpStatusText.setText(getString(R.string.txt_fpenrolfail));

                if (iFinger == 1) {
//                    imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                } else {
                    //                  imageViewFingerPrint3.setColorFilter(getResources().getColor(R.color.red));
                }
                //imageViewFingerPrint1.setColorFilter(getResources().getColor(R.color.red));
                Toast.makeText(mContext, "Enrol Failed", Toast.LENGTH_LONG).show();
                fpDialog.cancel();
            }
        });
    }

}

