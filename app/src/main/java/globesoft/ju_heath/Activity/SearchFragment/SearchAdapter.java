package globesoft.ju_heath.Activity.SearchFragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.coachDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2017-12-20.
 */

public class SearchAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<coachDTO> searchArr;

    public SearchAdapter(Context context, ArrayList<coachDTO> arrayList)
    {
        this.context = context;
        this.searchArr = arrayList;
    }

    @Override
    public int getCount() {
        return searchArr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItems(ArrayList<coachDTO> addItem) {
        searchArr = addItem;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder viewHolder;
        final coachDTO data = searchArr.get(position);

        if(v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.row_search_trainer, null);
            viewHolder = new ViewHolder();
            viewHolder.profileImageView = (ImageView)v.findViewById(R.id.profileImageView);
            viewHolder.nameTextView = (TextView)v.findViewById(R.id.nameTextView);
            viewHolder.gymTextView = (TextView)v.findViewById(R.id.gymTextView);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent1 = new Intent();
                intent1.putExtra("uid",data.uid);
                intent1.putExtra("coach_token",data.token);
                ((SearchActivity)context).setResult(1,intent1);
                ((SearchActivity)context).finish();

                /*Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("uid",data.uid);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                context.startActivity(intent);
                ((SearchActivity)context).finish();*/
            }
        });
        viewHolder.nameTextView.setText(data.name);
        //viewHolder.gymTextView.setText(data.gym);
        Glide.with(context).load(data.profile_path).apply(new RequestOptions().error(R.drawable.profile_man).placeholder(R.drawable.profile_man)).into(viewHolder.profileImageView);

        return v;
    }


    private static class ViewHolder {

        public ImageView profileImageView;
        public TextView nameTextView;
        public TextView gymTextView;

    }
}
