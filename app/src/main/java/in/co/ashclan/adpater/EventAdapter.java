package in.co.ashclan.adpater;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import in.co.ashclan.fingerprint.AttendanceActivity;
import in.co.ashclan.fingerprint.EventDetailsActivity;
import in.co.ashclan.fingerprint.R;
import in.co.ashclan.fingerprint.TestingActivity;
import in.co.ashclan.model.EventPOJO;

public class EventAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<EventPOJO> list;
    TextView textViewDate,textViewTime,textViewName;
    LinearLayout layout1,layout2,layout3;

    public EventAdapter(Context mContext,ArrayList<EventPOJO> list){
        this.mContext=mContext;
//        this.list=list;
        Collections.reverse(list);
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
            vList = inflater.inflate(R.layout.event_details,null);
        }else {
            vList = (View)view;
        }
        textViewDate=(TextView)vList.findViewById(R.id.event_date);
        textViewTime=(TextView)vList.findViewById(R.id.event_time);
        textViewName=(TextView)vList.findViewById(R.id.event_name);
        layout1=(LinearLayout)vList.findViewById(R.id.layout1);
        layout2=(LinearLayout)vList.findViewById(R.id.layout2);
        layout3=(LinearLayout)vList.findViewById(R.id.layout3);
        final EventPOJO event= list.get(i);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EventDetailsActivity.class);
                intent.putExtra("event_details",event);
                mContext.startActivity(intent);
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(mContext, Enroll2Activity.class);
  //              mContext.startActivity(intent);
            }
        });
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, TestingActivity.class);
                intent.putExtra("eventId",event.getId());
                intent.putExtra("eventname",event.getName());
                mContext.startActivity(intent);
            }
        });


        textViewName.setText(event.getName());
        textViewTime.setText(event.getStart_time());
        try {
            textViewDate.setText(datePattern(event.getStartDate()));
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
