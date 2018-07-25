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
import in.co.ashclan.model.ContributionsPOJO;

public class ContributionAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<ContributionsPOJO> list;
    TextView txt_batch,txt_member,txt_amount,txt_method,txt_date,txt_notes;

    public ContributionAdapter(Context mContext, ArrayList<ContributionsPOJO> list) {
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
            vList = inflater.inflate(R.layout.custom_contribution_template,null);
        }else {
            vList = (View)view;
        }
        txt_batch=(TextView)vList.findViewById(R.id.txt_Contri_batch);
        txt_amount=(TextView)vList.findViewById(R.id.txt_ContriAmount);
        txt_method=(TextView)vList.findViewById(R.id.txt_ContriMethod);
        txt_date =(TextView)vList.findViewById(R.id.txt_ContriDate);
        txt_notes=(TextView)vList.findViewById(R.id.txt_ContriNotes);

        final ContributionsPOJO contriPojo= list.get(i);


        txt_batch.setText(contriPojo.getBatchName());
        txt_amount.setText(contriPojo.getAmount());
        txt_method.setText(contriPojo.getPaymentMethod());
        txt_notes.setText(contriPojo.getNotes());

        try {
            txt_date.setText(datePattern(contriPojo.getDate()));
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
