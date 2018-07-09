package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.otto.Subscribe;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import globesoft.ju_heath.Activity.SignActivity;
import globesoft.ju_heath.Common.PushEvent;
import globesoft.ju_heath.DTO.LessonDTO;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.FormatUtil;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-15.
 */

public class ScheduleAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<LessonDTO> dateArr;
    String uid;
    String documentName;
    String teacher_token;
    boolean isHalf;
    String profile_path;
    public static final String TRAINER = "trainer";
    public static final String USER = "user";
    public static final String NOTI = "noti";

    public static boolean isSign = false;       //유저를클릭한건지 아니면 다른곳을누르거나 백키로 다이얼로그를 dismiss시킨건지 확인하기위해사용


    public ScheduleAdapter(Context context, ArrayList<LessonDTO> arrayList,String uid,String documentName,boolean isHalf,String profile_path)
    {
        this.context = context;
        this.dateArr = arrayList;
        this.uid = uid;
        this.documentName = documentName;
        this.isHalf = isHalf;
        this.profile_path = profile_path;
    }

    public ScheduleAdapter(Context context, ArrayList<LessonDTO> arrayList,String uid,String documentName,boolean isHalf)
    {
        this.context = context;
        this.dateArr = arrayList;
        this.uid = uid;
        this.documentName = documentName;
        this.isHalf = isHalf;
    }

    @Override
    public int getCount() {
        return dateArr.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void addItems(ArrayList<LessonDTO> addItem) {
        dateArr = addItem;
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        View v = view;
        ViewHolder viewHolder;
        final LessonDTO data = dateArr.get(position);
        if(v==null)
        {
            v= LayoutInflater.from(context).inflate(R.layout.row_schedule, null);
            viewHolder = new ViewHolder();
            viewHolder.rowLayout = (LinearLayout)v.findViewById(R.id.row_layout);
            viewHolder.rowImageView = (ImageView)v.findViewById(R.id.row_image);
            viewHolder.rowTextView = (TextView)v.findViewById(R.id.row_text);
            v.setTag(viewHolder);
        }else
        {
            viewHolder = (ViewHolder)view.getTag();
        }
        FirebaseAuth auth = FirebaseAuth.getInstance();
        final String myUid = auth.getCurrentUser().getUid();

        if(((Ju_healthApp)context.getApplicationContext()).getuserValue().equals("trainer"))
        {
            if(data.state)
            {

                viewHolder.rowLayout.setBackgroundColor(Color.parseColor("#fdfdfd"));
                viewHolder.rowImageView.setVisibility(View.GONE);
                viewHolder.rowTextView.setVisibility(View.GONE);
                if(data.name!=null&&data.name.size()>0)
                {
                    viewHolder.rowImageView.setVisibility(View.VISIBLE);
                    viewHolder.rowTextView.setVisibility(View.VISIBLE);
                    LogUtil.d("pppppppppppppppppppppppppppppppppp"+data.name.get(0));
                    String a = data.name.get(0).substring(0,1);
                    LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaa"+a);
                    viewHolder.rowTextView.setText(a);
                    viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //Toast.makeText(context,"hellow",Toast.LENGTH_SHORT).show();

                                isSign=false;

                                ScheduleListDialog scheduleListDialog = new ScheduleListDialog(context, data.uid, context);
                                scheduleListDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                                scheduleListDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialogInterface) {
                                    if (isSign) {                                   ///////////사용자눌렀는지판단여부
                                        if(!data.sign) {                            //////////사용자가 이미 사인을했는지 판단여부
                                            ScheduleListDialog dialog = (ScheduleListDialog) dialogInterface;
                                            usersDTO usersDTO = ((ScheduleListDialog) dialog).getUserDTO();
                                            Intent intent = new Intent(context, SignActivity.class);
                                            intent.putExtra("lessonDate",data.date);
                                            intent.putExtra("startDate",documentName);
                                            intent.putExtra("lesson",data.lesson);
                                            intent.putExtra("userUid", usersDTO.uid);
                                            intent.putExtra("coachUid", usersDTO.coach);
                                            intent.putExtra("nowCount", usersDTO.nowCount);
                                            intent.putExtra("totalCount", usersDTO.totalCount);

                                            context.startActivity(intent);

                                        }
                                        else
                                        {
                                            Toast.makeText(context,"이미 작성하셨습니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                            scheduleListDialog.show();


                        }
                    });
                }
            }
            else
            {
                viewHolder.rowLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
                viewHolder.rowImageView.setVisibility(View.GONE);
                viewHolder.rowTextView.setVisibility(View.GONE);
            }
        }
        else
        {



            if(data.state)
            {
                viewHolder.rowLayout.setBackgroundColor(Color.parseColor("#fdfdfd"));
                viewHolder.rowImageView.setVisibility(View.GONE);
                viewHolder.rowTextView.setVisibility(View.GONE);
                if(data.name!=null&&data.name.size()>0)
                {
                    viewHolder.rowImageView.setVisibility(View.VISIBLE);
                    viewHolder.rowImageView.setBackgroundColor(Color.parseColor("#fdfdfd"));
                    LogUtil.d("pppppppppppppppppppppppppppppppppp"+data.name.get(0));
                    for(int i=0; i<data.name.size();i++)
                    {
                        if(data.uid!=null&&data.uid.get(i).equals(myUid))
                        {
                            viewHolder.rowImageView.setImageResource(R.drawable.icon_my_reservation);
                            viewHolder.rowImageView.setOnClickListener(new View.OnClickListener() {             //////////예약취소
                                @Override
                                public void onClick(View view) {
                                    AlertDialog.Builder ab = new AlertDialog.Builder(context);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ab.setView(R.layout.dialog_custom_cancel);
                                    }
                                    ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+documentName+uid);
                                            final String coachuid = uid;
                                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                                            DocumentReference documentReference = db.collection("trainer").document(uid).collection("schedule").document(documentName);
                                            ArrayList<String> name = new ArrayList<>();
                                            name = data.name;
                                            final String myname = ((Ju_healthApp)context.getApplicationContext()).getuserName();
                                            name.remove(myname);
                                            ArrayList<String> uid = new ArrayList<>();
                                            uid = data.uid;
                                            uid.remove(((Ju_healthApp)context.getApplicationContext()).getUserUid());
                                            final ArrayList<String> finalName = name;               ///임시 카피변수
                                            final ArrayList<String> finalUid = uid;                 ///임시 카피변수
                                            documentReference.update(String.valueOf(position)+".name",name
                                                    ,String.valueOf(position)+".uid",uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    data.name = finalName;
                                                    data.uid = finalUid;
                                                    notifyDataSetChanged();
                                                    LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzSuccess");
                                                    saveQuote("C",String.valueOf(data.lesson),coachuid,myUid,myname,isHalf,documentName,profile_path,position);

                                                }
                                            });

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

                                }
                            });
                            break;
                        }
                        else
                        {
                            if(data.waiting_uid!=null)
                            {
                                if(data.waiting_uid.equals(myUid))
                                {
                                    viewHolder.rowImageView.setImageResource(R.drawable.icon_waiting_reservation);
                                    viewHolder.rowImageView.setOnClickListener(new View.OnClickListener() {                 ////////대기예약취소
                                        @Override
                                        public void onClick(View view) {
                                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ab.setView(R.layout.dialog_custom_cancel2);
                                            }
                                            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+documentName+uid);
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    DocumentReference documentReference = db.collection("trainer").document(uid).collection("schedule").document(documentName);
                                                    final String name = null;
                                                    final String uid = null;
                                                    documentReference.update(String.valueOf(position)+".waiting",name
                                                            ,String.valueOf(position)+".waiting_uid",uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            data.waiting = name;
                                                            data.waiting_uid = uid;
                                                            notifyDataSetChanged();
                                                            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzSuccess");
                                                        }
                                                    });

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
                                    }
                                    });
                                }
                                else
                                {
                                    viewHolder.rowImageView.setImageResource(R.drawable.icon_other_reservation);        ///////예약,대기예약 불가
                                    viewHolder.rowImageView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(context,"예약,대기예약 신청가능한 인원이 초과 되었습니다.",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                            else
                            {
                                viewHolder.rowImageView.setImageResource(R.drawable.icon_other_reservation);
                                if(data.name.size()==data.user_count)
                                {
                                    viewHolder.rowImageView.setOnClickListener(new View.OnClickListener() {         //////////대기예약
                                        @Override
                                        public void onClick(View view) {
                                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ab.setView(R.layout.dialog_custom_alert2);
                                            }
                                            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+documentName+uid);
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    DocumentReference documentReference = db.collection("trainer").document(uid).collection("schedule").document(documentName);
                                                    final String coachuid = uid;
                                                    final String name = ((Ju_healthApp)context.getApplicationContext()).getuserName();
                                                    final String uid = ((Ju_healthApp)context.getApplicationContext()).getUserUid();
                                                    documentReference.update(String.valueOf(position)+".waiting",name
                                                            ,String.valueOf(position)+".waiting_uid",uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            data.waiting = name;
                                                            data.waiting_uid = uid;
                                                            notifyDataSetChanged();
                                                            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzSuccess");
                                                            saveQuote("W",String.valueOf(data.lesson),coachuid,uid,name,isHalf,documentName,profile_path,position);
                                                        }
                                                    });

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
                                        }
                                    });

                                }
                                else
                                {
                                    viewHolder.rowImageView.setOnClickListener(new View.OnClickListener() {         ///////남은자리 예약
                                        @Override
                                        public void onClick(View view) {

                                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                                ab.setView(R.layout.dialog_custom_alert);
                                            }
                                            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                    LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+documentName+uid);
                                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                                    DocumentReference documentReference = db.collection("trainer").document(uid).collection("schedule").document(documentName);
                                                    final String coachuid = uid;
                                                    ArrayList<String> name = new ArrayList<>();
                                                    name = data.name;
                                                    final String myname = ((Ju_healthApp)context.getApplicationContext()).getuserName();
                                                    name.add(myname);
                                                    ArrayList<String> uid = new ArrayList<>();
                                                    uid = data.uid;
                                                    uid.add(((Ju_healthApp)context.getApplicationContext()).getUserUid());
                                                    final ArrayList<String> finalName = name;               ///임시 카피변수
                                                    final ArrayList<String> finalUid = uid;                 ///임시 카피변수
                                                    documentReference.update(String.valueOf(position)+".name",name
                                                            ,String.valueOf(position)+".uid",uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            data.name = finalName;
                                                            data.uid = finalUid;
                                                            notifyDataSetChanged();
                                                            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzSuccess");
                                                            saveQuote("R",String.valueOf(data.lesson),coachuid,myUid,myname,isHalf,documentName,profile_path,position);

                                                        }
                                                    });

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
                                        }
                                    });
                                }
                            }
                        }
                    }
                }
                else
                {
                    viewHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder ab = new AlertDialog.Builder(context);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ab.setView(R.layout.dialog_custom_alert);
                            }
                            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {               ////////////빈강의예약
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+documentName+uid);
                                    final String coachuid = uid;
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference documentReference = db.collection("trainer").document(uid).collection("schedule").document(documentName);
                                    final ArrayList<String> name = new ArrayList<>();
                                    final String myname = ((Ju_healthApp)context.getApplicationContext()).getuserName();
                                    name.add(myname);
                                    final ArrayList<String> uid = new ArrayList<>();
                                    uid.add(((Ju_healthApp)context.getApplicationContext()).getUserUid());
                                    documentReference.update(String.valueOf(position)+".name",name
                                            ,String.valueOf(position)+".uid",uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            data.name = name;
                                            data.uid = uid;
                                            notifyDataSetChanged();
                                            LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzSuccess");
                                            saveQuote("R",String.valueOf(data.lesson),coachuid,myUid,myname,isHalf,documentName,profile_path,position);

                                        }
                                    });

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
                        }
                    });
                }
            }
            else
            {
                viewHolder.rowLayout.setBackgroundColor(Color.parseColor("#f2f2f2"));
                viewHolder.rowImageView.setVisibility(View.GONE);
                viewHolder.rowTextView.setVisibility(View.GONE);
            }
        }



        return v;
    }

    /**
     * noti 디비에 입력하기
     * @param type (R: reservation 예약, C: cancel 예약취소, A: apply 트레이너 신청, W: waiting 대기예약)
     * @param lesson 수업시간
     * @param t_uid   trainer uid
     * @param u_uid   user uid
     * @param name  이름
     */
    public void saveQuote(final String type, final String lesson, final String t_uid, String u_uid, final String name, final boolean isHalf, final String week,String profile_path, final int position){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 출력될 포맷 설정
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now_date = simpleDateFormat.format(date);
        Log.i("now_date>>>>>>>>>", now_date);

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("type", type);
        dataToSave.put("lesson", lesson);
        dataToSave.put("uid", u_uid);
        dataToSave.put("name", name);
        dataToSave.put("regdate", now_date);
        dataToSave.put("isHalf",isHalf);
        dataToSave.put("week",week);
        dataToSave.put("profile_path",profile_path);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(TRAINER).document(t_uid).collection(NOTI).document()
                .set(dataToSave, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("success", "성공");
                db.collection("trainer").document(t_uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        teacher_token = documentSnapshot.getString("token");
                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        Date tempdate = null;
                        try {
                            tempdate = new SimpleDateFormat("yyyy-MM-dd").parse(documentName);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(tempdate);

                        int tempLesson = position;

                        if(tempLesson%7==0)
                        {

                        }
                        else if(tempLesson%7==1)
                        {
                            calendar.add(Calendar.DATE, 1);
                        }
                        else if(tempLesson%7==2)
                        {
                            calendar.add(Calendar.DATE, 2);
                        }
                        else if(tempLesson%7==3)
                        {
                            calendar.add(Calendar.DATE, 3);
                        }
                        else if(tempLesson%7==4)
                        {
                            calendar.add(Calendar.DATE, 4);
                        }
                        else if(tempLesson%7==5)
                        {
                            calendar.add(Calendar.DATE, 5);
                        }
                        else if(tempLesson%7==6)
                        {
                            calendar.add(Calendar.DATE, 6);
                        }

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월dd일");
                        String tempDay = simpleDateFormat.format(calendar.getTime());
                        String time = null;
                        if(isHalf)
                        {
                            int start_time = position+1;
                            if(start_time <= 7){    time = "06:00"; }
                            else if(start_time >= 8 && start_time <= 14)       {   time = "06:30";  }
                            else if(start_time >= 15 && start_time <= 21)      {   time = "07:00";  }
                            else if(start_time >= 22 && start_time <= 28)      {   time = "07:30";  }
                            else if(start_time >= 29 && start_time <= 35)      {   time = "08:00";  }
                            else if(start_time >= 36 && start_time <= 42)      {    time = "08:30"; }
                            else if(start_time >= 43 && start_time <= 49)      {    time = "09:00"; }
                            else if(start_time >= 50 && start_time <= 56)      {    time = "09:30"; }
                            else if(start_time >= 57 && start_time <= 63)      {    time = "10:00"; }
                            else if(start_time >= 64 && start_time <= 70)      {    time = "10:30"; }
                            else if(start_time >= 71 && start_time <= 77)      {    time = "11:00"; }
                            else if(start_time >= 78 && start_time <= 84)      {    time = "11:30"; }
                            else if(start_time >= 85 && start_time <= 91)      {    time = "12:00"; }
                            else if(start_time >= 92 && start_time <= 98)      {    time = "12:30"; }
                            else if(start_time >= 99 && start_time <= 105)     {    time = "13:00"; }
                            else if(start_time >= 106 && start_time <= 112)    {    time = "13:30"; }
                            else if(start_time >= 113 && start_time <= 119)    {    time = "14:00"; }
                            else if(start_time >= 120 && start_time <= 126)    {    time = "14:30"; }///
                            else if(start_time >= 127 && start_time <= 133)    {    time = "15:00"; }
                            else if(start_time >= 134 && start_time <= 140)    {    time = "15:30"; }
                            else if(start_time >= 141 && start_time <= 147)    {    time = "16:00"; }
                            else if(start_time >= 148 && start_time <= 154)    {    time = "16:30"; }
                            else if(start_time >= 155 && start_time <= 161)    {    time = "17:00"; }
                            else if(start_time >= 162 && start_time <= 168)    {    time = "17:30"; }
                            else if(start_time >= 169 && start_time <= 175)    {    time = "18:00"; }
                            else if(start_time >= 176 && start_time <= 182)    {    time = "18:30"; }
                            else if(start_time >= 183 && start_time <= 189)    {    time = "19:00"; }
                            else if(start_time >= 190 && start_time <= 196)    {    time = "19:30"; }
                            else if(start_time >= 197 && start_time <= 203)    {    time = "20:00"; }
                            else if(start_time >= 204 && start_time <= 210)    {    time = "20:30"; }
                            else if(start_time >= 211 && start_time <= 217)    {    time = "21:00"; }
                            else if(start_time >= 218 && start_time <= 224)    {    time = "21:30"; }
                            else if(start_time >= 225 && start_time <= 231)    {    time = "22:00"; }
                            else if(start_time >= 232 && start_time <= 238)    {    time = "22:30"; }
                            else if(start_time >= 239 && start_time <= 245)    {    time = "23:00"; }
                            else if(start_time >= 246 && start_time <= 252)    {    time = "23:30"; }

                        }
                        else
                        {
                            int start_time = position+1;
                            if(start_time <= 7){    time = "06:00"; }
                            else if(start_time >= 8 && start_time <= 14)       {   time = "07:00";  }
                            else if(start_time >= 15 && start_time <= 21)      {   time = "08:00";  }
                            else if(start_time >= 22 && start_time <= 28)      {   time = "09:00";  }
                            else if(start_time >= 29 && start_time <= 35)      {   time = "10:00";  }
                            else if(start_time >= 36 && start_time <= 42)      {    time = "11:00"; }
                            else if(start_time >= 43 && start_time <= 49)      {    time = "12:00"; }
                            else if(start_time >= 50 && start_time <= 56)      {    time = "13:00"; }
                            else if(start_time >= 57 && start_time <= 63)      {    time = "14:00"; }
                            else if(start_time >= 64 && start_time <= 70)      {    time = "15:00"; }
                            else if(start_time >= 71 && start_time <= 77)      {    time = "16:00"; }
                            else if(start_time >= 78 && start_time <= 84)      {    time = "17:00"; }
                            else if(start_time >= 85 && start_time <= 91)      {    time = "18:00"; }
                            else if(start_time >= 92 && start_time <= 98)      {    time = "19:00"; }
                            else if(start_time >= 99 && start_time <= 105)     {    time = "20:00"; }
                            else if(start_time >= 106 && start_time <= 112)    {    time = "21:00"; }
                            else if(start_time >= 113 && start_time <= 119)    {    time = "22:00"; }
                            else if(start_time >= 120 && start_time <= 126)    {    time = "23:00"; }

                        }


                        call_push(teacher_token,type,((Ju_healthApp)context.getApplicationContext()).getuserName(),tempDay,time);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context,"bbbbbbbbbbbbbbb",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("fail", "실패");
                System.out.println("Fail>>>>>>>>" + e.getMessage());
            }
        });

    }




    private static class ViewHolder {

        public LinearLayout rowLayout;
        public ImageView rowImageView;
        public TextView rowTextView;

    }

    public void call_push(final String teacher_token, final String type, final String name, final String day,final String time)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC 메시지 생성 start
                    LogUtil.d("ddddddddddddddddddddddddddddddddddddddddddddddddddddd");
                    JSONObject root = new JSONObject();
                    JSONObject data = new JSONObject();
                    if(type.equals("R"))
                    {
                        data.put("type","R");
                        data.put("message", name+"님이 "+day+time+" PT수업을 예약했습니다.");
                    }
                    else if(type.equals("W"))
                    {
                       data.put("type","W");
                       data.put("message", name+"님이 "+day+time+" PT수업을 대기예약했습니다.");
                    }
                    else if(type.equals("C"))
                    {
                        data.put("type","C");
                        data.put("message", name+"님이 "+day+time+" PT수업을 취소했습니다.");
                    }
                    data.put("title", "title");
                    root.put("data", data);
                    root.put("priority","high");
                    root.put("to", teacher_token);
                    // FMC 메시지 생성 end

                    URL Url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.addRequestProperty("Authorization", "key=" + "AAAA993NYAA:APA91bFrHN2RRVTk1VNBTm5ZUB1R9C-nAQv2U-ktWL49ig8-zJA7NueQlFHfA0tuYWEt_l7qa-IQa0TDAoZH4DNkycEvCFDh9af6Nmk0fOkX9e38zGjFkn0lIHoPU0dU5cSzgP9JivvB");
                    conn.setRequestProperty("Content-type", "application/json");
                    OutputStream os = conn.getOutputStream();
                    os.write(root.toString().getBytes("utf-8"));
                    os.flush();
                    conn.getResponseCode();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}


