package in.co.ashclan.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import in.co.ashclan.fingerprint.R;
import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.model.EventAttendancePOJO;
import in.co.ashclan.model.RecordingPOJO;

public class RecordingAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<RecordingPOJO> list;
    RecordingPOJO recordingPOJO;
    TextView txt_RecordName,txt_RecordDate;

    public RecordingAdapter(Context mContext, ArrayList<RecordingPOJO> list) {
        this.mContext = mContext;
        this.list = list;
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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vList;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.custom_recording_template,null);
        }else {
            vList = (View)view;
        }
        txt_RecordName =(TextView)vList.findViewById(R.id.record_name);
        txt_RecordDate=(TextView)vList.findViewById(R.id.record_date);

        recordingPOJO = list.get(i);

        txt_RecordName.setText(recordingPOJO.getFilename());

        try {
            txt_RecordDate.setText(datePattern(recordingPOJO.getEventDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return vList;
    }

    public String datePattern(String dateStr) throws ParseException {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date MyDate = newDateFormat.parse(dateStr);
        newDateFormat.applyPattern("EEEE,d MMM yyyy");
        return newDateFormat.format(MyDate);
    }
}
