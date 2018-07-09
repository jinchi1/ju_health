package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import globesoft.ju_heath.Common.PushEvent;
import globesoft.ju_heath.DTO.LessonDTO;
import globesoft.ju_heath.DTO.makeLessonDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.BusProvider;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-10.
 */

public class ScheduleFragment extends Fragment implements DialogInterface.OnDismissListener {

    View view;
    GridView mGridView;
    ScheduleAdapter adapter;
    ArrayList<LessonDTO> arrayList;
    ImageView writeButton;
    ImageView modifyButton;
    Context context;

    LinearLayout writeButtonLay;
    LinearLayout modifyButtonLay;
    @Override
    public void onDestroy() {
        BusProvider.getInstance().unregister(this);       //이벤트버스 해제
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);               //생성시 이벤트버스등록
    }


    @Subscribe
    public void NotifySign(PushEvent mPushEvent) {
        LogUtil.d("succcccccccccccccccccccccccccccccccccccccccc" + mPushEvent.getResult());
        int signLesson = mPushEvent.getResult();
        arrayList.get(signLesson).sign = true;
        adapter.notifyDataSetChanged();

    }


    LessonDTO lessonDTO;
    FirebaseFirestore db;
    String uid;
    String profile_path;
    String documentName;


    TextView monthTextView;
    TextView dayTimeMon;
    TextView dayTimeTue;
    TextView dayTimeWed;
    TextView dayTimeThu;
    TextView dayTimeFri;
    TextView dayTimeSat;
    TextView dayTimeSun;

    ImageView alphaMon;
    ImageView alphaTue;
    ImageView alphaWed;
    ImageView alphaThu;
    ImageView alphaFri;
    ImageView alphaSat;
    ImageView alphaSun;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_schedule, container, false);
            mGridView = (GridView) view.findViewById(R.id.scheduleGridView);
            writeButton = (ImageView) view.findViewById(R.id.writeButton);
            writeButtonLay = (LinearLayout) view.findViewById(R.id.writeButtonLay);
            monthTextView = (TextView) view.findViewById(R.id.monthTextView);
            dayTimeMon = (TextView) view.findViewById(R.id.dayTextViewMon);
            dayTimeTue = (TextView) view.findViewById(R.id.dayTextViewTue);
            dayTimeWed = (TextView) view.findViewById(R.id.dayTextViewWed);
            dayTimeThu = (TextView) view.findViewById(R.id.dayTextViewThu);
            dayTimeFri = (TextView) view.findViewById(R.id.dayTextViewFri);
            dayTimeSat = (TextView) view.findViewById(R.id.dayTextViewSat);
            dayTimeSun = (TextView) view.findViewById(R.id.dayTextViewSun);
            modifyButton = (ImageView) view.findViewById(R.id.modifyButton);
            modifyButtonLay = (LinearLayout) view.findViewById(R.id.modifyButtonLay);
            alphaMon = (ImageView) view.findViewById(R.id.alphaMon);
            alphaTue = (ImageView) view.findViewById(R.id.alphaTue);
            alphaWed = (ImageView) view.findViewById(R.id.alphaWed);
            alphaThu = (ImageView) view.findViewById(R.id.alphaThu);
            alphaFri = (ImageView) view.findViewById(R.id.alphaFri);
            alphaSat = (ImageView) view.findViewById(R.id.alphaSat);
            alphaSun = (ImageView) view.findViewById(R.id.alphaSun);


            documentName = getArguments().getString("documentName");


        }

        context = getContext();
        db = FirebaseFirestore.getInstance();
        arrayList = new ArrayList<>();
        lessonDTO = new LessonDTO();

        progressDialog = new ProgressDialog(getContext(),R.style.MyTheme);
        progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        if (((Ju_healthApp) getContext().getApplicationContext()).getuserValue().equals("trainer")) {
            uid = ((Ju_healthApp) getContext().getApplicationContext()).getUserUid();
            getSchedule(uid);
            adapter = new ScheduleAdapter(context, arrayList, uid, documentName, false);
            mGridView.setAdapter(adapter);
        } else {
            String userUid = ((Ju_healthApp) getContext().getApplicationContext()).getUserUid();
            writeButtonLay.setVisibility(View.INVISIBLE);
            modifyButtonLay.setVisibility(View.INVISIBLE);
            db.collection("users").document(userUid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    uid = task.getResult().get("coach").toString();
                    if(task.getResult().contains("profile_path")&&task.getResult().get("profile_path")!=null) profile_path = task.getResult().get("profile_path").toString();
                    LogUtil.d("ggggggggggggggggggggggggggggggggggggggggggg" + uid);
                    getSchedule(uid);
                    adapter = new ScheduleAdapter(context, arrayList, uid, documentName, false,profile_path);
                    mGridView.setAdapter(adapter);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    LogUtil.d("ggggggggggggggggggggggggggggggggggggg" + e.getMessage());
                }
            });
        }


        writeButtonLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat intformat = new SimpleDateFormat("yyyyMMdd");
                Calendar calendar = Calendar.getInstance();
                final int thisweek = Integer.parseInt(intformat.format(calendar.getTime()));
                db.collection("trainer").document(uid).collection("schedule").whereGreaterThanOrEqualTo("compareDate", thisweek).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int tempdate = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                int a = Integer.parseInt(String.valueOf(Math.round(document.getDouble("compareDate"))));
                                if (tempdate < a) {
                                    tempdate = a;
                                }
                            }
                            if (thisweek >= tempdate) {
                                WriteScheduleDialog writeScheduleDialog = new WriteScheduleDialog(getContext(), true, "shutup");
                                writeScheduleDialog.setOnDismissListener(ScheduleFragment.this);
                                writeScheduleDialog.show();

                                progressDialog.cancel();
                            } else {
                                String a = String.valueOf(tempdate);
                                WriteScheduleDialog writeScheduleDialog = new WriteScheduleDialog(getContext(), false, a);
                                writeScheduleDialog.setOnDismissListener(ScheduleFragment.this);
                                writeScheduleDialog.show();

                                progressDialog.cancel();
                            }
                        }
                    }
                });

            }
        });

        modifyButtonLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ab.setView(R.layout.dialog_custom_modify);
                }
                ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Bundle bundle = new Bundle();
                        bundle.putString("startDay", documentName);
                        bundle.putInt("people", lessonDTO.user_count);
                        ModifyScheduleFragment modifyScheduleFragment = new ModifyScheduleFragment();
                        modifyScheduleFragment.setArguments(bundle);
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, modifyScheduleFragment);
                        fragmentTransaction.addToBackStack(null);
                        // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.commit();


                    }
                });
                ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = ab.show();
                TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                Button posButton = (Button) dialog.findViewById(android.R.id.button1);
                posButton.setTextColor(Color.parseColor("#ee2b47"));
                Button negButton = (Button) dialog.findViewById(android.R.id.button2);
                negButton.setTextColor(Color.parseColor("#333333"));
                ///////////////////////////////////////////////////////////////////

            }
        });


        return view;
    }

    public void getSchedule(String uid) {
        for (int i = 0; i < 126; i++) {
            lessonDTO.check = false;
            lessonDTO.state = false;
            lessonDTO.lesson = i;
            arrayList.add(lessonDTO);
        }

        db.collection("trainer").document(uid)
                .collection("schedule").document(documentName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()) {
                        Gson gson = new Gson();
                        lessonDTO = new LessonDTO();
                        for (int i = 0; i < 126; i++) {
                            if (documentSnapshot.get(String.valueOf(i)) != null) {

                                lessonDTO = gson.fromJson(documentSnapshot.get(String.valueOf(i)).toString(), LessonDTO.class);
                                int position = lessonDTO.lesson;
                                arrayList.set(position, lessonDTO);
                                LogUtil.d("zzzzzzzzzzzzz" + lessonDTO.lesson);
                            }
                        }


                        matchDate();
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LogUtil.d(e.getMessage());
            }
        });

    }

    @Override
    public void onDismiss(DialogInterface $dialog) {
        // TODO Auto-generated method stub
        WriteScheduleDialog dialog = (WriteScheduleDialog) $dialog;
        makeLessonDTO makeLessonDTO = dialog.getmakeLessonDTO();
        if (makeLessonDTO.maketrue) {
            if (makeLessonDTO.lessonTime == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("startDay", makeLessonDTO.startDay);
                bundle.putInt("people", makeLessonDTO.people);
                bundle.putInt("week", makeLessonDTO.week);
                bundle.putBoolean("isHalf", false);
                PagerWriteScheduleFragment pagerWriteScheduleFragment = new PagerWriteScheduleFragment();
                pagerWriteScheduleFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_base_lay, pagerWriteScheduleFragment);
                fragmentTransaction.addToBackStack(null);
                // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.commit();
            } else if (makeLessonDTO.lessonTime == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("startDay", makeLessonDTO.startDay);
                bundle.putInt("people", makeLessonDTO.people);
                bundle.putInt("week", makeLessonDTO.week);
                bundle.putBoolean("isHalf", true);
                PagerWriteScheduleFragment pagerWriteScheduleFragment = new PagerWriteScheduleFragment();
                pagerWriteScheduleFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_base_lay, pagerWriteScheduleFragment);
                fragmentTransaction.addToBackStack(null);
                // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.commit();
            }
        } else {

        }
    }

    public void matchDate() {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(documentName);
            SimpleDateFormat month = new SimpleDateFormat("yyyy.MM");
            monthTextView.setText(month.format(date));
            ArrayList<Date> dateArrayList = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            for (int i = 0; i < 7; i++) {
                dateArrayList.add(calendar.getTime());
                calendar.add(Calendar.DATE, 1);
            }
            long now = System.currentTimeMillis();
            Date today = new Date(now);
            LogUtil.d("llllllllllllllllllllllll" + today.toString());
            SimpleDateFormat format = new SimpleDateFormat("dd");
            SimpleDateFormat tempformat = new SimpleDateFormat("yyyy.MM.dd");
            SimpleDateFormat tempmonthformat = new SimpleDateFormat("yyyyMM");

            if (month.format(date).equals(month.format(today)) || Integer.parseInt(tempmonthformat.format(date)) < Integer.parseInt(tempmonthformat.format(today))) {
                int todaydate = 9;
                for (int i = 0; i < 7; i++) {
                    if (tempformat.format(today).equals(tempformat.format(dateArrayList.get(i)))) {
                        todaydate = i;
                        break;
                    }
                }
                switch (todaydate) {
                    case 0:
                        dayTimeMon.setTextColor(Color.parseColor("#262a4f"));
                        dayTimeMon.setPaintFlags(dayTimeMon.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                        break;
                    case 1:
                        alphaMon.setVisibility(View.VISIBLE);
                        dayTimeTue.setTextColor(Color.parseColor("#262a4f"));
                        dayTimeTue.setPaintFlags(dayTimeMon.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                        break;
                    case 2:
                        alphaMon.setVisibility(View.VISIBLE);
                        alphaTue.setVisibility(View.VISIBLE);
                        dayTimeWed.setTextColor(Color.parseColor("#262a4f"));
                        dayTimeWed.setPaintFlags(dayTimeMon.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                        break;
                    case 3:
                        alphaMon.setVisibility(View.VISIBLE);
                        alphaTue.setVisibility(View.VISIBLE);
                        alphaWed.setVisibility(View.VISIBLE);
                        dayTimeThu.setTextColor(Color.parseColor("#262a4f"));
                        dayTimeThu.setPaintFlags(dayTimeMon.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                        break;
                    case 4:
                        alphaMon.setVisibility(View.VISIBLE);
                        alphaTue.setVisibility(View.VISIBLE);
                        alphaWed.setVisibility(View.VISIBLE);
                        alphaThu.setVisibility(View.VISIBLE);
                        dayTimeFri.setTextColor(Color.parseColor("#262a4f"));
                        dayTimeFri.setPaintFlags(dayTimeMon.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                        break;
                    case 5:
                        alphaMon.setVisibility(View.VISIBLE);
                        alphaTue.setVisibility(View.VISIBLE);
                        alphaWed.setVisibility(View.VISIBLE);
                        alphaThu.setVisibility(View.VISIBLE);
                        alphaFri.setVisibility(View.VISIBLE);
                        dayTimeSat.setTextColor(Color.parseColor("#262a4f"));
                        dayTimeSat.setPaintFlags(dayTimeMon.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                        break;
                    case 6:
                        alphaMon.setVisibility(View.VISIBLE);
                        alphaTue.setVisibility(View.VISIBLE);
                        alphaWed.setVisibility(View.VISIBLE);
                        alphaThu.setVisibility(View.VISIBLE);
                        alphaFri.setVisibility(View.VISIBLE);
                        alphaSat.setVisibility(View.VISIBLE);
                        dayTimeSun.setTextColor(Color.parseColor("#262a4f"));
                        dayTimeSun.setPaintFlags(dayTimeMon.getPaintFlags()| Paint.FAKE_BOLD_TEXT_FLAG);
                        break;
                    case 9:
                        break;

                }


            }

            dayTimeMon.setText(format.format(dateArrayList.get(0)));
            dayTimeTue.setText(format.format(dateArrayList.get(1)));
            dayTimeWed.setText(format.format(dateArrayList.get(2)));
            dayTimeThu.setText(format.format(dateArrayList.get(3)));
            dayTimeFri.setText(format.format(dateArrayList.get(4)));
            dayTimeSat.setText(format.format(dateArrayList.get(5)));
            dayTimeSun.setText(format.format(dateArrayList.get(6)));

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

}


