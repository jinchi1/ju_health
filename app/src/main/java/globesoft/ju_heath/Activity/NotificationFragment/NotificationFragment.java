package globesoft.ju_heath.Activity.NotificationFragment;

import android.content.Context;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2017-12-20.
 */

public class NotificationFragment extends Fragment{

    public static final String TRAINER = "trainer";
    public static final String USER = "user";
    public static final String NOTI = "noti";

    View view;
    RecyclerView list;
    NotificationAdapter notificationAdapter;
    Context context;

    String uid = "";

    boolean isHalf;
    String profile_path;
    String week;
    String lesson;
    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static ArrayList<Item> items;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        uid =  ((Ju_healthApp)getApplicationContext()).getUserUid();

        if(view == null)
        {
            context = getContext();
            view = inflater.inflate(R.layout.fragment_notification, container, false);
            list = (RecyclerView) view.findViewById(R.id.notification_recyclerView);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            list.setLayoutManager(layoutManager);


        }

        fetchQuote(uid);
        return view;
    }

    /**
     * 트레이너의 최근 7일간의 noti 조회
     * @param t_uid trainer uid
     */
    public void fetchQuote(String t_uid) {
        items = new ArrayList<>();

        long now = System.currentTimeMillis();
        Date date = new Date(now - (20 * 24 * 60 * 60 * 1000));      // 7일 전 날짜
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String befor_date = dateFormat.format(date);

        // noti data: where 절을 통한 검색
        db.collection(TRAINER).document(t_uid).collection(NOTI)
                .whereGreaterThan("regdate",befor_date)
                .orderBy("regdate", Query.Direction.DESCENDING)
                .limit(20)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {


                            for(DocumentSnapshot document : task.getResult()) {
                                String message = "";

                               if(document.contains("isHalf")) isHalf = document.getBoolean("isHalf");
                                String type = document.getString("type");
                               if(document.contains("lesson")) lesson = document.getString("lesson");
                                String name = document.getString("name");
                                String regdate = document.getString("regdate");
                                String uid = document.getString("uid");
                                if(document.contains("profile_path")) profile_path = document.getString("profile_path");
                                String diff_date = "";
                                if(document.contains("week")) {
                                    week = document.getString("week");

                                    Date tempdate = null;
                                    try {
                                        tempdate = new SimpleDateFormat("yyyy-MM-dd").parse(week);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(tempdate);

                                    int tempLesson = Integer.parseInt(lesson);

                                    if (tempLesson % 7 == 0) {

                                    } else if (tempLesson % 7 == 1) {
                                        calendar.add(Calendar.DATE, 1);
                                    } else if (tempLesson % 7 == 2) {
                                        calendar.add(Calendar.DATE, 2);
                                    } else if (tempLesson % 7 == 3) {
                                        calendar.add(Calendar.DATE, 3);
                                    } else if (tempLesson % 7 == 4) {
                                        calendar.add(Calendar.DATE, 4);
                                    } else if (tempLesson % 7 == 5) {
                                        calendar.add(Calendar.DATE, 5);
                                    } else if (tempLesson % 7 == 6) {
                                        calendar.add(Calendar.DATE, 6);
                                    }

                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월dd일");
                                    String tempDay = simpleDateFormat.format(calendar.getTime());


                                    String time = "";

                                    if(isHalf)
                                    {
                                        int start_time = Integer.parseInt(lesson)+1;
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


                                        if(type.equals("R"))    {  message += (name + " 님이 "+ tempDay + time + " PT수업을 예약하셨습니다.");       }
                                        if(type.equals("C"))    {  message += (name + " 님이 "+ tempDay + time + " PT수업을 취소하셨습니다.");       }
                                        if(type.equals("W"))    {  message += (name + " 님이 "+ tempDay + time + " PT수업을 대기예약하셨습니다.");   }
                                    }
                                    else
                                    {
                                        int start_time = Integer.parseInt(lesson)+1;
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

                                        if(type.equals("R"))    {  message += (name + " 님이 "+ tempDay + time + " PT수업을 예약하셨습니다.");       }
                                        if(type.equals("C"))    {  message += (name + " 님이 "+ tempDay + time + " PT수업을 취소하셨습니다.");       }
                                        if(type.equals("W"))    {  message += (name + " 님이 "+ tempDay + time + " PT수업을 대기예약하셨습니다.");   }
                                    }

                                }
                               else if(type.equals("A")){
                                    message += (name + " 님이 " + "트레이너님께 회원 신청하셨습니다.");
                                }else {


                                }
                                System.out.println(message);

                                // 현재 날짜와 noti 작성일의 차이 "일,시,분,초" 별로 구하기
                                try{
                                    Date today = new Date();
                                    Date date = dateFormat.parse(regdate);

                                    long diff = 0;
                                    diff = today.getTime() - date.getTime();
                                    long days = 0;
                                    long hour = 0;
                                    long min = 0;
                                    long sec = 0;

                                    Calendar cal1 = Calendar.getInstance();
                                    cal1.setTime(today);
                                    Calendar cal2 = Calendar.getInstance();
                                    cal2.setTime(date);

                                    days = cal1.get(Calendar.DATE) - cal2.get(Calendar.DATE);
                                    if(days != 0){
                                        diff_date = days + "일 전";
                                    }else {
                                        hour = cal1.get(Calendar.HOUR) - cal2.get(Calendar.HOUR);
                                        if(hour != 0) {
                                            diff_date = hour + "시간 전";
                                        }else {
                                            min = cal1.get(Calendar.MINUTE) - cal2.get(Calendar.MINUTE);
                                            if (min != 0) {
                                                diff_date = min + "분 전";
                                            }else {
                                                sec = cal1.get(Calendar.SECOND) - cal2.get(Calendar.SECOND);
                                                if(sec != 0){
                                                    diff_date = sec + "초 전";
                                                }
                                            }
                                        }
                                    }
                                }catch(ParseException e){
                                    e.printStackTrace();
                                }

                                Item item = new Item();
                                item.setProfile_path(profile_path);
                                item.setMessage(message);
                                item.setDiff_date(diff_date);
                                items.add(item);

                            } // for문

                            notificationAdapter = new NotificationAdapter(context, items);
                            list.setAdapter(notificationAdapter);
                            notificationAdapter.notifyDataSetChanged();


                        } else {
                            System.out.println("Fail>>>>>>>>" + task.getException());
                        }
                    }
                });
    }




    /**
     * Trainer noti 문서 삭제 함수
     * @param t_uid trainer uid
     * @param u_uid user uid
     * @param lesson 수업시간
     */
    public void deleteTrainerNotiQuote(final String t_uid, String u_uid, String lesson) {
        // noti에서 삭제 할 문서id 검색 쿼리
        db.collection(TRAINER).document(t_uid).collection(NOTI)
                .whereEqualTo("lesson", lesson)
                .whereEqualTo(USER, u_uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for(DocumentSnapshot document : task.getResult()) {
                                System.out.println("getID>>>>>>>>" + document.getId());


                                // noti: 검색된 문서id값을 통해서 문서 삭제 쿼리
                                db.collection(TRAINER).document(t_uid).collection(NOTI).document(document.getId())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i("Success", "삭제 성공~!!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("Success", "삭제 실패~!!");
                                                System.out.println("Error deleting document" + e);
                                            }
                                        });
                            }
                        } else {
                            System.out.println("get failed with " + task.getException());
                        }
                    }
                });
    }
}
