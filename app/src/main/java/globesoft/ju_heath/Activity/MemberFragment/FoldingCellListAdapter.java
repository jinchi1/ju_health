package globesoft.ju_heath.Activity.MemberFragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.daimajia.easing.linear.Linear;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ramotion.foldingcell.FoldingCell;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import globesoft.ju_heath.Activity.ScheduleFragment.PagerScheduleFragment;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2017-12-19.
 */

public class FoldingCellListAdapter extends BaseAdapter {
    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    Timer timer;
    TimerTask timerTask;
    Context context;
    ArrayList<usersDTO> objects;

    int tempNowCount;
    int tempTotalCount;
    FirebaseFirestore db;

    android.support.v4.app.FragmentManager fragmentManager;

    EditText ptWriteEditText;

    public FoldingCellListAdapter(Context context, ArrayList<usersDTO> objects) {
        this.context = context;
        this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // get item for selected view
        final usersDTO usersDTO = objects.get(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        final ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(context);
            cell = (FoldingCell) vi.inflate(R.layout.cell, parent, false);
            // binding view parts to view holder
            viewHolder.aaa = (LinearLayout)cell.findViewById(R.id.aaa);
            viewHolder.member_photo_ImageView = (ImageView)cell.findViewById(R.id.member_photoImageView);
            viewHolder.nameTextView = (TextView) cell.findViewById(R.id.nameTextView);
            viewHolder.nowCountTextView = (TextView)cell.findViewById(R.id.nowCountTextView);
            viewHolder.totalCountTextView = (TextView)cell.findViewById(R.id.totalCountTextView);
            viewHolder.sexImageView = (ImageView)cell.findViewById(R.id.sexImageView);
            viewHolder.signListLay = (LinearLayout)cell.findViewById(R.id.signListLay);
            viewHolder.contentNameTextView = (TextView) cell.findViewById(R.id.contentNameTextView);
            viewHolder.content_photo_ImageView = (ImageView)cell.findViewById(R.id.head_image);
            viewHolder.contentNowCountTextView = (TextView)cell.findViewById(R.id.contentNowCountTextView);
            viewHolder.contentTotalCountTextView = (TextView)cell.findViewById(R.id.contentTotalCountTextView);
            viewHolder.contentAgeTextView = (TextView)cell.findViewById(R.id.contentAgeTextView);
            viewHolder.contentWeightTextView = (TextView)cell.findViewById(R.id.contentWeightTextView);
            viewHolder.contentBirthTextView = (TextView)cell.findViewById(R.id.contentBirthTextView);
            viewHolder.contentPhoneImageView = (LinearLayout) cell.findViewById(R.id.contentPhoneLinearLayout);
            viewHolder.contentSexImageView = (ImageView)cell.findViewById(R.id.contentSexImageView);
            viewHolder.contentPTWriteLay = (LinearLayout)cell.findViewById(R.id.contentPTWriteLay);
            viewHolder.contentSignListLay = (LinearLayout)cell.findViewById(R.id.contentsignListLay);

            cell.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) cell.getTag();
        }

        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();

        viewHolder.nameTextView.setText(usersDTO.name);
        if(usersDTO.profile_path!=null&&!usersDTO.profile_path.equals(""))
        {
            Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(usersDTO.profile_path).into(viewHolder.member_photo_ImageView);
            Glide.with(context).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.profile_man).error(R.drawable.profile_man)).load(usersDTO.profile_path).into(viewHolder.content_photo_ImageView);
        }
        if(usersDTO.gender!=null&&usersDTO.gender.equals("M"))
        {
            viewHolder.sexImageView.setImageResource(R.drawable.man);
            viewHolder.contentSexImageView.setImageResource(R.drawable.man);
        }
        else if(usersDTO.gender!=null&&usersDTO.gender.equals("F"))
        {
            viewHolder.sexImageView.setImageResource(R.drawable.woman);
            viewHolder.contentSexImageView.setImageResource(R.drawable.woman);
        }

        db = FirebaseFirestore.getInstance();


        viewHolder.contentNameTextView.setText(usersDTO.name);
        viewHolder.contentAgeTextView.setText(usersDTO.age);
        viewHolder.contentWeightTextView.setText(usersDTO.weight);
        viewHolder.contentBirthTextView.setText(usersDTO.birth);


        if(usersDTO.totalCount!=null&&!usersDTO.totalCount.equals("")){
            tempTotalCount = Integer.parseInt(usersDTO.totalCount);
        }
        else
        {
            tempTotalCount = 0;
        }
        if(usersDTO.nowCount!=null&&!usersDTO.nowCount.equals("")){
            tempNowCount = Integer.parseInt(usersDTO.nowCount);
        }
        else
        {
            tempNowCount = 0;
        }

        if(usersDTO.nowCount!=null)
        {
            viewHolder.nowCountTextView.setText(usersDTO.nowCount);
            viewHolder.contentNowCountTextView.setText(usersDTO.nowCount);
        }
        if(usersDTO.totalCount!=null)
        {
            viewHolder.totalCountTextView.setText(usersDTO.totalCount);
            viewHolder.contentTotalCountTextView.setText(usersDTO.totalCount);
        }

        viewHolder.contentPhoneImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + usersDTO.phone));
                context.startActivity(intent);
            }
        });
        viewHolder.contentPTWriteLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    View view1 = LayoutInflater.from(context).inflate(R.layout.dialog_custom_ptwrite,null);
                    ab.setView(view1);
                    ptWriteEditText = (EditText)view1.findViewById(R.id.ptWriteEditText);
                   if(usersDTO.totalCount!=null&&!usersDTO.totalCount.equals("")) ptWriteEditText.setText(usersDTO.totalCount);
                }
                ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String ptcountWrite = ptWriteEditText.getText().toString();

                        if (ptcountWrite != null && !ptcountWrite.equals("")) {
                            db.collection("trainer").document(usersDTO.coach).collection("users").document(usersDTO.uid).update("totalCount", ptcountWrite).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    db.collection("users").document(usersDTO.uid).update("totalCount", ptcountWrite).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            viewHolder.contentTotalCountTextView.setText(ptcountWrite);
                                            usersDTO.totalCount = ptcountWrite;
                                            notifyDataSetChanged();
                                        }
                                    });
                                }
                            });

                        }
                        else {
                            Toast.makeText(context,"PT횟수를 입력해주세요",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                });
                AlertDialog dialog = ab.show();

                Button posButton = (Button)dialog.findViewById(android.R.id.button1);
                posButton.setTextColor(Color.parseColor("#ee2b47"));
                Button negButton = (Button)dialog.findViewById(android.R.id.button2);
                negButton.setTextColor(Color.parseColor("#333333"));

            }
        });
        viewHolder.signListLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                android.support.v4.app.Fragment fragment = new SignListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("coachUid",usersDTO.coach);
                bundle.putString("userUid",usersDTO.uid);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_base_lay, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        viewHolder.contentSignListLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                android.support.v4.app.Fragment fragment = new SignListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("coachUid",usersDTO.coach);
                bundle.putString("userUid",usersDTO.uid);
                fragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.main_base_lay, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });



        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
    }

    // View lookup cache
    private static class ViewHolder {
        ImageView member_photo_ImageView;
        TextView nameTextView;
        TextView nowCountTextView;
        TextView totalCountTextView;
        ImageView sexImageView;
        LinearLayout aaa;
        LinearLayout signListLay;
        //////////////////////////////////////////


        ImageView content_photo_ImageView;
        TextView contentNameTextView;
        TextView contentNowCountTextView;
        TextView contentTotalCountTextView;
        TextView contentAgeTextView;
        TextView contentWeightTextView;
        TextView contentBirthTextView;
        LinearLayout contentPhoneImageView;
        LinearLayout contentPTWriteLay;
        ImageView contentSexImageView;
        LinearLayout contentSignListLay;


    }


}

