package in.co.ashclan.fingerprint;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import in.co.ashclan.adpater.MemberAdapter;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.utils.Constants;
import in.co.ashclan.utils.Utils;


public class MemberFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String LIST = "list";
    private String mParam1;
    private String mParam2;
    ListView listView;
    Button sreachButton;
    EditText sreachEditText;
    MemberAdapter memberAdapter;
    public static MemberFragment instance;
    DataBaseHelper dataBaseHelper;
    //private OnFragmentInteractionListener mListener;

    ArrayList<MemberPOJO> list = new ArrayList<MemberPOJO>();
    ArrayList<MemberPOJO> sList = new ArrayList<MemberPOJO>();
    public MemberFragment() {
    }

    public static MemberFragment newInstance(){
        if(instance==null)
        instance = new MemberFragment();
        return instance;
    }
    public static MemberFragment newInstance(ArrayList<MemberPOJO> list) {
        MemberFragment fragment = new MemberFragment();
        Bundle args = new Bundle();
        args.putSerializable(LIST,list);
        fragment.setArguments(args);
        return fragment;
    }


    public static MemberFragment newInstance(String param1, String param2) {
        MemberFragment fragment = new MemberFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            list = (ArrayList<MemberPOJO>)getArguments().getSerializable(LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member,
                container, false);
        memberAdapter = new MemberAdapter(MemberFragment.this.getActivity(),list,"ic_person.png");
        memberAdapter.notifyDataSetChanged();
        dataBaseHelper=new DataBaseHelper(MemberFragment.this.getActivity());
        listView = (ListView)view.findViewById(R.id.list);
        sreachEditText=(EditText)view.findViewById(R.id.edit_search);
        sreachButton = (Button) view.findViewById(R.id.button_search);
        listView.setAdapter(memberAdapter);

        sreachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sList = (ArrayList) dataBaseHelper.getSerachMember(sreachEditText.getText().toString());
                memberAdapter = new MemberAdapter(MemberFragment.this.getActivity(),sList,"ic_person.png");
                memberAdapter.notifyDataSetChanged();
                listView.setAdapter(memberAdapter);
                sreachEditText.setText("");
                Utils.hideKeyboard(MemberFragment.this.getActivity());

            }
        });

        sreachEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sList = (ArrayList) dataBaseHelper.getSerachMember(sreachEditText.getText().toString());
                memberAdapter = new MemberAdapter(MemberFragment.this.getActivity(),sList,"ic_person.png");
                memberAdapter.notifyDataSetChanged();
                listView.setAdapter(memberAdapter);
       //         sreachEditText.setText("");
//                Utils.hideKeyboard(MemberFragment.this.getActivity());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                MemberPOJO memberDetails = list.get(i);
                Intent intent = new Intent(getActivity().getApplication(),MemberDetailsActivity.class);
                intent.putExtra("member_details",memberDetails);
                startActivity(intent);
                getActivity().finish();
            }
        });

    return view;
    }
}
