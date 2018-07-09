package globesoft.ju_heath.Activity.CalendarFragment;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pl.wheelview.WheelView;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;

import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;
import petrov.kristiyan.colorpicker.ColorPicker;

public class CalendarWriteActivity extends AppCompatActivity implements View.OnClickListener,SwitchButton.OnCheckedChangeListener{

    LinearLayout startDateLay;
    LinearLayout endDateLay;
    LinearLayout startWVLay;
    LinearLayout endWVLay;
    LinearLayout startTimeWVLay;
    LinearLayout endTimeWVLay;
    LinearLayout startTimeLay;
    LinearLayout endTimeLay;
    LinearLayout colorLay;
    LinearLayout alarmWVLay;
    ImageView colorImageView;
    WheelView sWvYear;
    WheelView sWvMonth;
    WheelView sWvDay;
    WheelView eWvYear;
    WheelView eWvMonth;
    WheelView eWvDay;
    WheelView sWvHour;
    WheelView sWvMin;
    WheelView eWvHour;
    WheelView eWvMin;
    WheelView  alarmWvHour;
    WheelView  alarmWvMin;
    SwitchButton switchButton;
    SwitchButton alarmButton;
    ArrayList<String> yearArr;
    ArrayList<String> monthArr;
    ArrayList<String> dayArr;
    ArrayList<String> hourArr;
    ArrayList<String> minArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_write);
        startDateLay=(LinearLayout)findViewById(R.id.startDateLayout);
        endDateLay=(LinearLayout)findViewById(R.id.endDateLayout);
        startTimeLay = (LinearLayout)findViewById(R.id.startTimeLayout);
        endTimeLay = (LinearLayout)findViewById(R.id.endTimeLayout);
        startWVLay=(LinearLayout)findViewById(R.id.startWVLayout);
        endWVLay=(LinearLayout)findViewById(R.id.endWVLayout);
        startTimeWVLay = (LinearLayout)findViewById(R.id.startTimeWVLayout);
        endTimeWVLay = (LinearLayout)findViewById(R.id.endTimeWVLayout);
        colorLay = (LinearLayout)findViewById(R.id.colorLayout);
        alarmWVLay = (LinearLayout)findViewById(R.id.alarmWVLayout);

        colorImageView = (ImageView)findViewById(R.id.colorImageView);
        sWvYear=(WheelView)findViewById(R.id.wv1);
        sWvMonth=(WheelView)findViewById(R.id.wv2);
        sWvDay=(WheelView)findViewById(R.id.wv3);
        eWvYear=(WheelView)findViewById(R.id.wv4);
        eWvMonth=(WheelView)findViewById(R.id.wv5);
        eWvDay=(WheelView)findViewById(R.id.wv6);
        sWvHour = (WheelView)findViewById(R.id.wvSHour);
        sWvMin = (WheelView)findViewById(R.id.wvSMin);
        eWvHour = (WheelView)findViewById(R.id.wvEHour);
        eWvMin = (WheelView)findViewById(R.id.wvEMin);
        alarmWvHour = (WheelView)findViewById(R.id.wvAlarmHour);
        alarmWvMin = (WheelView)findViewById(R.id.wvAlarmMin);

        switchButton = (SwitchButton)findViewById(R.id.switch_button);
        alarmButton = (SwitchButton)findViewById(R.id.alarm_button);

        yearArr = new ArrayList<String>();
        monthArr = new ArrayList<String>();
        dayArr = new ArrayList<String>();
        hourArr = new ArrayList<String>();
        minArr = new ArrayList<String>();

        for(int i =2018; i<2030; i++)
        {
            yearArr.add(String.valueOf(i));
        }
        for(int j = 1; j<13; j++)
        {
            monthArr.add(String.valueOf(j));
        }
        for(int k = 1; k<32; k++)
        {
            dayArr.add(String.valueOf(k));
        }
        for(int l = 0; l<13; l++)
        {
            hourArr.add(String.valueOf(l));
        }
        for(int m = 0; m<61; m++)
        {
            minArr.add(String.valueOf(m));
        }

        sWvYear.setData(yearArr);
        sWvMonth.setData(monthArr);
        sWvDay.setData(dayArr);
        eWvYear.setData(yearArr);
        eWvMonth.setData(monthArr);
        eWvDay.setData(dayArr);
        sWvHour.setData(hourArr);
        sWvMin.setData(minArr);
        eWvHour.setData(hourArr);
        sWvMin.setData(minArr);
        alarmWvHour.setData(hourArr);
        alarmWvMin.setData(minArr);

        startDateLay.setOnClickListener(this);
        endDateLay.setOnClickListener(this);
        startTimeLay.setOnClickListener(this);
        endTimeLay.setOnClickListener(this);
        colorLay.setOnClickListener(this);


        switchButton.setOnCheckedChangeListener(this);
        alarmButton.setOnCheckedChangeListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.startDateLayout :
                if(startWVLay.getVisibility() == View.VISIBLE)
                {
                    startWVLay.setVisibility(View.GONE);
                }
                else
                {
                    startWVLay.setVisibility(View.VISIBLE);
                    startTimeWVLay.setVisibility(View.GONE);
                }
                break;
            case R.id.endDateLayout :
                if(endWVLay.getVisibility() == View.VISIBLE)
                {
                    endWVLay.setVisibility(View.GONE);
                }
                else
                {
                    endWVLay.setVisibility(View.VISIBLE);
                    endTimeWVLay.setVisibility(View.GONE);
                }
                break;
            case R.id.startTimeLayout :
                if(startTimeWVLay.getVisibility() == View.VISIBLE)
                {
                    startTimeWVLay.setVisibility(View.GONE);
                }
                else
                {
                    startTimeWVLay.setVisibility(View.VISIBLE);
                    startWVLay.setVisibility(View.GONE);
                }
                break;
            case R.id.endTimeLayout :
                if(endTimeWVLay.getVisibility() == View.VISIBLE)
                {
                    endTimeWVLay.setVisibility(View.GONE);
                }
                else
                {
                    endTimeWVLay.setVisibility(View.VISIBLE);
                    endWVLay.setVisibility(View.GONE);
                }
                break;
            case R.id.colorLayout :
                final ColorPicker colorPicker = new ColorPicker(CalendarWriteActivity.this);
                colorPicker.setRoundColorButton(true);
                colorPicker.disableDefaultButtons(true);
                colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
                    @Override
                    public void setOnFastChooseColorListener(int position, int color) {

                        String hexColor = String.format("#%06X", (0xFFFFFF & color));       ///color integer값 rgb로변경
                        colorImageView.setBackgroundColor(color);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                colorPicker.show();


                break;




        }
    }

    @Override
    public void onCheckedChanged(SwitchButton view, boolean isChecked) {
        switch (view.getId())
        {
            case R.id.switch_button :
                if(isChecked)
                {
                    Toast.makeText(CalendarWriteActivity.this,"check",Toast.LENGTH_SHORT).show();
                    startTimeLay.setVisibility(View.INVISIBLE);
                    endTimeLay.setVisibility(View.INVISIBLE);
                    startTimeWVLay.setVisibility(View.GONE);
                    endTimeWVLay.setVisibility(View.GONE);

                }
                else
                {
                    Toast.makeText(CalendarWriteActivity.this,"release",Toast.LENGTH_SHORT).show();
                    startTimeLay.setVisibility(View.VISIBLE);
                    endTimeLay.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.alarm_button :
                if(isChecked)
                {
                    alarmWVLay.setVisibility(View.VISIBLE);
                }
                else
                {
                    alarmWVLay.setVisibility(View.GONE);
                }
                break;

        }
    }
}
