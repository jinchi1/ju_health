package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import globesoft.ju_heath.DTO.LessonDTO;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-26.
 */

public class ModifyScheduleFragmenthalf extends Fragment implements View.OnClickListener, DialogInterface.OnDismissListener{

    View view;
    GridView mGridView;
    ImageView uploadButton;
    ImageView modifyButton;
    TextView timeMon;
    TextView timeTue;
    TextView timeWed;
    TextView timeThu;
    TextView timeFri;
    TextView timeSat;
    TextView timeSun;
    TextView dayTimeMon;
    TextView dayTimeTue;
    TextView dayTimeWed;
    TextView dayTimeThu;
    TextView dayTimeFri;
    TextView dayTimeSat;
    TextView dayTimeSun;

    TextView monthTextView;

    TextView time06half;
    TextView time07half;
    TextView time08half;
    TextView time09half;
    TextView time10half;
    TextView time11half;
    TextView time12half;
    TextView time13half;
    TextView time14half;
    TextView time15half;
    TextView time16half;
    TextView time17half;
    TextView time18half;
    TextView time19half;
    TextView time20half;
    TextView time21half;
    TextView time22half;
    TextView time23half;
    TextView time06;
    TextView time07;
    TextView time08;
    TextView time09;
    TextView time10;
    TextView time11;
    TextView time12;
    TextView time13;
    TextView time14;
    TextView time15;
    TextView time16;
    TextView time17;
    TextView time18;
    TextView time19;
    TextView time20;
    TextView time21;
    TextView time22;
    TextView time23;

    ImageView alphaMon;
    ImageView alphaTue;
    ImageView alphaWed;
    ImageView alphaThu;
    ImageView alphaFri;
    ImageView alphaSat;
    ImageView alphaSun;

    boolean flagMon=false;
    boolean flagTue=false;
    boolean flagWed=false;
    boolean flagThu=false;
    boolean flagFri=false;
    boolean flagSat=false;
    boolean flagSun=false;

    boolean flag06=false;
    boolean flag07=false;
    boolean flag08=false;
    boolean flag09=false;
    boolean flag10=false;
    boolean flag11=false;
    boolean flag12=false;
    boolean flag13=false;
    boolean flag14=false;
    boolean flag15=false;
    boolean flag16=false;
    boolean flag17=false;
    boolean flag18=false;
    boolean flag19=false;
    boolean flag20=false;
    boolean flag21=false;
    boolean flag22=false;
    boolean flag23=false;
    boolean flag06half=false;
    boolean flag07half=false;
    boolean flag08half=false;
    boolean flag09half=false;
    boolean flag10half=false;
    boolean flag11half=false;
    boolean flag12half=false;
    boolean flag13half=false;
    boolean flag14half=false;
    boolean flag15half=false;
    boolean flag16half=false;
    boolean flag17half=false;
    boolean flag18half=false;
    boolean flag19half=false;
    boolean flag20half=false;
    boolean flag21half=false;
    boolean flag22half=false;
    boolean flag23half=false;

    WriteScheduleAdapter adapter;
    ArrayList<LessonDTO> arrayList;
    LessonDTO lessonDTO;
    LessonDTO lessonDTO2;
    FirebaseAuth auth;
    FirebaseFirestore db;

    String uid;
    String startDay;
    int people;
    String startDay1;
    String startDay2;

    ProgressDialog progressDialog;
    SimpleDateFormat makedateFormat;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null) {

            view = inflater.inflate(R.layout.fragment_modifyschedulehalf, container, false);
            mGridView = (GridView) view.findViewById(R.id.scheduleGridView);
            uploadButton = (ImageView) view.findViewById(R.id.uploadButton);
            modifyButton = (ImageView) view.findViewById(R.id.modifyButton);

            findView();

            Bundle bundle = getArguments();
            startDay = bundle.getString("startDay");
            people = bundle.getInt("people");
            try {

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 7);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                startDay1 = format.format(calendar.getTime());
                calendar.add(Calendar.DATE, 7);
                startDay2 = format.format(calendar.getTime());

            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
                SimpleDateFormat month = new SimpleDateFormat("yyyy.MM");
                monthTextView.setText(month.format(date));
                month.format(date);
                ArrayList<Date> dateArrayList = new ArrayList<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                for (int i = 0; i < 7; i++) {
                    dateArrayList.add(calendar.getTime());
                    calendar.add(Calendar.DATE, 1);
                }
                SimpleDateFormat format = new SimpleDateFormat("dd");
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


            LogUtil.d("ssssssssssssssssssssssss" + startDay);

            auth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            uid = auth.getCurrentUser().getUid();

            arrayList = new ArrayList<>();
            lessonDTO = new LessonDTO();
            getSchedule(uid);
            adapter = new WriteScheduleAdapter(getContext(), arrayList, this);
            mGridView.setAdapter(adapter);

            progressDialog = new ProgressDialog(getContext(), R.style.MyTheme);
            progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

            modify();
        }


        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ab.setView(R.layout.dialog_custom_save);
                }
                ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        write();

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

            }
        });

        return view;
    }


    public void write() {
        progressDialog.show();
        final Map<String, LessonDTO> data = new HashMap<>();
        final Map<String, Object> ishalf = new HashMap<>();
        final int comparedate1 = Integer.parseInt(startDay.replace("-", ""));
        ishalf.put("isHalf", true);


        for (int i = 0; i < 252; i++) {
            if (arrayList.get(i).state) {
                lessonDTO = arrayList.get(i);
                data.put(String.valueOf(i), lessonDTO);

            }
        }

        db.collection("trainer").document(uid)
                .collection("schedule").document(startDay).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ishalf.put("compareDate", comparedate1);
                db.collection("trainer").document(uid)
                        .collection("schedule").document(startDay).set(ishalf, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.cancel();
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.main_base_lay, new PagerScheduleFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

            }
        });

        progressDialog.cancel();


    }

    @Override
    public void onDismiss(DialogInterface $dialog) {
        // TODO Auto-generated method stub
        ScheduleSearchDialog dialog = (ScheduleSearchDialog) $dialog;
        usersDTO usersDTO = dialog.getUser();
        for (int j = 0; j < 252; j++) {
            if (arrayList.get(j).check) {
                LessonDTO lessonDTO = new LessonDTO();
                lessonDTO = arrayList.get(j);
                lessonDTO.state = true;
                if (lessonDTO.name != null) {
                    if (lessonDTO.name.size() < people) {
                        lessonDTO.name.add(usersDTO.name);
                        lessonDTO.uid.add(usersDTO.uid);
                    }
                    Toast.makeText(getContext(), "예약인원이 가득 찼습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    lessonDTO.name = new ArrayList<>();
                    lessonDTO.uid = new ArrayList<>();
                    lessonDTO.name.add(usersDTO.name);
                    lessonDTO.uid.add(usersDTO.uid);
                }
                lessonDTO.check = false;
                arrayList.set(j, lessonDTO);
            }
        }
        adapter.notifyDataSetChanged();

    }



    public void textviewclickEventArray0CheckTrue(int i) {
        for (int j = i; j < 252; j += 7) {
            LessonDTO  lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList.get(j);
            lessonDTO2.check = true;
            arrayList.set(j, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void textviewclickEventArray0CheckFalse(int k) {
        for (int j = k; j < 252; j += 7) {
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO = arrayList.get(j);
            lessonDTO.check = false;
            arrayList.set(j, lessonDTO);
        }
        for (int i = 0; i < 252; i++) {
            if (arrayList.get(i).check = false) {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            } else {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void timeTextViewClickEventArray0CheckTrue(int k, int l) {
        for (int i = k; i < l; i++) {
            LessonDTO lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList.get(i);
            lessonDTO2.check = true;
            arrayList.set(i, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void timeTextViewClickEventArray0CheckFalse(int k, int l) {
        for (int i = k; i < l; i++) {
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO = arrayList.get(i);
            lessonDTO.check = false;
            arrayList.set(i, lessonDTO);
        }
        for (int i = 0; i < 252; i++) {
            if (arrayList.get(i).check = false) {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            } else {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void modify() {


        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> items = new ArrayList<>();
                items.add("회원 예약");
                items.add("클래스 열기");
                items.add("클래스 닫기");
                CharSequence[] item = items.toArray(new String[items.size()]);
                android.support.v7.app.AlertDialog.Builder alert_confirm = new android.support.v7.app.AlertDialog.Builder(getContext());
                alert_confirm
                        .setItems(item, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                switch (position) {
                                    case 0:
                                        ScheduleSearchDialog dialog = new ScheduleSearchDialog(getContext());
                                        dialog.setOnDismissListener(ModifyScheduleFragmenthalf.this);
                                        dialog.show();
                                        break;
                                    case 1:
                                        for (int i = 0; i < 252; i++) {
                                            if (arrayList.get(i).check) {
                                                LessonDTO lessonDTO = new LessonDTO();
                                                lessonDTO = arrayList.get(i);
                                                lessonDTO.state = true;
                                                lessonDTO.check = false;
                                                lessonDTO.name = null;
                                                lessonDTO.uid = null;
                                                arrayList.set(i, lessonDTO);

                                            }
                                        }
                                        check_flag_false();
                                        adapter.notifyDataSetChanged();
                                        break;
                                    case 2:
                                        for (int i = 0; i < 252; i++) {
                                            if (arrayList.get(i).check) {
                                                LessonDTO lessonDTO2 = new LessonDTO();
                                                lessonDTO2 = arrayList.get(i);
                                                lessonDTO2.state = false;
                                                lessonDTO2.check = false;
                                                lessonDTO2.name = null;
                                                lessonDTO2.uid = null;
                                                arrayList.set(i, lessonDTO2);

                                            }
                                        }
                                        check_flag_false();
                                        adapter.notifyDataSetChanged();

                                        break;
                                }
                            }
                        });
                android.support.v7.app.AlertDialog alertDialog = alert_confirm.create();
                alertDialog.show();
            }
        });
    }

    public void getSchedule(final String uid)
    {
        makedateFormat = new SimpleDateFormat("yyyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();

        for(int i = 0; i<252; i++)
        {
            LessonDTO lessonDTO = new LessonDTO();
            lessonDTO.check=false;
            lessonDTO.state=false;
            lessonDTO.lesson = i;

            int d=i%7;
            switch(d)
            {
                case 0:
                    lessonDTO.date = startDay;
                    break;
                case 1:
                    try {
                        Date date = makedateFormat.parse(startDay);
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,1);
                        String dateString = makedateFormat.format(calendar.getTime());
                        lessonDTO.date = dateString;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    try {
                        Date date = makedateFormat.parse(startDay);
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,2);
                        String dateString = makedateFormat.format(calendar.getTime());
                        lessonDTO.date = dateString;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 3:
                    try {
                        Date date = makedateFormat.parse(startDay);
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,3);
                        String dateString = makedateFormat.format(calendar.getTime());
                        lessonDTO.date = dateString;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    try {
                        Date date = makedateFormat.parse(startDay);
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,4);
                        String dateString = makedateFormat.format(calendar.getTime());
                        lessonDTO.date = dateString;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 5:
                    try {
                        Date date = makedateFormat.parse(startDay);
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,5);
                        String dateString = makedateFormat.format(calendar.getTime());
                        lessonDTO.date = dateString;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                case 6:
                    try {
                        Date date = makedateFormat.parse(startDay);
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE,6);
                        String dateString = makedateFormat.format(calendar.getTime());
                        lessonDTO.date = dateString;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
                default :
                    break;
            }

            arrayList.add(lessonDTO);
        }

        db.collection("trainer").document(uid)
                .collection("schedule").document(startDay).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists())
                    {
                        Gson gson = new Gson();
                        for (int i=0;i<252;i++)
                        {
                            if(documentSnapshot.get(String.valueOf(i))!=null) {
                                LessonDTO lessonDTO = new LessonDTO();
                                lessonDTO = gson.fromJson(documentSnapshot.get(String.valueOf(i)).toString(), LessonDTO.class);
                                int position = lessonDTO.lesson;
                                arrayList.set(position,lessonDTO);
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

    public void matchDate()
    {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
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

            if (month.format(date).equals(month.format(today))||Integer.parseInt(tempmonthformat.format(date))<Integer.parseInt(tempmonthformat.format(today))) {
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

                dayTimeMon.setText(format.format(dateArrayList.get(0)));
                dayTimeTue.setText(format.format(dateArrayList.get(1)));
                dayTimeWed.setText(format.format(dateArrayList.get(2)));
                dayTimeThu.setText(format.format(dateArrayList.get(3)));
                dayTimeFri.setText(format.format(dateArrayList.get(4)));
                dayTimeSat.setText(format.format(dateArrayList.get(5)));
                dayTimeSun.setText(format.format(dateArrayList.get(6)));
            }

        } catch(ParseException e){
            e.printStackTrace();
        }

    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.textViewMon:
                if (!flagMon) {
                    textviewclickEventArray0CheckTrue(0);
                    flagMon = true;
                } else {
                    textviewclickEventArray0CheckFalse(0);
                    flagMon = false;

                }

                adapter.notifyDataSetChanged();
                break;
            case R.id.textViewTue:

                if (!flagTue) {
                    textviewclickEventArray0CheckTrue(1);
                    flagTue = true;
                } else {
                    textviewclickEventArray0CheckFalse(1);
                    flagTue = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textViewWed:

                if (!flagWed) {
                    textviewclickEventArray0CheckTrue(2);
                    flagWed = true;
                } else {
                    textviewclickEventArray0CheckFalse(2);
                    flagWed = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textViewThu:

                if (!flagThu) {
                    textviewclickEventArray0CheckTrue(3);
                    flagThu = true;
                } else {
                    textviewclickEventArray0CheckFalse(3);
                    flagThu = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textViewFri:

                if (!flagFri) {
                    textviewclickEventArray0CheckTrue(4);
                    flagFri = true;
                } else {
                    textviewclickEventArray0CheckFalse(4);
                    flagFri = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textViewSat:

                if (!flagSat) {
                    textviewclickEventArray0CheckTrue(5);
                    flagSat = true;
                } else {
                    textviewclickEventArray0CheckFalse(5);
                    flagSat = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textViewSun:

                if (!flagSun) {
                    textviewclickEventArray0CheckTrue(6);
                    flagSun = true;
                } else {
                    textviewclickEventArray0CheckFalse(6);
                    flagSun = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.dayTextViewMon:
                if (!flagMon) {
                    textviewclickEventArray0CheckTrue(0);
                    flagMon = true;
                } else {
                    textviewclickEventArray0CheckFalse(0);
                    flagMon = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.dayTextViewTue:
                if (!flagTue) {
                    textviewclickEventArray0CheckTrue(1);
                    flagTue = true;
                } else {
                    textviewclickEventArray0CheckFalse(1);
                    flagTue = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.dayTextViewWed:
                if (!flagWed) {
                    textviewclickEventArray0CheckTrue(2);
                    flagWed = true;
                } else {
                    textviewclickEventArray0CheckFalse(2);
                    flagWed = false;

                }

                adapter.notifyDataSetChanged();
                break;
            case R.id.dayTextViewThu:
                if (!flagThu) {
                    textviewclickEventArray0CheckTrue(3);
                    flagThu = true;
                } else {
                    textviewclickEventArray0CheckFalse(3);
                    flagThu = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.dayTextViewFri:
                if (!flagFri) {
                    textviewclickEventArray0CheckTrue(4);
                    flagFri = true;
                } else {
                    textviewclickEventArray0CheckFalse(4);
                    flagFri = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.dayTextViewSat:
                if (!flagSat) {
                    textviewclickEventArray0CheckTrue(5);
                    flagSat = true;
                } else {
                    textviewclickEventArray0CheckFalse(5);
                    flagSat = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.dayTextViewSun:
                if (!flagSun) {
                    textviewclickEventArray0CheckTrue(6);
                    flagSun = true;
                } else {
                    textviewclickEventArray0CheckFalse(6);
                    flagSun = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView06:

                if (!flag06) {
                    timeTextViewClickEventArray0CheckTrue(0,7);
                    flag06 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(0,7);
                    flag06 = false;

                }

                adapter.notifyDataSetChanged();
                break;
            case R.id.textView06half:

                if (!flag06half) {
                    timeTextViewClickEventArray0CheckTrue(7,14);
                    flag06half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(7,14);
                    flag06half = false;
                }

                adapter.notifyDataSetChanged();
                break;
            case R.id.textView07:
                if (!flag07) {
                    timeTextViewClickEventArray0CheckTrue(14,21);
                    flag07 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(14,21);
                    flag07 = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView07half:
                if (!flag07half) {
                    timeTextViewClickEventArray0CheckTrue(21,28);
                    flag07half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(21,28);
                    flag07half = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView08:
                if (!flag08) {
                    timeTextViewClickEventArray0CheckTrue(28,35);
                    flag08 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(28,35);
                    flag08 = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView08half:
                if (!flag08half) {
                    timeTextViewClickEventArray0CheckTrue(35,42);
                    flag08half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(35,42);
                    flag08half = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView09:
                if (!flag09) {
                    timeTextViewClickEventArray0CheckTrue(42,49);
                    flag09 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(42,49);
                    flag09 = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView09half:
                if (!flag09half) {
                    timeTextViewClickEventArray0CheckTrue(49,56);
                    flag09half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(49,56);
                    flag09half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView10:
                if (!flag10) {
                    timeTextViewClickEventArray0CheckTrue(56,63);
                    flag10 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(56,63);
                    flag10 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView10half:
                if (!flag10half) {
                    timeTextViewClickEventArray0CheckTrue(63,70);
                    flag10half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(63,70);
                    flag10half = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView11:
                if (!flag11) {
                    timeTextViewClickEventArray0CheckTrue(70,77);
                    flag11 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(70,77);
                    flag11 = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView11half:
                if (!flag11half) {
                    timeTextViewClickEventArray0CheckTrue(77,84);
                    flag11half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(77,84);
                    flag11half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView12:
                if (!flag12) {
                    timeTextViewClickEventArray0CheckTrue(84,91);
                    flag12 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(84,91);
                    flag12 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView12half:
                if (!flag12half) {
                    timeTextViewClickEventArray0CheckTrue(91,98);
                    flag12half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(84,91);
                    flag12half = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView13:
                if (!flag13) {
                    timeTextViewClickEventArray0CheckTrue(98,105);
                    flag13 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(98,105);
                    flag13 = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView13half:
                if (!flag13half) {
                    timeTextViewClickEventArray0CheckTrue(105,112);
                    flag13half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(105,112);
                    flag13half = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView14:
                if (!flag14) {
                    timeTextViewClickEventArray0CheckTrue(112,119);
                    flag14 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(112,119);
                    flag14 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView14half:
                if (!flag14half) {
                    timeTextViewClickEventArray0CheckTrue(119,126);
                    flag14half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(119,126);
                    flag14half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView15:
                if (!flag15) {
                    timeTextViewClickEventArray0CheckTrue(126,133);
                    flag15 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(126,133);
                    flag15 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView15half:
                if (!flag15half) {
                    timeTextViewClickEventArray0CheckTrue(133,140);
                    flag15half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(133,140);
                    flag15half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView16:
                if (!flag16) {
                    timeTextViewClickEventArray0CheckTrue(140,147);
                    flag16 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(140,147);
                    flag16 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView16half:
                if (!flag16half) {
                    timeTextViewClickEventArray0CheckTrue(147,154);
                    flag16half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(147,154);
                    flag16half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView17:
                if (!flag17) {
                    timeTextViewClickEventArray0CheckTrue(154,161);
                    flag17 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(154,161);
                    flag17 = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView17half:
                if (!flag17half) {
                    timeTextViewClickEventArray0CheckTrue(161,168);
                    flag17half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(161,168);
                    flag17half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView18:
                if (!flag18) {
                    timeTextViewClickEventArray0CheckTrue(168,175);
                    flag18 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(168,175);
                    flag18 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView18half:
                if (!flag18half) {
                    timeTextViewClickEventArray0CheckTrue(175,182);
                    flag18half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(175,182);
                    flag18half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView19:
                if (!flag19) {
                    timeTextViewClickEventArray0CheckTrue(182,189);
                    flag19 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(182,189);
                    flag19 = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView19half:
                if (!flag19half) {
                    timeTextViewClickEventArray0CheckTrue(189,196);
                    flag19half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(189,196);
                    flag19half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView20:
                if (!flag20) {
                    timeTextViewClickEventArray0CheckTrue(196,203);
                    flag20 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(196,203);
                    flag20 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView20half:
                if (!flag20half) {
                    timeTextViewClickEventArray0CheckTrue(203,210);
                    flag20half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(203,210);
                    flag20half = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView21:
                if (!flag21) {
                    timeTextViewClickEventArray0CheckTrue(210,217);
                    flag21 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(210,217);
                    flag21 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView21half:
                if (!flag21half) {
                    timeTextViewClickEventArray0CheckTrue(217,224);
                    flag21half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(217,224);
                    flag21half = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView22:
                if (!flag22) {
                    timeTextViewClickEventArray0CheckTrue(224,231);
                    flag22 = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(224,231);
                    flag22 = false;
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView22half:
                if (!flag22half) {
                    timeTextViewClickEventArray0CheckTrue(231,238);
                    flag22half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(231,238);
                    flag22half = false;

                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView23:
                if (!flag23) {
                    timeTextViewClickEventArray0CheckTrue(238,245);
                    flag23 = true;
                } else {
                    flag23 = false;
                    timeTextViewClickEventArray0CheckFalse(238,245);
                }
                adapter.notifyDataSetChanged();
                break;
            case R.id.textView23half:
                if (!flag23half) {
                    timeTextViewClickEventArray0CheckTrue(245,252);
                    flag23half = true;
                } else {
                    timeTextViewClickEventArray0CheckFalse(245,252);
                    flag23half = false;

                }
                adapter.notifyDataSetChanged();
                break;

        }
    }

    public void findView() {

        timeMon = (TextView)view.findViewById(R.id.textViewMon);
        timeTue = (TextView)view.findViewById(R.id.textViewTue);
        timeWed = (TextView)view.findViewById(R.id.textViewWed);
        timeThu = (TextView)view.findViewById(R.id.textViewThu);
        timeFri = (TextView)view.findViewById(R.id.textViewFri);
        timeSat = (TextView)view.findViewById(R.id.textViewSat);
        timeSun = (TextView)view.findViewById(R.id.textViewSun);

        dayTimeMon = (TextView)view.findViewById(R.id.dayTextViewMon);
        dayTimeTue = (TextView)view.findViewById(R.id.dayTextViewTue);
        dayTimeWed = (TextView)view.findViewById(R.id.dayTextViewWed);
        dayTimeThu = (TextView)view.findViewById(R.id.dayTextViewThu);
        dayTimeFri = (TextView)view.findViewById(R.id.dayTextViewFri);
        dayTimeSat = (TextView)view.findViewById(R.id.dayTextViewSat);
        dayTimeSun = (TextView)view.findViewById(R.id.dayTextViewSun);

        monthTextView = (TextView)view.findViewById(R.id.monthTextView);


        time06 = (TextView)view.findViewById(R.id.textView06);
        time07 = (TextView)view.findViewById(R.id.textView07);
        time08 = (TextView)view.findViewById(R.id.textView08);
        time09 = (TextView)view.findViewById(R.id.textView09);
        time10 = (TextView)view.findViewById(R.id.textView10);
        time11 = (TextView)view.findViewById(R.id.textView11);
        time12 = (TextView)view.findViewById(R.id.textView12);
        time13 = (TextView)view.findViewById(R.id.textView13);
        time14 = (TextView)view.findViewById(R.id.textView14);
        time15 = (TextView)view.findViewById(R.id.textView15);
        time16 = (TextView)view.findViewById(R.id.textView16);
        time17 = (TextView)view.findViewById(R.id.textView17);
        time18 = (TextView)view.findViewById(R.id.textView18);
        time19 = (TextView)view.findViewById(R.id.textView19);
        time20 = (TextView)view.findViewById(R.id.textView20);
        time21 = (TextView)view.findViewById(R.id.textView21);
        time22 = (TextView)view.findViewById(R.id.textView22);
        time23 = (TextView)view.findViewById(R.id.textView23);

        time06half = (TextView)view.findViewById(R.id.textView06half);
        time07half = (TextView)view.findViewById(R.id.textView07half);
        time08half = (TextView)view.findViewById(R.id.textView08half);
        time09half = (TextView)view.findViewById(R.id.textView09half);
        time10half = (TextView)view.findViewById(R.id.textView10half);
        time11half = (TextView)view.findViewById(R.id.textView11half);
        time12half = (TextView)view.findViewById(R.id.textView12half);
        time13half = (TextView)view.findViewById(R.id.textView13half);
        time14half = (TextView)view.findViewById(R.id.textView14half);
        time15half = (TextView)view.findViewById(R.id.textView15half);
        time16half = (TextView)view.findViewById(R.id.textView16half);
        time17half = (TextView)view.findViewById(R.id.textView17half);
        time18half = (TextView)view.findViewById(R.id.textView18half);
        time19half = (TextView)view.findViewById(R.id.textView19half);
        time20half = (TextView)view.findViewById(R.id.textView20half);
        time21half = (TextView)view.findViewById(R.id.textView21half);
        time22half = (TextView)view.findViewById(R.id.textView22half);
        time23half = (TextView)view.findViewById(R.id.textView23half);

        alphaMon = (ImageView) view.findViewById(R.id.alphaMon);
        alphaTue = (ImageView) view.findViewById(R.id.alphaTue);
        alphaWed = (ImageView) view.findViewById(R.id.alphaWed);
        alphaThu = (ImageView) view.findViewById(R.id.alphaThu);
        alphaFri = (ImageView) view.findViewById(R.id.alphaFri);
        alphaSat = (ImageView) view.findViewById(R.id.alphaSat);
        alphaSun = (ImageView) view.findViewById(R.id.alphaSun);

        timeMon.setOnClickListener(this);
        timeTue.setOnClickListener(this);
        timeWed.setOnClickListener(this);
        timeThu.setOnClickListener(this);
        timeFri.setOnClickListener(this);
        timeSat.setOnClickListener(this);
        timeSun.setOnClickListener(this);
        dayTimeMon.setOnClickListener(this);
        dayTimeTue.setOnClickListener(this);
        dayTimeWed.setOnClickListener(this);
        dayTimeThu.setOnClickListener(this);
        dayTimeFri.setOnClickListener(this);
        dayTimeSat.setOnClickListener(this);
        dayTimeSun.setOnClickListener(this);
        time06half.setOnClickListener(this);
        time07half.setOnClickListener(this);
        time08half.setOnClickListener(this);
        time09half.setOnClickListener(this);
        time10half.setOnClickListener(this);
        time11half.setOnClickListener(this);
        time12half.setOnClickListener(this);
        time13half.setOnClickListener(this);
        time14half.setOnClickListener(this);
        time15half.setOnClickListener(this);
        time16half.setOnClickListener(this);
        time17half.setOnClickListener(this);
        time18half.setOnClickListener(this);
        time19half.setOnClickListener(this);
        time20half.setOnClickListener(this);
        time21half.setOnClickListener(this);
        time22half.setOnClickListener(this);
        time23half.setOnClickListener(this);
        time06.setOnClickListener(this);
        time07.setOnClickListener(this);
        time08.setOnClickListener(this);
        time09.setOnClickListener(this);
        time10.setOnClickListener(this);
        time11.setOnClickListener(this);
        time12.setOnClickListener(this);
        time13.setOnClickListener(this);
        time14.setOnClickListener(this);
        time15.setOnClickListener(this);
        time16.setOnClickListener(this);
        time17.setOnClickListener(this);
        time18.setOnClickListener(this);
        time19.setOnClickListener(this);
        time20.setOnClickListener(this);
        time21.setOnClickListener(this);
        time22.setOnClickListener(this);
        time23.setOnClickListener(this);

        setColorInPartitial(time06);
        setColorInPartitial(time06half);
        setColorInPartitial(time07);
        setColorInPartitial(time07half);
        setColorInPartitial(time08);
        setColorInPartitial(time08half);
        setColorInPartitial(time09);
        setColorInPartitial(time09half);
        setColorInPartitial(time10);
        setColorInPartitial(time10half);
        setColorInPartitial(time11);
        setColorInPartitial(time11half);
        setColorInPartitial(time12);
        setColorInPartitial(time12half);
        setColorInPartitial(time13);
        setColorInPartitial(time13half);
        setColorInPartitial(time14);
        setColorInPartitial(time14half);
        setColorInPartitial(time15);
        setColorInPartitial(time15half);
        setColorInPartitial(time16);
        setColorInPartitial(time16half);
        setColorInPartitial(time17);
        setColorInPartitial(time17half);
        setColorInPartitial(time18);
        setColorInPartitial(time18half);
        setColorInPartitial(time19);
        setColorInPartitial(time19half);
        setColorInPartitial(time20);
        setColorInPartitial(time20half);
        setColorInPartitial(time21);
        setColorInPartitial(time21half);
        setColorInPartitial(time22);
        setColorInPartitial(time22half);
        setColorInPartitial(time23);
        setColorInPartitial(time23half);
    }

    public void check_flag_false()
    {
        flagMon=false;
        flagTue=false;
        flagWed=false;
        flagThu=false;
        flagFri=false;
        flagSat=false;
        flagSun=false;

        flag06=false;
        flag07=false;
        flag08=false;
        flag09=false;
        flag10=false;
        flag11=false;
        flag12=false;
        flag13=false;
        flag14=false;
        flag15=false;
        flag16=false;
        flag17=false;
        flag18=false;
        flag19=false;
        flag20=false;
        flag21=false;
        flag22=false;
        flag23=false;

        flag06half=false;
        flag07half=false;
        flag08half=false;
        flag09half=false;
        flag10half=false;
        flag11half=false;
        flag12half=false;
        flag13half=false;
        flag14half=false;
        flag15half=false;
        flag16half=false;
        flag17half=false;
        flag18half=false;
        flag19half=false;
        flag20half=false;
        flag21half=false;
        flag22half=false;
        flag23half=false;
    }

    private void setColorInPartitial(TextView textView) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        LogUtil.d("aaaaaaaaaaaaaaaaaaaaaa"+textView.getText().toString());
        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#262a4f")), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(builder);
    }

}
