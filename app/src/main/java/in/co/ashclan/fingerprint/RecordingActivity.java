package in.co.ashclan.fingerprint;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

import in.co.ashclan.adpater.EventAttendanceAdapter;
import in.co.ashclan.adpater.RecordingAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.RecordingPOJO;

public class RecordingActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<RecordingPOJO> DetailsList = new ArrayList<>();
    DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);
        getSupportActionBar().setTitle("Recording");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataBaseHelper = new DataBaseHelper(getApplicationContext());

        listView = (ListView)findViewById(R.id.record_list);
        DetailsList.addAll(dataBaseHelper.getAllRecordings());
        RecordingAdapter recordingAdapter = new RecordingAdapter(RecordingActivity.this,DetailsList);
        listView.setAdapter(recordingAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                RecordingActivity.this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}


