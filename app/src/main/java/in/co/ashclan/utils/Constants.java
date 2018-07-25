package in.co.ashclan.utils;

import android.content.Context;
import android.widget.Toast;

public class  Constants {

    public static final String SUBMIT="submit";
    public static final String UPDATEWITHIMAGE="updateWithImage";
    public static final String UDATEWITHOUTIMAGE="updateWithOutImage";

    public static Constants  instance;


    public static Constants getInstance(){
        if (null==instance){
            instance = new Constants();
        }
        return instance;
    }

    public static void setURL(Context context, int selectedServer) {
//        Toast.makeText(context,selectedServer,Toast.LENGTH_LONG).show();

        switch(selectedServer){
            case 0:
                Toast.makeText(context,"local",Toast.LENGTH_LONG).show();

                PreferenceUtils.setUrlLogin(context,"http://52.172.221.235:8983/api/login");
                PreferenceUtils.setUrlGetAllMembers(context,"http://52.172.221.235:8983/api/get_all_members");
                PreferenceUtils.setUrlGetAllEvents(context,"http://52.172.221.235:8983/api/get_all_events");
                PreferenceUtils.setUrlCreateMember(context,"http://52.172.221.235:8983/api/create_member");
                PreferenceUtils.setUrlAddCheckin(context,"http://52.172.221.235:8983/api/add_checkin");

                PreferenceUtils.setUrlGetAllMemberEvents(context,"http://52.172.221.235:8983/api/get_all_member_events");
                PreferenceUtils.setUrlGetContributions(context,"http://52.172.221.235:8983/api/get_all_member_contributions");
                PreferenceUtils.setUrlGetPledges(context,"http://52.172.221.235:8983/api/get_all_member_pledges");
                PreferenceUtils.setUrlGetFamily(context,"http://52.172.221.235:8983/api/get_all_member_family");
                PreferenceUtils.setUrlGetGroup(context,"http://52.172.221.235:8983/api/get_all_member_groups");
                PreferenceUtils.setUrlGetAllMemberDetails(context,"http://52.172.221.235:8983/api/get_all_member_data");




                PreferenceUtils.setUrlUploadImage(context,"http://52.172.221.235:8983/uploads/");
                PreferenceUtils.setUrlUpdateMember(context,"http://52.172.221.235:8983/api/update_member");

                break;

            case 1:
                Toast.makeText(context,"pentecost",Toast.LENGTH_LONG).show();

                //https://bwc.pentecostchurch.org/
                PreferenceUtils.setUrlLogin(context,"https://bwc.pentecostchurch.org/api/login");
                PreferenceUtils.setUrlGetAllMembers(context,"https://bwc.pentecostchurch.org/api/get_all_members");
                PreferenceUtils.setUrlGetAllEvents(context,"https://bwc.pentecostchurch.org/api/get_all_events");
                PreferenceUtils.setUrlCreateMember(context,"https://bwc.pentecostchurch.org/api/create_member");
                PreferenceUtils.setUrlAddCheckin(context,"https://bwc.pentecostchurch.org/api/add_checkin");
                PreferenceUtils.setUrlGetAllMemberEvents(context,"https://bwc.pentecostchurch.org/api/get_all_member_events");
                PreferenceUtils.setUrlGetContributions(context,"https://bwc.pentecostchurch.org/api/get_all_member_contributions");
                PreferenceUtils.setUrlGetPledges(context,"https://bwc.pentecostchurch.org/api/get_all_member_pledges");

                PreferenceUtils.setUrlGetAllMemberDetails(context,"https://bwc.pentecostchurch.org/api/get_all_member_data");

                PreferenceUtils.setUrlGetFamily(context,"https://bwc.pentecostchurch.org/api/get_all_member_family");
                PreferenceUtils.setUrlGetGroup(context,"https://bwc.pentecostchurch.org/api/get_all_member_groups");PreferenceUtils.setUrlUploadImage(context,"https://bwc.pentecostchurch.org/uploads/");
                PreferenceUtils.setUrlUpdateMember(context,"https://bwc.pentecostchurch.org/api/update_member");

                break;

            default:
                Toast.makeText(context,"default",Toast.LENGTH_LONG).show();

                PreferenceUtils.setUrlLogin(context,"http://52.172.221.235:8983/api/login");
                PreferenceUtils.setUrlGetAllMembers(context,"http://52.172.221.235:8983/api/get_all_members");
                PreferenceUtils.setUrlGetAllEvents(context,"http://52.172.221.235:8983/api/get_all_events");
                PreferenceUtils.setUrlCreateMember(context,"http://52.172.221.235:8983/api/create_member");
                PreferenceUtils.setUrlAddCheckin(context,"http://52.172.221.235:8983/api/add_checkin");
                PreferenceUtils.setUrlGetAllMemberEvents(context,"http://52.172.221.235:8983/api/get_all_member_events");
                PreferenceUtils.setUrlGetContributions(context,"http://52.172.221.235:8983/api/get_all_member_contributions");
                PreferenceUtils.setUrlGetPledges(context,"http://52.172.221.235:8983/api/get_all_member_pledges");
                PreferenceUtils.setUrlGetFamily(context,"http://52.172.221.235:8983/api/get_all_member_family");
                PreferenceUtils.setUrlGetGroup(context,"http://52.172.221.235:8983/api/get_all_member_groups");
                PreferenceUtils.setUrlUploadImage(context,"http://52.172.221.235:8983/uploads/");
                PreferenceUtils.setUrlUpdateMember(context,"http://52.172.221.235:8983/api/update_member");
                PreferenceUtils.setUrlGetAllMemberDetails(context,"http://52.172.221.235:8983/api/get_all_member_data");

                break;
        }
    }

}

