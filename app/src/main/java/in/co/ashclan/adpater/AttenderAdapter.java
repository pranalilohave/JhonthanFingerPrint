package in.co.ashclan.adpater;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import in.co.ashclan.fingerprint.R;
import in.co.ashclan.model.AttenderPOJO;
import in.co.ashclan.utils.PreferenceUtils;

public class AttenderAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<AttenderPOJO> list;
    TextView txt_id,txt_name,txt_phone,txt_gender,txt_age,txt_address;
    CircleImageView imageView;
    ImageLoaderConfiguration loaderConfiguration;
    ImageLoader imageLoader = ImageLoader.getInstance();

    public AttenderAdapter(Context mContext, ArrayList<AttenderPOJO> list) {
        this.mContext = mContext;
        this.list = list;


        DisplayImageOptions imageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.ARGB_8888)
                .showImageOnLoading(R.drawable.ic_person)
                .showImageForEmptyUri(R.drawable.ic_person)
                .showImageOnFail(R.drawable.ic_person)
                .build();

        loaderConfiguration = new ImageLoaderConfiguration.Builder(mContext)
                .defaultDisplayImageOptions(imageOptions).build();
        imageLoader.init(loaderConfiguration);

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
        //return Long.parseLong(list.get(i).getId());
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vList;
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vList = new View(mContext);
            vList = inflater.inflate(R.layout.custom_attender_template,null);
        }else {
            vList = (View)view;
        }
        txt_id=(TextView)vList.findViewById(R.id.attender_id);
        txt_name=(TextView)vList.findViewById(R.id.attender_name);
        txt_address=(TextView)vList.findViewById(R.id.attender_address);
        txt_age =(TextView)vList.findViewById(R.id.attender_age);
        txt_gender=(TextView)vList.findViewById(R.id.attender_gender);
        txt_phone=(TextView)vList.findViewById(R.id.attender_phone);

        imageView = (CircleImageView)vList.findViewById(R.id.attender_image);


        final AttenderPOJO attenderPOJO= list.get(i);

        String Fname,Lname;
        Fname = attenderPOJO.getF_name();
        Lname = attenderPOJO.getL_name();

        txt_id.setText(attenderPOJO.getId());
        txt_name.setText(Fname +" "+ Lname);
        txt_address.setText(attenderPOJO.getAddress());
        //txt_age.setText(attenderPOJO.getAge());
        txt_gender.setText(attenderPOJO.getGender());
        txt_phone.setText(attenderPOJO.getPhone());


        if(attenderPOJO.getAge()!=null) {
            String birthdateStr = attenderPOJO.getAge();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            try {
                Date birthdate = df.parse(birthdateStr);
                txt_age.setText("Age: "+String.valueOf(calculateAge(birthdate)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (null!=attenderPOJO.getPhotoURL()){
            String imgURL= PreferenceUtils.getUrlUploadImage(mContext)+attenderPOJO.getPhotoURL();
            try {
                imageLoader.displayImage(imgURL, imageView, new ImageLoadingListener() {
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
            }
        }


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
