package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

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
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;
import petrov.kristiyan.colorpicker.CustomDialog;

/**
 * Created by system777 on 2018-01-16.
 */

public class WriteScheduleFragment extends Fragment implements View.OnClickListener,DialogInterface.OnDismissListener {

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

    WriteScheduleAdapter adapter;
    public static ArrayList<LessonDTO> arrayList;
    public static ArrayList<LessonDTO> arrayList1;
    public static ArrayList<LessonDTO> arrayList2;
    LessonDTO lessonDTO;
    LessonDTO lessonDTO2;
    FirebaseFirestore db;

    String uid;
    String startDay;
    int people;
    int week;
    String startDay1;
    String startDay2;

    SimpleDateFormat makedateFormat;

    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view == null)
        {

            view = inflater.inflate(R.layout.fragment_writeschedule,container,false);
            mGridView = (GridView)view.findViewById(R.id.scheduleGridView);
            uploadButton  = (ImageView)view.findViewById(R.id.uploadButton);
            modifyButton = (ImageView)view.findViewById(R.id.modifyButton);

            findView();

            Bundle bundle = getArguments();
            startDay = bundle.getString("startDay");
            people = bundle.getInt("people");
            week = bundle.getInt("week");

            try {

                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                calendar.add(Calendar.DATE, 7);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

                startDay1 = format.format(calendar.getTime());
                calendar.add(Calendar.DATE,7);
                startDay2 = format.format(calendar.getTime());

                LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+startDay+startDay1+startDay2+calendar.getTime().getDay());

            } catch (ParseException e) {
                e.printStackTrace();
            }



            if(week==0)
            {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDay);
                    SimpleDateFormat month = new SimpleDateFormat("yyyy.MM");
                    monthTextView.setText(month.format(date));
                    month.format(date);
                    ArrayList<Date> dateArrayList = new ArrayList<>();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    for(int i=0;i<7;i++)
                    {
                        dateArrayList.add(calendar.getTime());
                        calendar.add(Calendar.DATE,1);
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
            }
            else if(week==1)
            {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDay1);
                    SimpleDateFormat month = new SimpleDateFormat("yyyy.MM");
                    monthTextView.setText(month.format(date));
                    ArrayList<Date> dateArrayList = new ArrayList<>();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    for(int i=0;i<7;i++)
                    {
                        dateArrayList.add(calendar.getTime());
                        calendar.add(Calendar.DATE,1);
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

            }
            else if(week==2)
            {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(startDay2);
                    SimpleDateFormat month = new SimpleDateFormat("yyyy.MM");
                    monthTextView.setText(month.format(date));
                    ArrayList<Date> dateArrayList = new ArrayList<>();
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(date);
                    for(int i=0;i<7;i++)
                    {
                        dateArrayList.add(calendar.getTime());
                        calendar.add(Calendar.DATE,1);
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
            }


            LogUtil.d("ssssssssssssssssssssssss"+startDay);

            db = FirebaseFirestore.getInstance();
            uid = ((Ju_healthApp)getContext().getApplicationContext()).getUserUid();

            makedateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();

            if(week==0) {
                arrayList = new ArrayList<>();
                for (int i = 0; i < 126; i++) {

                    lessonDTO = new LessonDTO();
                    lessonDTO.check = false;
                    lessonDTO.state = true;
                    lessonDTO.user_count = people;
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
                adapter = new WriteScheduleAdapter(getContext(), arrayList,this);
                mGridView.setAdapter(adapter);
            }
            else if(week==1) {
                arrayList1 = new ArrayList<>();
                for (int i = 0; i < 126; i++) {

                    lessonDTO = new LessonDTO();
                    lessonDTO.check = false;
                    lessonDTO.state = true;
                    lessonDTO.user_count = people;
                    lessonDTO.lesson = i;
                    int d=i%7;
                    switch(d)
                    {
                        case 0:
                            lessonDTO.date = startDay1;
                            break;
                        case 1:
                            try {
                                Date date = makedateFormat.parse(startDay1);
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
                                Date date = makedateFormat.parse(startDay1);
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
                                Date date = makedateFormat.parse(startDay1);
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
                                Date date = makedateFormat.parse(startDay1);
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
                                Date date = makedateFormat.parse(startDay1);
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
                                Date date = makedateFormat.parse(startDay1);
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

                    arrayList1.add(lessonDTO);
                }
                adapter = new WriteScheduleAdapter(getContext(), arrayList1,this);
                mGridView.setAdapter(adapter);
            }
            else if(week==2) {
                arrayList2 = new ArrayList<>();
                for (int i = 0; i < 126; i++) {

                    lessonDTO = new LessonDTO();
                    lessonDTO.check = false;
                    lessonDTO.state = true;
                    lessonDTO.user_count = people;
                    lessonDTO.lesson = i;
                    int d=i%7;
                    switch(d)
                    {
                        case 0:
                            lessonDTO.date = startDay2;
                            break;
                        case 1:
                            try {
                                Date date = makedateFormat.parse(startDay2);
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
                                Date date = makedateFormat.parse(startDay2);
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
                                Date date = makedateFormat.parse(startDay2);
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
                                Date date = makedateFormat.parse(startDay2);
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
                                Date date = makedateFormat.parse(startDay2);
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
                                Date date = makedateFormat.parse(startDay2);
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

                    arrayList2.add(lessonDTO);
                }
                adapter = new WriteScheduleAdapter(getContext(), arrayList2,this);
                mGridView.setAdapter(adapter);
            }


            progressDialog = new ProgressDialog(getContext(),R.style.MyTheme);
            progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);

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
                Button posButton = (Button)dialog.findViewById(android.R.id.button1);
                posButton.setTextColor(Color.parseColor("#ee2b47"));
                Button negButton = (Button)dialog.findViewById(android.R.id.button2);
                negButton.setTextColor(Color.parseColor("#333333"));

       }});

        modify();


        return view;
    }


    public void write() {
        progressDialog.show();
        final Map<String, LessonDTO> data = new HashMap<>();
        final Map<String, Object> ishalf = new HashMap<>();
        final int comparedate1 = Integer.parseInt(startDay.replace("-",""));
        final int comparedate2 = Integer.parseInt(startDay1.replace("-",""));
        final int comparedate3 = Integer.parseInt(startDay2.replace("-",""));
        ishalf.put("isHalf",false);


        for(int i =0; i<126; i++)
        {
            if(arrayList.get(i).state) {
                lessonDTO = arrayList.get(i);
                data.put(String.valueOf(i),lessonDTO);

            }
        }

        db.collection("trainer").document(uid)
                .collection("schedule").document(startDay).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                ishalf.put("compareDate",comparedate1);
                db.collection("trainer").document(uid)
                        .collection("schedule").document(startDay).set(ishalf, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        data.clear();
                        progressDialog.cancel();
                        if(arrayList1!=null) {
                            progressDialog.show();
                            for(int i =0; i<126; i++) {
                                if(arrayList1.get(i).state) {
                                    lessonDTO = arrayList1.get(i);
                                    data.put(String.valueOf(i),lessonDTO);
                                }
                            }

                            db.collection("trainer").document(uid)
                                    .collection("schedule").document(startDay1).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    ishalf.clear();
                                    ishalf.put("isHalf",false);
                                    ishalf.put("compareDate",comparedate2);
                                    db.collection("trainer").document(uid)
                                            .collection("schedule").document(startDay1).set(ishalf, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            data.clear();
                                            progressDialog.cancel();
                                            if (arrayList2!=null) {
                                                progressDialog.show();
                                                for (int i = 0; i < 126; i++) {
                                                    if (arrayList2.get(i).state) {
                                                        lessonDTO = arrayList2.get(i);
                                                        data.put(String.valueOf(i), lessonDTO);
                                                    }
                                                }

                                                db.collection("trainer").document(uid)
                                                        .collection("schedule").document(startDay2).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.cancel();
                                                        ishalf.clear();
                                                        ishalf.put("isHalf",false);
                                                        ishalf.put("compareDate",comparedate3);
                                                        db.collection("trainer").document(uid)
                                                                .collection("schedule").document(startDay2).set(ishalf, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                progressDialog.cancel();
                                                                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                                fragmentTransaction.replace(R.id.main_base_lay,new PagerScheduleFragment());
                                                                fragmentTransaction.addToBackStack(null);
                                                                fragmentTransaction.commit();
                                                            }
                                                        });
                                                    }
                                                });
                                            }else{
                                                progressDialog.cancel();
                                                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                                fragmentTransaction.replace(R.id.main_base_lay,new PagerScheduleFragment());
                                                fragmentTransaction.addToBackStack(null);
                                                fragmentTransaction.commit();
                                            }
                                        }
                                    });
                                }
                            });
                        }else{
                            progressDialog.cancel();
                            FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay,new PagerScheduleFragment());
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    }
                });
            }
        });

        progressDialog.cancel();


    }

    @Override
    public void onDismiss(DialogInterface $dialog) {
        // TODO Auto-generated method stub
        ScheduleSearchDialog dialog = (ScheduleSearchDialog) $dialog ;
        usersDTO usersDTO = dialog.getUser();
        if(week==0) {
            for (int j = 0; j < 126; j++) {
                if (arrayList.get(j).check) {
                    LessonDTO lessonDTO = new LessonDTO();
                    lessonDTO = arrayList.get(j);
                    lessonDTO.state = true;
                    if (lessonDTO.name != null) {
                        if (lessonDTO.name.size() < people) {
                            lessonDTO.name.add(usersDTO.name);
                            lessonDTO.uid.add(usersDTO.uid);
                        }
                        Toast.makeText(getContext(),"예약인원이 가득 찼습니다.",Toast.LENGTH_SHORT).show();
                    } else {
                        lessonDTO.name = new ArrayList<>();
                        lessonDTO.uid = new ArrayList<>();
                        lessonDTO.name.add(usersDTO.name);
                        lessonDTO.uid.add(usersDTO.uid);
                    }
                    lessonDTO.user_count = people;
                    lessonDTO.check = false;
                    arrayList.set(j, lessonDTO);
                }
            }
            adapter.notifyDataSetChanged();
        }
        else if(week==1)
        {
            for (int j = 0; j < 126; j++) {
                if (arrayList1.get(j).check) {
                    LessonDTO lessonDTO = new LessonDTO();
                    lessonDTO = arrayList1.get(j);
                    lessonDTO.state = true;
                    if (lessonDTO.name != null) {
                        if (lessonDTO.name.size() < people) {
                            lessonDTO.name.add(usersDTO.name);
                            lessonDTO.uid.add(usersDTO.uid);
                        }
                        else
                        {
                            Toast.makeText(getContext(),"예약인원이 가득 찼습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        lessonDTO.name = new ArrayList<>();
                        lessonDTO.uid = new ArrayList<>();
                        lessonDTO.name.add(usersDTO.name);
                        lessonDTO.uid.add(usersDTO.uid);
                    }
                    lessonDTO.user_count = people;
                    lessonDTO.check = false;
                    arrayList1.set(j, lessonDTO);
                }
            }
            adapter.notifyDataSetChanged();
        }
        else if(week==2)
        {
            for (int j = 0; j < 126; j++) {
                if (arrayList2.get(j).check) {
                    LessonDTO lessonDTO = new LessonDTO();
                    lessonDTO = arrayList2.get(j);
                    lessonDTO.state = true;
                    if (lessonDTO.name != null) {
                        if (lessonDTO.name.size() < people) {
                            lessonDTO.name.add(usersDTO.name);
                            lessonDTO.uid.add(usersDTO.uid);
                        }
                        else
                            {
                            Toast.makeText(getContext(),"예약인원이 가득 찼습니다.",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        lessonDTO.name = new ArrayList<>();
                        lessonDTO.uid = new ArrayList<>();
                        lessonDTO.name.add(usersDTO.name);
                        lessonDTO.uid.add(usersDTO.uid);
                    }
                    lessonDTO.user_count = people;
                    lessonDTO.check = false;
                    arrayList2.set(j, lessonDTO);
                }
            }
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
       
        if(week==0) {
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
                case R.id.textView07:
                    if (!flag07) {
                        timeTextViewClickEventArray0CheckTrue(7,14);
                        flag07 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(7,14);
                        flag07 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView08:
                    if (!flag08) {
                        timeTextViewClickEventArray0CheckTrue(14,21);
                        flag08 = true;
                        
                    } else {
                        timeTextViewClickEventArray0CheckFalse(14,21);
                        flag08 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView09:
                    if (!flag09) {
                        timeTextViewClickEventArray0CheckTrue(21,28);
                        flag09 = true;
                     
                    } else {
                        timeTextViewClickEventArray0CheckFalse(21,28);
                        flag09 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView10:
                    if (!flag10) {
                        timeTextViewClickEventArray0CheckTrue(28,35);
                        flag10 = true;
                      
                    } else {
                        timeTextViewClickEventArray0CheckFalse(28,35);
                        flag10 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView11:
                    if (!flag11) {
                        timeTextViewClickEventArray0CheckTrue(35,42);
                        flag11 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(35,42);
                        flag11 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView12:
                    if (!flag12) {
                        timeTextViewClickEventArray0CheckTrue(42,49);
                        flag12 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(42,49);
                        flag12 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView13:
                    if (!flag13) {
                        timeTextViewClickEventArray0CheckTrue(49,56);
                        flag13 = true;
                     
                    } else {
                        timeTextViewClickEventArray0CheckFalse(49,56);
                        flag13 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView14:
                    if (!flag14) {
                        timeTextViewClickEventArray0CheckTrue(56,63);
                        flag14 = true;
                     
                    } else {
                        timeTextViewClickEventArray0CheckFalse(56,63);
                        flag14 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView15:
                    if (!flag15) {
                        timeTextViewClickEventArray0CheckTrue(63,70);
                        flag15 = true;
                       
                    } else {
                        timeTextViewClickEventArray0CheckFalse(63,70);
                        flag15 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView16:
                    if (!flag16) {
                        timeTextViewClickEventArray0CheckTrue(70,77);
                        flag16 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(70,77);
                        flag16 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView17:
                    if (!flag17) {
                        timeTextViewClickEventArray0CheckTrue(77,84);
                        flag17 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(77,84);
                        flag17 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView18:
                    if (!flag18) {
                        timeTextViewClickEventArray0CheckTrue(84,91);
                        flag18 = true;
                       
                    } else {
                        timeTextViewClickEventArray0CheckFalse(84,91);
                        flag18 = false;
                        
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView19:
                    if (!flag19) {
                        timeTextViewClickEventArray0CheckTrue(91,98);
                        flag19 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(91,98);
                        flag19 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView20:
                    if (!flag20) {
                        timeTextViewClickEventArray0CheckTrue(98,105);
                        flag20 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(98,105);
                        flag20 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView21:
                    if (!flag21) {
                        timeTextViewClickEventArray0CheckTrue(105,112);
                        flag21 = true;
                       
                    } else {
                        timeTextViewClickEventArray0CheckFalse(105,112);
                        flag21 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView22:
                    if (!flag22) {
                        timeTextViewClickEventArray0CheckTrue(112,119);
                        flag22 = true;
                    } else {
                        timeTextViewClickEventArray0CheckFalse(112,119);
                        flag22 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView23:
                    if (!flag23) {
                        timeTextViewClickEventArray0CheckTrue(119,126);
                        flag23 = true;
                        
                    } else {
                        timeTextViewClickEventArray0CheckFalse(119,126);
                        flag23 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }if(week==1) {
            switch (view.getId()) {
                case R.id.textViewMon:
                    if (!flagMon) {
                        textviewclickEventArray1CheckTrue(0);
                        flagMon = true;
                    } else {
                        textviewclickEventArray1CheckFalse(0);
                        flagMon = false;

                    }

                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewTue:

                    if (!flagTue) {
                        textviewclickEventArray1CheckTrue(1);
                        flagTue = true;
                    } else {
                        textviewclickEventArray1CheckFalse(1);
                        flagTue = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewWed:
                    if (!flagWed) {
                        textviewclickEventArray1CheckTrue(2);
                        flagWed = true;

                    } else {
                        textviewclickEventArray1CheckFalse(2);
                        flagWed = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewThu:

                    if (!flagThu) {
                        textviewclickEventArray1CheckTrue(3);
                        flagThu = true;
                    } else {
                        textviewclickEventArray1CheckFalse(3);
                        flagThu = false;

                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewFri:
                    if (!flagFri) {
                        textviewclickEventArray1CheckTrue(4);
                        flagFri = true;
                    } else {
                        textviewclickEventArray1CheckFalse(4);
                        flagFri = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewSat:

                    if (!flagSat) {
                        textviewclickEventArray1CheckTrue(5);
                        flagSat = true;
                    } else {
                        textviewclickEventArray1CheckFalse(5);
                        flagSat = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewSun:

                    if (!flagSun) {
                        textviewclickEventArray1CheckTrue(6);
                        flagSun = true;
                    } else {
                        textviewclickEventArray1CheckFalse(6);
                        flagSun = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewMon:
                    if (!flagMon) {
                        textviewclickEventArray1CheckTrue(0);
                        flagMon = true;
                    } else {
                        textviewclickEventArray1CheckFalse(0);
                        flagMon = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewTue:
                    if (!flagTue) {
                        textviewclickEventArray1CheckTrue(1);
                        flagTue = true;
                    } else {
                        textviewclickEventArray1CheckFalse(1);
                        flagTue = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewWed:
                    if (!flagWed) {
                        textviewclickEventArray1CheckTrue(2);
                        flagWed = true;

                    } else {
                        textviewclickEventArray1CheckFalse(2);
                        flagWed = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewThu:

                    if (!flagThu) {
                        textviewclickEventArray1CheckTrue(3);
                        flagThu = true;
                    } else {
                        textviewclickEventArray1CheckFalse(3);
                        flagThu = false;

                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewFri:
                    if (!flagFri) {
                        textviewclickEventArray1CheckTrue(4);
                        flagFri = true;
                    } else {
                        textviewclickEventArray1CheckFalse(4);
                        flagFri = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewSat:
                    if (!flagSat) {
                        textviewclickEventArray1CheckTrue(5);
                        flagSat = true;
                    } else {
                        textviewclickEventArray1CheckFalse(5);
                        flagSat = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewSun:
                    if (!flagSun) {
                        textviewclickEventArray1CheckTrue(6);
                        flagSun = true;
                    } else {
                        textviewclickEventArray1CheckFalse(6);
                        flagSun = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView06:

                    if (!flag06) {
                        timeTextViewClickEventArray1CheckTrue(0,7);
                        flag06 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(0,7);
                        flag06 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView07:
                    if (!flag07) {
                        timeTextViewClickEventArray1CheckTrue(7,14);
                        flag07 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(7,14);
                        flag07 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView08:
                    if (!flag08) {
                        timeTextViewClickEventArray1CheckTrue(14,21);
                        flag08 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(14,21);
                        flag08 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView09:
                    if (!flag09) {
                        timeTextViewClickEventArray1CheckTrue(21,28);
                        flag09 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(21,28);
                        flag09 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView10:
                    if (!flag10) {
                        timeTextViewClickEventArray1CheckTrue(28,35);
                        flag10 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(28,35);
                        flag10 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView11:
                    if (!flag11) {
                        timeTextViewClickEventArray1CheckTrue(35,42);
                        flag11 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(35,42);
                        flag11 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView12:
                    if (!flag12) {
                        timeTextViewClickEventArray1CheckTrue(42,49);
                        flag12 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(42,49);
                        flag12 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView13:
                    if (!flag13) {
                        timeTextViewClickEventArray1CheckTrue(49,56);
                        flag13 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(49,56);
                        flag13 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView14:
                    if (!flag14) {
                        timeTextViewClickEventArray1CheckTrue(56,63);
                        flag14 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(56,63);
                        flag14 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView15:
                    if (!flag15) {
                        timeTextViewClickEventArray1CheckTrue(63,70);
                        flag15 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(63,70);
                        flag15 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView16:
                    if (!flag16) {
                        timeTextViewClickEventArray1CheckTrue(70,77);
                        flag16 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(70,77);
                        flag16 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView17:
                    if (!flag17) {
                        timeTextViewClickEventArray1CheckTrue(77,84);
                        flag17 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(77,84);
                        flag17 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView18:
                    if (!flag18) {
                        timeTextViewClickEventArray1CheckTrue(84,91);
                        flag18 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(84,91);
                        flag18 = false;

                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView19:
                    if (!flag19) {
                        timeTextViewClickEventArray1CheckTrue(91,98);
                        flag19 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(91,98);
                        flag19 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView20:
                    if (!flag20) {
                        timeTextViewClickEventArray1CheckTrue(98,105);
                        flag20 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(98,105);
                        flag20 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView21:
                    if (!flag21) {
                        timeTextViewClickEventArray1CheckTrue(105,112);
                        flag21 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(105,112);
                        flag21 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView22:
                    if (!flag22) {
                        timeTextViewClickEventArray1CheckTrue(112,119);
                        flag22 = true;
                    } else {
                        timeTextViewClickEventArray1CheckFalse(112,119);
                        flag22 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView23:
                    if (!flag23) {
                        timeTextViewClickEventArray1CheckTrue(119,126);
                        flag23 = true;

                    } else {
                        timeTextViewClickEventArray1CheckFalse(119,126);
                        flag23 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
        if(week==2) {
            switch (view.getId()) {
                case R.id.textViewMon:
                    if (!flagMon) {
                        textviewclickEventArray2CheckTrue(0);
                        flagMon = true;
                    } else {
                        textviewclickEventArray2CheckFalse(0);
                        flagMon = false;

                    }

                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewTue:

                    if (!flagTue) {
                        textviewclickEventArray2CheckTrue(1);
                        flagTue = true;
                    } else {
                        textviewclickEventArray2CheckFalse(1);
                        flagTue = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewWed:
                    if (!flagWed) {
                        textviewclickEventArray2CheckTrue(2);
                        flagWed = true;

                    } else {
                        textviewclickEventArray2CheckFalse(2);
                        flagWed = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewThu:

                    if (!flagThu) {
                        textviewclickEventArray2CheckTrue(3);
                        flagThu = true;
                    } else {
                        textviewclickEventArray2CheckFalse(3);
                        flagThu = false;

                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewFri:
                    if (!flagFri) {
                        textviewclickEventArray2CheckTrue(4);
                        flagFri = true;
                    } else {
                        textviewclickEventArray2CheckFalse(4);
                        flagFri = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewSat:

                    if (!flagSat) {
                        textviewclickEventArray2CheckTrue(5);
                        flagSat = true;
                    } else {
                        textviewclickEventArray2CheckFalse(5);
                        flagSat = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textViewSun:

                    if (!flagSun) {
                        textviewclickEventArray2CheckTrue(6);
                        flagSun = true;
                    } else {
                        textviewclickEventArray2CheckFalse(6);
                        flagSun = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewMon:
                    if (!flagMon) {
                        textviewclickEventArray2CheckTrue(0);
                        flagMon = true;
                    } else {
                        textviewclickEventArray2CheckFalse(0);
                        flagMon = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewTue:
                    if (!flagTue) {
                        textviewclickEventArray2CheckTrue(1);
                        flagTue = true;
                    } else {
                        textviewclickEventArray2CheckFalse(1);
                        flagTue = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewWed:
                    if (!flagWed) {
                        textviewclickEventArray2CheckTrue(2);
                        flagWed = true;

                    } else {
                        textviewclickEventArray2CheckFalse(2);
                        flagWed = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewThu:

                    if (!flagThu) {
                        textviewclickEventArray2CheckTrue(3);
                        flagThu = true;
                    } else {
                        textviewclickEventArray2CheckFalse(3);
                        flagThu = false;

                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewFri:
                    if (!flagFri) {
                        textviewclickEventArray2CheckTrue(4);
                        flagFri = true;
                    } else {
                        textviewclickEventArray2CheckFalse(4);
                        flagFri = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewSat:
                    if (!flagSat) {
                        textviewclickEventArray2CheckTrue(5);
                        flagSat = true;
                    } else {
                        textviewclickEventArray2CheckFalse(5);
                        flagSat = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.dayTextViewSun:
                    if (!flagSun) {
                        textviewclickEventArray2CheckTrue(6);
                        flagSun = true;
                    } else {
                        textviewclickEventArray2CheckFalse(6);
                        flagSun = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView06:

                    if (!flag06) {
                        timeTextViewClickEventArray2CheckTrue(0,7);
                        flag06 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(0,7);
                        flag06 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView07:
                    if (!flag07) {
                        timeTextViewClickEventArray2CheckTrue(7,14);
                        flag07 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(7,14);
                        flag07 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView08:
                    if (!flag08) {
                        timeTextViewClickEventArray2CheckTrue(14,21);
                        flag08 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(14,21);
                        flag08 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView09:
                    if (!flag09) {
                        timeTextViewClickEventArray2CheckTrue(21,28);
                        flag09 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(21,28);
                        flag09 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView10:
                    if (!flag10) {
                        timeTextViewClickEventArray2CheckTrue(28,35);
                        flag10 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(28,35);
                        flag10 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView11:
                    if (!flag11) {
                        timeTextViewClickEventArray2CheckTrue(35,42);
                        flag11 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(35,42);
                        flag11 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView12:
                    if (!flag12) {
                        timeTextViewClickEventArray2CheckTrue(42,49);
                        flag12 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(42,49);
                        flag12 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView13:
                    if (!flag13) {
                        timeTextViewClickEventArray2CheckTrue(49,56);
                        flag13 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(49,56);
                        flag13 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView14:
                    if (!flag14) {
                        timeTextViewClickEventArray2CheckTrue(56,63);
                        flag14 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(56,63);
                        flag14 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView15:
                    if (!flag15) {
                        timeTextViewClickEventArray2CheckTrue(63,70);
                        flag15 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(63,70);
                        flag15 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView16:
                    if (!flag16) {
                        timeTextViewClickEventArray2CheckTrue(70,77);
                        flag16 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(70,77);
                        flag16 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView17:
                    if (!flag17) {
                        timeTextViewClickEventArray2CheckTrue(77,84);
                        flag17 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(77,84);
                        flag17 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView18:
                    if (!flag18) {
                        timeTextViewClickEventArray2CheckTrue(84,91);
                        flag18 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(84,91);
                        flag18 = false;

                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView19:
                    if (!flag19) {
                        timeTextViewClickEventArray2CheckTrue(91,98);
                        flag19 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(91,98);
                        flag19 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView20:
                    if (!flag20) {
                        timeTextViewClickEventArray2CheckTrue(98,105);
                        flag20 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(98,105);
                        flag20 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView21:
                    if (!flag21) {
                        timeTextViewClickEventArray2CheckTrue(105,112);
                        flag21 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(105,112);
                        flag21 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView22:
                    if (!flag22) {
                        timeTextViewClickEventArray2CheckTrue(112,119);
                        flag22 = true;
                    } else {
                        timeTextViewClickEventArray2CheckFalse(112,119);
                        flag22 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case R.id.textView23:
                    if (!flag23) {
                        timeTextViewClickEventArray2CheckTrue(119,126);
                        flag23 = true;

                    } else {
                        timeTextViewClickEventArray2CheckFalse(119,126);
                        flag23 = false;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
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
        timeMon.setOnClickListener(this);
        timeTue.setOnClickListener(this);
        timeWed.setOnClickListener(this);
        timeThu.setOnClickListener(this);
        timeFri.setOnClickListener(this);
        timeSat.setOnClickListener(this);
        timeSun.setOnClickListener(this);
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
        dayTimeMon.setOnClickListener(this);
        dayTimeTue.setOnClickListener(this);
        dayTimeWed.setOnClickListener(this);
        dayTimeThu.setOnClickListener(this);
        dayTimeFri.setOnClickListener(this);
        dayTimeSat.setOnClickListener(this);
        dayTimeSun.setOnClickListener(this);
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
    }

    public void textviewclickEventArray0CheckTrue(int i)
    {
        for(int j= i; j<126; j +=7)
        {
            lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList.get(j);
            lessonDTO2.check = true;
            arrayList.set(j, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void textviewclickEventArray0CheckFalse(int k)
    {
        for(int j= k; j<126; j +=7)
        {
            lessonDTO = new LessonDTO();
            lessonDTO = arrayList.get(j);
            lessonDTO.check = false;
            arrayList.set(j, lessonDTO);
        }
        for(int i=0; i<126; i++)
        {
            if(arrayList.get(i).check = false)
            {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            }
            else
            {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void timeTextViewClickEventArray0CheckTrue(int k,int l)
    {
        for (int i = k; i < l; i++) {
            lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList.get(i);
            lessonDTO2.check = true;
            arrayList.set(i, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void timeTextViewClickEventArray0CheckFalse(int k,int l)
    {
        for (int i = k; i < l; i++) {
            lessonDTO = new LessonDTO();
            lessonDTO = arrayList.get(i);
            lessonDTO.check = false;
            arrayList.set(i, lessonDTO);
        }
        for(int i=0; i<126; i++)
        {
            if(arrayList.get(i).check = false)
            {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            }
            else
            {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void textviewclickEventArray1CheckTrue(int i)
    {
        for(int j= i; j<126; j +=7)
        {
            lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList1.get(j);
            lessonDTO2.check = true;
            arrayList1.set(j, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void textviewclickEventArray1CheckFalse(int k)
    {
        for(int j= k; j<126; j +=7)
        {
            lessonDTO = new LessonDTO();
            lessonDTO = arrayList1.get(j);
            lessonDTO.check = false;
            arrayList1.set(j, lessonDTO);
        }
        for(int i=0; i<126; i++)
        {
            if(arrayList1.get(i).check = false)
            {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            }
            else
            {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void timeTextViewClickEventArray1CheckTrue(int k,int l)
    {
        for (int i = k; i < l; i++) {
            lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList1.get(i);
            lessonDTO2.check = true;
            arrayList1.set(i, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void timeTextViewClickEventArray1CheckFalse(int k,int l)
    {
        for (int i = k; i < l; i++) {
            lessonDTO = new LessonDTO();
            lessonDTO = arrayList1.get(i);
            lessonDTO.check = false;
            arrayList1.set(i, lessonDTO);
        }
        for(int i=0; i<126; i++)
        {
            if(arrayList1.get(i).check = false)
            {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            }
            else
            {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void textviewclickEventArray2CheckTrue(int i)
    {
        for(int j= i; j<126; j +=7)
        {
            lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList2.get(j);
            lessonDTO2.check = true;
            arrayList2.set(j, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void textviewclickEventArray2CheckFalse(int k)
    {
        for(int j= k; j<126; j +=7)
        {
            lessonDTO = new LessonDTO();
            lessonDTO = arrayList2.get(j);
            lessonDTO.check = false;
            arrayList2.set(j, lessonDTO);
        }
        for(int i=0; i<126; i++)
        {
            if(arrayList2.get(i).check = false)
            {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            }
            else
            {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void timeTextViewClickEventArray2CheckTrue(int k,int l)
    {
        for (int i = k; i < l; i++) {
            lessonDTO2 = new LessonDTO();
            lessonDTO2 = arrayList2.get(i);
            lessonDTO2.check = true;
            arrayList2.set(i, lessonDTO2);
        }
        modifyButton.setImageResource(R.drawable.modify_button);
        modifyButton.setEnabled(true);
    }

    public void timeTextViewClickEventArray2CheckFalse(int k,int l)
    {
        for (int i = k; i < l; i++) {
            lessonDTO = new LessonDTO();
            lessonDTO = arrayList2.get(i);
            lessonDTO.check = false;
            arrayList2.set(i, lessonDTO);
        }
        for(int i=0; i<126; i++)
        {
            if(arrayList2.get(i).check = false)
            {
                modifyButton.setImageResource(R.drawable.modify_button_grey);
                modifyButton.setEnabled(false);
            }
            else
            {
                modifyButton.setImageResource(R.drawable.modify_button);
                modifyButton.setEnabled(true);
                break;
            }
        }
    }

    public void modify(){

        if(week==0) {
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> items = new ArrayList<>();
                    items.add("회원 예약");
                    items.add("클래스 열기");
                    items.add("클래스 닫기");
                    CharSequence[] item =  items.toArray(new String[ items.size()]);
                    android.support.v7.app.AlertDialog.Builder alert_confirm = new android.support.v7.app.AlertDialog.Builder(getContext());
                    alert_confirm
                            .setItems(item, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
                                    switch (position)
                                    {
                                        case 0:
                                            ScheduleSearchDialog dialog = new ScheduleSearchDialog(getContext());
                                            dialog.setOnDismissListener(WriteScheduleFragment.this);
                                            dialog.show();
                                            break;
                                        case 1:
                                            for (int i = 0; i < 126; i++) {
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
                                            for (int i = 0; i < 126; i++) {
                                                if (arrayList.get(i).check) {
                                                    LessonDTO lessonDTO = new LessonDTO();
                                                    lessonDTO = arrayList.get(i);
                                                    lessonDTO.state = false;
                                                    lessonDTO.check = false;
                                                    lessonDTO.name = null;
                                                    lessonDTO.uid = null;
                                                    arrayList.set(i, lessonDTO);

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
        if(week==1) {
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> items = new ArrayList<>();
                    items.add("회원 예약");
                    items.add("클래스 열기");
                    items.add("클래스 닫기");
                    CharSequence[] item =  items.toArray(new String[ items.size()]);
                    android.support.v7.app.AlertDialog.Builder alert_confirm = new android.support.v7.app.AlertDialog.Builder(getContext());
                    alert_confirm
                            .setItems(item, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
                                    switch (position)
                                    {
                                        case 0:
                                            ScheduleSearchDialog dialog = new ScheduleSearchDialog(getContext());
                                            dialog.setOnDismissListener(WriteScheduleFragment.this);
                                            dialog.show();
                                            break;
                                        case 1:
                                            for (int i = 0; i < 126; i++) {
                                                if (arrayList1.get(i).check) {
                                                    LessonDTO lessonDTO = new LessonDTO();
                                                    lessonDTO = arrayList1.get(i);
                                                    lessonDTO.state = true;
                                                    lessonDTO.check = false;
                                                    lessonDTO.name = null;
                                                    lessonDTO.uid = null;
                                                    arrayList1.set(i, lessonDTO);

                                                }
                                            }
                                            check_flag_false();
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case 2:
                                            for (int i = 0; i < 126; i++) {
                                                if (arrayList1.get(i).check) {
                                                    LessonDTO lessonDTO = new LessonDTO();
                                                    lessonDTO = arrayList1.get(i);
                                                    lessonDTO.state = false;
                                                    lessonDTO.check = false;
                                                    lessonDTO.name = null;
                                                    lessonDTO.uid = null;
                                                    arrayList1.set(i, lessonDTO);

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
        else if(week==2) {
            modifyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> items = new ArrayList<>();
                    items.add("회원 예약");
                    items.add("클래스 열기");
                    items.add("클래스 닫기");
                    CharSequence[] item =  items.toArray(new String[ items.size()]);
                    android.support.v7.app.AlertDialog.Builder alert_confirm = new android.support.v7.app.AlertDialog.Builder(getContext());
                    alert_confirm
                            .setItems(item, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
                                    switch (position)
                                    {
                                        case 0:
                                            ScheduleSearchDialog dialog = new ScheduleSearchDialog(getContext());
                                            dialog.setOnDismissListener(WriteScheduleFragment.this);
                                            dialog.show();
                                            break;
                                        case 1:
                                            for (int i = 0; i < 126; i++) {
                                                if (arrayList2.get(i).check) {
                                                    LessonDTO lessonDTO = new LessonDTO();
                                                    lessonDTO = arrayList2.get(i);
                                                    lessonDTO.state = true;
                                                    lessonDTO.check = false;
                                                    lessonDTO.name = null;
                                                    lessonDTO.uid = null;
                                                    arrayList2.set(i, lessonDTO);

                                                }
                                            }
                                            check_flag_false();
                                            adapter.notifyDataSetChanged();
                                            break;
                                        case 2:
                                            for (int i = 0; i < 126; i++) {
                                                if (arrayList2.get(i).check) {
                                                    LessonDTO lessonDTO = new LessonDTO();
                                                    lessonDTO = arrayList2.get(i);
                                                    lessonDTO.state = false;
                                                    lessonDTO.check = false;
                                                    lessonDTO.name = null;
                                                    lessonDTO.uid = null;
                                                    arrayList2.set(i, lessonDTO);

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

    }
    

}


