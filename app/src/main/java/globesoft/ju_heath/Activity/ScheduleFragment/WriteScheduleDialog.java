package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fanwe.lib.switchbutton.FISwitchButton;
import com.fanwe.lib.switchbutton.FSwitchButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.xw.repo.BubbleSeekBar;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import globesoft.ju_heath.DTO.makeLessonDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.FormatUtil;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-22.
 */

public class WriteScheduleDialog extends Dialog{

    FSwitchButton timeSwitchButton;
    BubbleSeekBar weekBar;
    BubbleSeekBar peopleBar;
    ImageView cancelImageView;
    ImageView writeImageView;
    LinearLayout cancelImageLay;
    LinearLayout writeImageLay;
    TextView startTextView;
    TextView endTextView;
    TextView hourTimeTextView;
    TextView halfTimeTextView;


    makeLessonDTO makeLessonDTO;

    FirebaseFirestore db;

    String uid;
    boolean startnow;
    private OnDismissListener _listener ;
    public String startDate;

    public WriteScheduleDialog(@NonNull Context context,boolean startnow,String date) {
        super(context);
        this.startnow = startnow;
        this.startDate = date;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
        setContentView(R.layout.dialog_writeschedule);
        timeSwitchButton = (FSwitchButton)findViewById(R.id.timeSwitchButton);
        weekBar = (BubbleSeekBar)findViewById(R.id.weekBar);
        peopleBar = (BubbleSeekBar)findViewById(R.id.peopleBar);
        writeImageLay = (LinearLayout)findViewById(R.id.writeImageLay);
        cancelImageLay = (LinearLayout)findViewById(R.id.cancelImageLay);
        cancelImageView = (ImageView)findViewById(R.id.cancelImageView);
        writeImageView = (ImageView)findViewById(R.id.writeImageView);
        startTextView = (TextView)findViewById(R.id.startTextView);
        endTextView = (TextView)findViewById(R.id.endTextView);
        hourTimeTextView = (TextView)findViewById(R.id.hourTimeTextView);
        halfTimeTextView = (TextView)findViewById(R.id.halfTimeTextView);

        db=FirebaseFirestore.getInstance();
        uid = ((Ju_healthApp)getContext().getApplicationContext()).getUserUid();
        makeLessonDTO = new makeLessonDTO();

        if(startnow) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 6);                  ///////달력이 일요일부터시작하기때문에 다음주로넘기기위함
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            makeLessonDTO.startDay = format.format(calendar.getTime());
            startTextView.setText(makeLessonDTO.startDay);
            calendar.add(Calendar.DATE, 6);
            makeLessonDTO.endDay = format.format(calendar.getTime());
            endTextView.setText(makeLessonDTO.endDay);
        }
        else
        {
            try {
                LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+startDate);
                SimpleDateFormat stringformat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
                Date simpleDateFormat = stringformat.parse(startDate);
                LogUtil.d("zzzzzzzzzzzzzzzzzzxxxxxxxxxx"+simpleDateFormat);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(simpleDateFormat);
                calendar.add(Calendar.DATE, 7);                  ///////다음주로넘기기위함
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                makeLessonDTO.startDay = format.format(calendar.getTime());
                LogUtil.d("zzzzzzzzzzzzzzzzzzzzcccccccccc"+makeLessonDTO.startDay);
                startTextView.setText(makeLessonDTO.startDay);
                calendar.add(Calendar.DATE, 6);
                makeLessonDTO.endDay = format.format(calendar.getTime());
                LogUtil.d("zzzzzzzzzzzzzzzzzzzzvvvvvvvvvv"+makeLessonDTO.endDay);
                endTextView.setText(makeLessonDTO.endDay);
            } catch (ParseException e) {
                e.printStackTrace();
            }



        }

        weekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {

                LogUtil.d("zzzzzzzzzzzzzzzzzzzz"+progress+"cccccccccccccccc"+progressFloat);
                if(progress==1)
                {
                    endTextView.setText(makeLessonDTO.endDay);
                }
                else if(progress==2)
                {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date tempDate = dateFormat.parse(makeLessonDTO.endDay);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(tempDate);
                        calendar1.add(Calendar.DATE,7);
                        String temp = dateFormat.format(calendar1.getTime());
                        endTextView.setText(temp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else if(progress==3)
                {
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date tempDate = dateFormat.parse(makeLessonDTO.endDay);
                        Calendar calendar1 = Calendar.getInstance();
                        calendar1.setTime(tempDate);
                        calendar1.add(Calendar.DATE,14);
                        String temp = dateFormat.format(calendar1.getTime());
                        endTextView.setText(temp);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });


        timeSwitchButton.setOnCheckedChangedCallback(new FISwitchButton.OnCheckedChangedCallback() {
            @Override
            public void onCheckedChanged(boolean checked, FSwitchButton view) {
                if(checked)
                {
                    halfTimeTextView.setTextColor(Color.parseColor("#262a4f"));
                    hourTimeTextView.setTextColor(Color.parseColor("#ee2b47"));

                }
                else
                {
                    halfTimeTextView.setTextColor(Color.parseColor("#ee2b47"));
                    hourTimeTextView.setTextColor(Color.parseColor("#262a4f"));
                }
            }
        });


        cancelImageLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeLessonDTO.maketrue = false;
                dismiss();
            }
        });

        writeImageLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeLessonDTO.maketrue = true;
                makeLessonDTO.people = peopleBar.getProgress();
                makeLessonDTO.week = weekBar.getProgress();
                if(timeSwitchButton.isChecked())
                {
                    makeLessonDTO.lessonTime = 1;
                }
                else
                {
                    makeLessonDTO.lessonTime = 0;
                }
                if(makeLessonDTO.maketrue)
                {

                    LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaa"+makeLessonDTO.lessonTime);
                    if( _listener == null ) {} else {
                        _listener.onDismiss( WriteScheduleDialog.this ) ;
                    }
                    dismiss();
                }
            }
        });

/*
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             makeLessonDTO.maketrue = false;
             dismiss();
            }
        });*/

       /* writeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                makeLessonDTO.maketrue = true;
                makeLessonDTO.people = peopleBar.getProgress();
                makeLessonDTO.week = weekBar.getProgress();
                if(timeSwitchButton.isChecked())
                {
                    makeLessonDTO.lessonTime = 1;
                }
                else
                {
                    makeLessonDTO.lessonTime = 0;
                }
                if(makeLessonDTO.maketrue)
                {

                    LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaa"+makeLessonDTO.lessonTime);
                    if( _listener == null ) {} else {
                        _listener.onDismiss( WriteScheduleDialog.this ) ;
                    }
                    dismiss();
                }
            }
        });
*/

    }



    public void setOnDismissListener( OnDismissListener $listener ) {
        _listener = $listener ;
    }

    public makeLessonDTO getmakeLessonDTO() {
        return makeLessonDTO;
    }


    public WriteScheduleDialog(@NonNull Context context) {
        super(context);
    }
}
