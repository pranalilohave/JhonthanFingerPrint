package in.co.ashclan.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.co.ashclan.fingerprint.R;
import in.co.ashclan.model.GroupsPOJO;

public class GroupsAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<GroupsPOJO> list;
    TextView txtGroupName,txtNotes;

    public GroupsAdapter(Context mContext, ArrayList<GroupsPOJO> list) {
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
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.custom_groups_template,null);
        }else {
            vList = (View)view;
        }
        txtGroupName=(TextView)vList.findViewById(R.id.group_name);
        txtNotes=(TextView)vList.findViewById(R.id.groups_note_all);

        final GroupsPOJO groupsPOJO = list.get(i);

        txtGroupName.setText("GROUP : " + groupsPOJO.getName());
        txtNotes.setText("NOTE : "+groupsPOJO.getNotes());

        return vList;
    }
}
