package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import globesoft.ju_heath.Activity.MemberFragment.PagerAdapter;
import globesoft.ju_heath.Activity.SignUpActivity.SignUpActivity;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-16.
 */

public class PagerScheduleFragment extends Fragment{


    private View view;
    FirebaseFirestore db;
    String uid;
    String thisMonday;
    String nextMonday;
    String next2Monday;
    ViewPager newsViewPager;
    PagerAdapter newPagerAdapter;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(view == null){
            view = inflater.inflate(R.layout.fragment_schedulepager, container, false);
            db=FirebaseFirestore.getInstance();

            uid = ((Ju_healthApp)getContext().getApplicationContext()).getUserUid();

            newsViewPager = (ViewPager) view.findViewById(R.id.viewPager);
            newPagerAdapter = new PagerAdapter(getChildFragmentManager());
            /*
            newPagerAdapter.addFragment(new ScheduleFragment(), "Schedule1");
            newPagerAdapter.addFragment(new ScheduleFragment(), "Schedule2");*/
            newsViewPager.setAdapter(newPagerAdapter);
            newsViewPager.setOffscreenPageLimit(3);

            progressDialog = new ProgressDialog(getContext(),R.style.MyTheme);
            progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            Calendar calendar = Calendar.getInstance();
            calendar.get(Calendar.DAY_OF_WEEK);
            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+calendar.get(Calendar.DAY_OF_WEEK));
            if(calendar.get(Calendar.DAY_OF_WEEK)==1)
            {
                calendar.add(Calendar.DATE,-1);
            }
            calendar.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            thisMonday = format.format(calendar.getTime());
            calendar.add(Calendar.DATE,7);
            nextMonday = format.format(calendar.getTime());
            calendar.add(Calendar.DATE,7);
            next2Monday = format.format(calendar.getTime());
            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+thisMonday);


            if(((Ju_healthApp)getContext().getApplicationContext()).getuserValue().equals("trainer"))
            {
                getSchedule(uid);
            }
            else
            {
                if(((Ju_healthApp)getContext().getApplicationContext()).getuserConnect()) {
                    db.collection("users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            uid = task.getResult().get("coach").toString();
                            getSchedule(uid);
                        }
                    });
                }
                else
                {
                    noSchedule();
                }

            }


        }
        return view;
    }

    public void getSchedule(String uid)
    {
        int thisweek = Integer.parseInt(thisMonday.replace("-",""));
        db.collection("trainer").document(uid).collection("schedule").whereGreaterThanOrEqualTo("compareDate",thisweek).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty())
                    {
                        noSchedule();
                        progressDialog.cancel();
                        newPagerAdapter.notifyDataSetChanged();
                    }
                    else
                    {
                        for (DocumentSnapshot document : task.getResult())
                        {
                            LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+document.getId());
                            if(document.getBoolean("isHalf"))
                            {
                                Bundle bundle = new Bundle();
                                bundle.putString("documentName",document.getId());
                                ScheduleFragmenthalf scheduleFragmenthalf = new ScheduleFragmenthalf();
                                scheduleFragmenthalf.setArguments(bundle);
                                newPagerAdapter.addFragment(scheduleFragmenthalf, document.getId());

                            }
                            else
                            {
                                Bundle bundle = new Bundle();
                                bundle.putString("documentName",document.getId());
                                ScheduleFragment scheduleFragment = new ScheduleFragment();
                                scheduleFragment.setArguments(bundle);
                                newPagerAdapter.addFragment(scheduleFragment,document.getId());

                            }
                        }
                        progressDialog.cancel();
                        newPagerAdapter.notifyDataSetChanged();
                    }

                }
                else{
                    noSchedule();
                    progressDialog.cancel();
                    newPagerAdapter.notifyDataSetChanged();
                }
            }
        });

    }



    public void noSchedule()
    {
        NoScheduleFragment NoscheduleFragment = new NoScheduleFragment();
        newPagerAdapter.addFragment(NoscheduleFragment, "Noschedule");
        progressDialog.cancel();
        newPagerAdapter.notifyDataSetChanged();
    }

}
