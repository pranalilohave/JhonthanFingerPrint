package in.co.ashclan.fingerprint;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.airbnb.lottie.animation.content.Content;

import fr.ganfra.materialspinner.MaterialSpinner;

public class AddNewUserActivity extends AppCompatActivity {

    Context mContext;
    TextInputEditText editTextFirstName,editRepeatPassword,editTextLastname,editTextEmail,editTextNote,
            editTextMobilePhone,editTextWorkPhone,editTextDOB,editTextAddress,editTextDescription,editTextUserPassword;
     ImageView imageViewProfilepic;
     MaterialSpinner msGender,msRole;
     Button buttonSubmit,buttonChangePwd;
     ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);

        imageViewProfilepic = (ImageView) findViewById(R.id.imageView_profilepic);
        editTextFirstName = (TextInputEditText) findViewById(R.id.first_name);
        editTextLastname = (TextInputEditText) findViewById(R.id.last_name);
        msGender = (MaterialSpinner) findViewById(R.id.spinner_gender);
        editTextMobilePhone = (TextInputEditText) findViewById(R.id.user_phone);
        editTextEmail = (TextInputEditText) findViewById(R.id.user_email);
        editTextUserPassword = (TextInputEditText) findViewById(R.id.user_password);
        editRepeatPassword = (TextInputEditText) findViewById(R.id.repeat_password);
        msRole = (MaterialSpinner) findViewById(R.id.spinner_Role);
        //editTextAddress = (TextInputEditText)findViewById(R.id.user_address);
        //editTextNote = (TextInputEditText) findViewById(R.id.user_notes);

        buttonSubmit = (Button) findViewById(R.id.user_btnSubmit);




    }
}
