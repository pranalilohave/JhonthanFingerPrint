package in.co.ashclan.fingerprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SettingsActivity extends AppCompatActivity {
    Button btnChangePassword,btnAddNewUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnAddNewUser = (Button)findViewById(R.id.btn_AddNewUser);
        btnChangePassword = (Button)findViewById(R.id.btn_Change_Password);
    }

    public void ChangePassword(View view) {

    }

    public void addNewUser(View view) {

    }
}
