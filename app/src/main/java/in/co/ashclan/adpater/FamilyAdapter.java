package in.co.ashclan.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.ashclan.fingerprint.R;
import in.co.ashclan.model.FamilyPOJO;

public class FamilyAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<FamilyPOJO> list;
    TextView txtFamilyName,txtFamilyRole;

    public FamilyAdapter(Context mContext, ArrayList<FamilyPOJO> list) {
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
//        return Long.parseLong(list.get(i).getId());
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vList;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.custom_family_template,null);
        }else {
            vList = (View)view;
        }
        //txt_batch=(TextView)vList.findViewById(R.id.txt_Contri_batch);
        txtFamilyName = (TextView)vList.findViewById(R.id.txt_name);
        txtFamilyRole = (TextView)vList.findViewById(R.id.txt_role);

        final FamilyPOJO familyPOJO = list.get(i);

        txtFamilyName.setText(familyPOJO.getName());
        txtFamilyRole.setText(familyPOJO.getRole());
      /*  try {
           txt_date.setText(datePattern(contriPojo.getDate()));
        } catch (ParseException e) {
            e.printStackTrace();
        }*/

        return vList;
    }
}
