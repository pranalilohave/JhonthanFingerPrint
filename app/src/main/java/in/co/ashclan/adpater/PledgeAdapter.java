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
import in.co.ashclan.model.PledgesPOJO;

public class PledgeAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<PledgesPOJO> list;
    TextView txt_campainname, txt_amount, txt_date, txt_notes;


    public PledgeAdapter(Context mContext, ArrayList<PledgesPOJO> list) {
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
       // return Long.parseLong(list.get(i).getId());
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vList;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.custom_pledge_template, null);
        } else {
            vList = (View) view;
        }
        //txt_batch=(TextView)vList.findViewById(R.id.txt_Contri_batch);
        txt_campainname = (TextView) vList.findViewById(R.id.txt_pledge_campaign);
        txt_amount = (TextView) vList.findViewById(R.id.txt_pledge_amount);
        txt_date = (TextView) vList.findViewById(R.id.txt_pledge_date);
        txt_notes = (TextView) vList.findViewById(R.id.txt_pledge_notes);

        final PledgesPOJO pledgesPOJO = list.get(i);

        txt_campainname.setText(pledgesPOJO.getCampaign_name());
        txt_amount.setText(pledgesPOJO.getTotal_amount());
        txt_notes.setText(pledgesPOJO.getNotes());

        try {
            txt_date.setText(datePattern(pledgesPOJO.getDate()));
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