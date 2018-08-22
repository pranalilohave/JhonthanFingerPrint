package in.co.ashclan.fingerprint;

import android.content.Context;
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
    private EditText editTextFirstName,editTextName,editTextLastname,editTextEmail,editTextHomePhone,
            editTextMobilePhone,editTextWorkPhone,editTextDOB,editTextAddress,editTextDescription;
    private ImageView imageViewProfilepic;
    private MaterialSpinner msGender,msRole;
    private Button buttonSubmit,buttonChangePwd;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_user);
    }
}
