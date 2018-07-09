package globesoft.ju_heath.Activity.MemberFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.SignDTO;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-02-07.
 */

public class SignListAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<SignDTO> signArr;
    FragmentManager fragmentManager;


    public SignListAdapter(Context context, ArrayList<SignDTO> arrayList)
    {
        this.context = context;
        this.signArr = arrayList;
    }

    @Override
    public int getCount() {
        return signArr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItems(ArrayList<SignDTO> addItem) {
        signArr = addItem;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder viewHolder;
        final SignDTO data = signArr.get(position);

        if(v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.row_sign_list, null);
            viewHolder = new ViewHolder();
            viewHolder.signdateTextView = (TextView) v.findViewById(R.id.dateTextView);
            viewHolder.signLay = (RelativeLayout) v.findViewById(R.id.signLay);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        viewHolder.signdateTextView.setText(data.date);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = new SignImageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("sign_path",data.sign_path);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_base_lay,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
        return v;

    }

    private static class ViewHolder {

        public TextView signdateTextView;
        public RelativeLayout signLay;

    }

}
