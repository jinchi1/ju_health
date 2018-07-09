package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import globesoft.ju_heath.Activity.MypageFragment.UserMypageFragment;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-19.
 */

public class ScheduleUserListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<usersDTO> userArr;
    private usersDTO data;

    public ScheduleUserListAdapter(Context context, ArrayList<usersDTO> arrayList)
    {
        this.context = context;
        this.userArr = arrayList;
    }

    @Override
    public int getCount() {
        return userArr.size();
    }

    @Override
    public usersDTO getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItems(ArrayList<usersDTO> addItem) {
        userArr = addItem;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder viewHolder;
        data = userArr.get(position);

        if(v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.row_userlist, null);
            viewHolder = new ViewHolder();
            viewHolder.layout = (LinearLayout)v.findViewById(R.id.layout);
            viewHolder.callLay = (LinearLayout)v.findViewById(R.id.callLayout);
            viewHolder.signLay = (LinearLayout)v.findViewById(R.id.signCallLay);
            viewHolder.profileImageView = (ImageView)v.findViewById(R.id.profileImageView);
            viewHolder.nameTextView = (TextView)v.findViewById(R.id.nameTextView);
            viewHolder.nowCountTextView = (TextView)v.findViewById(R.id.nowCountTextView);
            viewHolder.totalCountTextView = (TextView)v.findViewById(R.id.totalCountTextView);

            v.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }


        viewHolder.nameTextView.setText(data.name);
        if(data.totalCount!=null)viewHolder.totalCountTextView.setText(data.totalCount);
        if(data.nowCount!=null)viewHolder.nowCountTextView.setText(data.nowCount);
        Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man))
                .load(data.profile_path).into(viewHolder.profileImageView);

        viewHolder.callLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + data.phone));
                context.startActivity(intent);
            }
        });

      return v;
    }


    private static class ViewHolder {

        public LinearLayout layout;
        public LinearLayout callLay;
        public LinearLayout signLay;
        public ImageView profileImageView;
        public TextView nameTextView;
        public TextView nowCountTextView;
        public TextView totalCountTextView;

    }
}
