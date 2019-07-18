package com.android.aquagiraffe.rotaryconnect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.aquagiraffe.rotaryconnect.DisplayNotification;
import com.android.aquagiraffe.rotaryconnect.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BirthDayAdapter extends BaseAdapter
{
    Context c;
    ArrayList<HashMap<String, String>> datalist;
    HashMap<String, String> data = new HashMap<>();
    // LayoutInflater inflater;
    boolean click = true;
    public String NAME = "NAME";
    public String PROFILEPHOTO = "PROFILEPHOTO";
    public String Reason_Celebrate = "Reason_Celebrate";
    private String mimetype;

    int[] integer;
    String[] string;


    public BirthDayAdapter(DisplayNotification displayNotification, HashMap<String, String> birthday, ArrayList<HashMap<String, String>> birthdayList,String[] string, int[] integer)
    {
        this.c = displayNotification;
        this.data = birthday;
        this.datalist =birthdayList;
        this.integer = integer;
        this.string = string;
    }

    @Override
    public int getCount()
    {
        return datalist.size();
    }

    @Override
    public Object getItem(int position)
    {
        return datalist.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup)
    {
        final birthdayHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(c).inflate(R.layout.birthday_adapter, viewGroup, false);
        }
        data = datalist.get(position);
        holder = new birthdayHolder();
        assert convertView != null;
        holder.img_birthday = convertView.findViewById(R.id.img_bod);
        holder.txtName = convertView.findViewById(R.id.str_name);
        holder.txtbirthDate = convertView.findViewById(R.id.txt_dob);
        holder.img_gift = convertView.findViewById(R.id.img_gift);

        final Animation animation_4 = AnimationUtils.loadAnimation(c,R.anim.anim_shake);

        holder.txtName.setText(data.get(NAME));
        holder.txtbirthDate.setText(data.get(Reason_Celebrate));

        String url = "https://s3-ap-southeast-1.amazonaws.com/awsaquabucket/P_Rotary/Profile/" + data.get(PROFILEPHOTO);

        Picasso.with(c)
                .load(url)
                .into(holder.img_birthday, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {

                    }

                    @Override
                    public void onError()
                    {

                    }
                });

        if(data.get(Reason_Celebrate).equals("Event"))
        {
            holder.img_gift.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.img_gift.setAnimation(animation_4);
            animation_4.setAnimationListener(new Animation.AnimationListener()
            {
                @Override
                public void onAnimationStart(Animation animation)
                {

                }

                @Override
                public void onAnimationEnd(Animation animation)
                {
                    holder.img_gift.startAnimation(animation_4);
                }

                @Override
                public void onAnimationRepeat(Animation animation)
                {

                }
            });
        }

        return convertView;
    }

}

class birthdayHolder
{
    TextView txtName,txtbirthDate,txtProfile;
    ImageView img_birthday, img_gift;
}
