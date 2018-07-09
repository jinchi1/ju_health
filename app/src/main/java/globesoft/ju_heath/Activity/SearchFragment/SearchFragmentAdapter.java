package globesoft.ju_heath.Activity.SearchFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import globesoft.ju_heath.Activity.MypageFragment.CoachpageFragment;
import globesoft.ju_heath.DTO.coachDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-04.
 */

public class SearchFragmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<coachDTO> searchArr;

    public SearchFragmentAdapter(Context context, ArrayList<coachDTO> arrayList)
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
        SearchFragmentAdapter.ViewHolder viewHolder;
        final coachDTO data = searchArr.get(position);

        if(v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.row_search_fragment, null);
            viewHolder = new SearchFragmentAdapter.ViewHolder();
            viewHolder.profileImageView = (ImageView)v.findViewById(R.id.profileImageView);
            viewHolder.nameTextView = (TextView)v.findViewById(R.id.nameTextView);
            viewHolder.gymTextView = (TextView)v.findViewById(R.id.gymTextView);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (SearchFragmentAdapter.ViewHolder)view.getTag();
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment ProfileFragment = new CoachpageFragment();
                Bundle bundle = new Bundle();
                bundle.putString("uid", data.uid);
                ProfileFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager.beginTransaction();
                fragmentTransaction3.replace(R.id.main_base_lay, ProfileFragment);
                fragmentTransaction3.addToBackStack(null);
                // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction3.commit();

                view = View.inflate(context, R.layout.fragment_search, null);
                EditText edt_search = (EditText) view.findViewById(R.id.Edt_search);

                // 키보드 감추기
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt_search.getWindowToken(), 0);

            }
        });
        viewHolder.nameTextView.setText(data.name);
        viewHolder.gymTextView.setText(data.gym);
        Glide.with(context).load(data.profile_path).apply(new RequestOptions().error(R.drawable.profile_man).placeholder(R.drawable.profile_man)).into(viewHolder.profileImageView);


        return v;
    }


    private static class ViewHolder {

        public ImageView profileImageView;
        public TextView nameTextView;
        public TextView gymTextView;

    }
}
