package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.DashboardActivity;
import com.android.aquagiraffe.rotaryconnect.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;



public class ViewerPagerAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<String> reasonArrayList;
    String[] string;
    ArrayList<HashMap<String, String>> datalist;
    HashMap<String, String> data = new HashMap<>();
    String strName = "NAME";
    String strTittleOfEvent = "TittleOfEvent";
    String EventDateTime = "EventDateTime";
   // String Reason_Celebrate = "Reason_Celebrate";
    String PROFILEPHOTO = "PROFILEPHOTO";
    RelativeLayout relativeLayout;


    public ViewerPagerAdapter(DashboardActivity dashboardActivity, ArrayList<String> reasonArrayList, String[] strings, HashMap<String, String> hashMap, ArrayList<HashMap<String, String>> list) {
        this.mContext = dashboardActivity;
        this.string = strings;
        this.reasonArrayList = reasonArrayList;
        this.data = hashMap;
        this.datalist = list;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return  reasonArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view == ((RelativeLayout)o);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = inflater.inflate(R.layout.birthday_single, (ViewGroup) container, false);
        data = datalist.get(position);
        //final NotificationModel imageModel = (NotificationModel) this.getItem(position);
        TextView txtBirthDayName = itemView.findViewById(R.id.txtBirthDayName);
        txtBirthDayName.setText(data.get(strName));
        TextView txtEvent = itemView.findViewById(R.id.txtEvent);
        txtEvent.setText(data.get(strTittleOfEvent));

        TextView txtEventDate = itemView.findViewById(R.id.txtEventDate);
        txtEventDate.setText(data.get(EventDateTime));

        TextView txtAnniversary = itemView.findViewById(R.id.txtAnniversary);
        relativeLayout = itemView.findViewById(R.id.relative);

       /* if (data.get(Reason_Celebrate).equalsIgnoreCase("Birthday"))
        {
            relativeLayout.setBackgroundColor(Color.parseColor("#B421FE"));
        }else if (data.get(Reason_Celebrate).equalsIgnoreCase("Event"))
        {
            relativeLayout.setBackgroundColor(Color.parseColor("#4AD2E6"));
        }else {
            relativeLayout.setBackgroundColor(Color.parseColor("#578BEE"));
        }
        txtAnniversary.setText(data.get(Reason_Celebrate));*/

        ImageView profile = itemView.findViewById(R.id.profile);

        String url = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/Profile/"+data.get(PROFILEPHOTO) ;
        Picasso.with(mContext)
                .load(url)
                .into(profile, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });


        ((ViewGroup) container).addView(itemView);
        return itemView;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }

    private int[] sliderImageId = new int[]{
            R.drawable.add_icon,R.drawable.address
    };

}
