package globesoft.ju_heath.Activity.NotificationFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2017-12-20.
 */

public class NotificationAdapter extends  RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Item> items = new ArrayList<>();

    public  NotificationAdapter(Context context, ArrayList<Item> itemArrayList){
        this.context = context;
        this.items = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // XML 가져오는 부분

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // 데이터를 넣어주는 부분

        Item item = items.get(position);

        Glide.with(context).load(item.getProfile_path()).apply(new RequestOptions().placeholder(R.drawable.profile_man)).into(holder.circleImageView);
        //LogUtil.d("hellow"+item.getProfile_path());
        holder.contentTxt.setText(item.getMessage());
        holder.regdateTxt.setText(item.getDiff_date());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }   //카운터


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView circleImageView;
        public TextView contentTxt;
        public TextView regdateTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            circleImageView = (CircleImageView)itemView.findViewById(R.id.profile_image);
            contentTxt = (TextView)itemView.findViewById(R.id.news_pager_content_row_txt);
            regdateTxt = (TextView)itemView.findViewById(R.id.news_pager_regdate_txt);
        }
    }
}