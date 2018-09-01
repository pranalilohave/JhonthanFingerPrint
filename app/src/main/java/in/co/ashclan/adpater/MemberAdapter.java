package in.co.ashclan.adpater;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.ashclan.database.DataBaseHelper;
import in.co.ashclan.fingerprint.R;
import in.co.ashclan.model.MemberPOJO;
import in.co.ashclan.model.MemberPhotoPojo;
import in.co.ashclan.utils.PreferenceUtils;

public class MemberAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<MemberPOJO> list;
    String defaultIcon;
    ImageLoaderConfiguration loaderConfiguration;
    ImageLoader imageLoader = ImageLoader.getInstance();
    ImageView imageView;
    ProgressBar progressBar;
    ArrayList<MemberPhotoPojo> memberPhotoPojosList;
    DataBaseHelper dataBaseHelper;

    TextView textViewName,textViewLocation,textViewGender,
            textViewAge,textViewPhone,textViewRollno;


    public MemberAdapter(Context context,ArrayList<MemberPOJO> list,String defaultIcon){
        mContext = context;
        this.defaultIcon = defaultIcon;
        this.list=list;

        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageOnLoading(R.drawable.ic_person)
                .showImageForEmptyUri(R.drawable.ic_person)
                .showImageOnFail(R.drawable.ic_person)
                .build();

        loaderConfiguration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(imageOptions).build();
        imageLoader.init(loaderConfiguration);

        dataBaseHelper = new DataBaseHelper(mContext);
        memberPhotoPojosList = (ArrayList<MemberPhotoPojo>) dataBaseHelper.getAllTempMembers();

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
            vList = inflater.inflate(R.layout.member_details,null);
        }else {
            vList = (View)view;
        }
        imageView=(CircleImageView)vList.findViewById(R.id.list_imgDoc);
        textViewName=(TextView)vList.findViewById(R.id.md_name);
        textViewLocation=(TextView)vList.findViewById(R.id.md_location);
        textViewGender=(TextView)vList.findViewById(R.id.md_gender);
        textViewPhone=(TextView)vList.findViewById(R.id.md_phone);
        textViewAge=(TextView)vList.findViewById(R.id.md_age);
        textViewRollno=(TextView)vList.findViewById(R.id.md_rollno);
        progressBar = (ProgressBar)vList.findViewById(R.id.progress_img);

        MemberPOJO member = list.get(i);
        textViewName.setText(member.getFirstName()+" "+member.getLastName());
        textViewLocation.setText(member.getAddress());
        textViewGender.setText("Gender: "+member.getGender());
        textViewPhone.setText(member.getMobilePhone());
        textViewRollno.setText(member.getRollNo());

        if(member.getDob()!=null) {
            String birthdateStr = member.getDob();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            try {
                Date birthdate = df.parse(birthdateStr);
                textViewAge.setText("Age: "+String.valueOf(calculateAge(birthdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        /*MemberPhotoPojo memberPhotoPojo = new MemberPhotoPojo();
        memberPhotoPojo = memberPhotoPojosList.get(i);

        if(member.getId().equals(memberPhotoPojo.getId())){
            if(null!=memberPhotoPojo.getFilepath()){
                   try {
                       Picasso.with(mContext)
                               .load("file://"+memberPhotoPojo.getFilepath())
                               .config(Bitmap.Config.RGB_565)
                               .fit()
                               .centerCrop()
                               .into(imageView);

                       member.setPhotoLocalPath(memberPhotoPojo.getFilepath().toString());

            }catch (Exception e){
                imageView.setImageResource(R.drawable.ic_profile_image_1);
                e.printStackTrace();
            }
            }
        }*/

        DataBaseHelper dataBaseHelper = new DataBaseHelper(mContext);
        String Imagepath = dataBaseHelper.getTempMemberPhoto(member.getId());

        if(null!=Imagepath ||Imagepath == ""){
            try {
                Picasso.with(mContext)
                        .load("file://"+Imagepath)
                        .config(Bitmap.Config.RGB_565)
                        .fit()
                        .centerCrop()
                        .into(imageView);

            }catch (Exception e){
                imageView.setImageResource(R.drawable.ic_profile_image_1);
                e.printStackTrace();
            }
        }else{
            imageView.setImageResource(R.drawable.ic_profile_image_1);
        }



       /* if (null!=member.getPhotoURL()){

            String imgURL= PreferenceUtils.getUrlUploadImage(mContext)+member.getPhotoURL();//Directly loaded from server
            Log.e("img from server-->",imgURL.toString() );

      *//*      // Show progress bar
            progressBar.setVisibility(View.VISIBLE);
            // Hide progress bar on successful load
            Picasso.with(mContext).load(imgURL)
                    .into(imageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (progressBar != null) {
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onError() {

                        }
                    });
*//*

           *//* try {
                imageLoader.displayImage(
                        imgURL,
                        imageView,
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                //    imageLoader.displayImage("http://52.172.221.235:8983/uploads/" + defaultIcon, imageView);
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {

                            }
                        });

            }catch (Exception e){
                imageView.setImageResource(R.drawable.ic_profile_image_1);
                e.printStackTrace();
            }*//*
        }*/

        return vList;
    }

    public int calculateAge(Date birthdate) {
        Calendar birth = Calendar.getInstance();
        birth.setTime(birthdate);
        Calendar today = Calendar.getInstance();

        int yearDifference = today.get(Calendar.YEAR)
                - birth.get(Calendar.YEAR);

        if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
            yearDifference--;
        } else {
            if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
                    && today.get(Calendar.DAY_OF_MONTH) < birth
                    .get(Calendar.DAY_OF_MONTH)) {
                yearDifference--;
            }

        }
        return yearDifference;
    }


}
