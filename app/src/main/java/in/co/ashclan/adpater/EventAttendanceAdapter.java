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
import in.co.ashclan.model.EventAttendancePOJO;

public class EventAttendanceAdapter  extends BaseAdapter {

    private Context mContext;
    private ArrayList<EventAttendancePOJO> list;
    TextView txt_EventName,txt_EventDate;

    public EventAttendanceAdapter(Context mContext, ArrayList<EventAttendancePOJO> list) {
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
        return Long.parseLong(list.get(i).getId());
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vList;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.custom_attendance_template,null);
        }else {
            vList = (View)view;
        }
        txt_EventName =(TextView)vList.findViewById(R.id.event_atten_name);
        txt_EventDate=(TextView)vList.findViewById(R.id.event_atten_date);

        final EventAttendancePOJO eventAttendancePOJO= list.get(i);

        txt_EventName.setText(eventAttendancePOJO.getEventName());

        try {
            txt_EventDate.setText(datePattern(eventAttendancePOJO.getDate()));
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