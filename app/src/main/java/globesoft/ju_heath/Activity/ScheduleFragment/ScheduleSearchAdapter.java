package globesoft.ju_heath.Activity.ScheduleFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-18.
 */

public class ScheduleSearchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<usersDTO> searchArr;
    usersDTO data;

    public ScheduleSearchAdapter(Context context, ArrayList<usersDTO> arrayList)
    {
        this.context = context;
        this.searchArr = arrayList;
    }

    @Override
    public int getCount() {
        return searchArr.size();
    }

    @Override
    public usersDTO getItem(int i) {
        return searchArr.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItems(ArrayList<usersDTO> addItem) {
        searchArr = addItem;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder viewHolder;
        data = searchArr.get(position);

        if(v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.row_search, null);
            viewHolder = new ViewHolder();
            viewHolder.profileImageView = (ImageView)v.findViewById(R.id.profileImageView);
            viewHolder.nameTextView = (TextView)v.findViewById(R.id.nameTextView);
            viewHolder.ptTextView = (TextView)v.findViewById(R.id.ptTextView);
            viewHolder.totalptTextView = (TextView)v.findViewById(R.id.totalptTextView);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.nameTextView.setText(data.name);
       if(data.nowCount!=null&&!data.nowCount.equals("")) viewHolder.ptTextView.setText(String.valueOf(data.nowCount));
       if(data.totalCount!=null&&!data.totalCount.equals(""))  viewHolder.totalptTextView.setText(String.valueOf(data.totalCount));
       if(data.profile_path!=null&&!data.totalCount.equals("")) Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(data.profile_path).into(viewHolder.profileImageView);

        return v;
    }


    private static class ViewHolder {

        public ImageView profileImageView;
        public TextView nameTextView;
        public TextView ptTextView;
        public TextView totalptTextView;

    }
}
