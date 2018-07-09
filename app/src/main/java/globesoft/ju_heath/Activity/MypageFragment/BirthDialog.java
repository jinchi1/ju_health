package globesoft.ju_heath.Activity.MypageFragment;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.pl.wheelview.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import globesoft.ju_heath.Activity.ScheduleFragment.ScheduleSearchDialog;
import globesoft.ju_heath.DTO.birthDTO;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system999 on 2018-01-31.
 */

public class BirthDialog extends Dialog{

    Context context;

    ArrayList<String> monthArr;
    ArrayList<String> dayArr;

    WheelView monthWheelView;
    WheelView dayWheelView;

    LinearLayout ok;

    String birth;
    int month;
    int day;

    birthDTO bDTO;

    private OnDismissListener _listener ;

    public BirthDialog(@NonNull Context context,String birth) {
        super(context);
        this.birth = birth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_birth);

        monthWheelView = (WheelView)findViewById(R.id.wv1);
        dayWheelView = (WheelView)findViewById(R.id.wv2);
        ok = (LinearLayout)findViewById(R.id.ok);

        bDTO = new birthDTO();
        monthArr = new ArrayList<String>();
        dayArr = new ArrayList<String>();

        for(int j = 1; j<13; j++) {
            monthArr.add(String.valueOf(j));
        }
        for(int k = 1; k<32; k++) {
            dayArr.add(String.valueOf(k));
        }

        monthWheelView.setData(monthArr);
        dayWheelView.setData(dayArr);

        // Cyclic 여부 true일경우 위아래로 값이 생겨서 무제한 스크롤 가능
        monthWheelView.setCyclic(true);
        dayWheelView.setCyclic(true);


        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd");
        Calendar calendar = Calendar.getInstance();

        if(birth == null) {

            long now = System.currentTimeMillis();
            Date date = new Date(now);
            calendar.setTime(date);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);

            System.out.println(">>>>>>>>>mont/day>>>>>>>" + month + "/" + day);
        }else {
            try {
                Date d = dateFormat.parse(birth);
                calendar.setTime(d);
                month = calendar.get(Calendar.MONTH) + 1;
                day = calendar.get(Calendar.DAY_OF_MONTH);

                System.out.println(">>>>>>>>>mont/day>>>>>>>" + month + "/" + day);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String m = monthWheelView.getSelectedText();
                String d = dayWheelView.getSelectedText();

                bDTO.month = Integer.parseInt(m);
                bDTO.day = Integer.parseInt(d);

                if( _listener == null ) {} else {
                    _listener.onDismiss( BirthDialog.this ) ;
                }
                dismiss();
            }
        });


    }

    public void setOnDismissListener( OnDismissListener $listener ) {
        _listener = $listener ;
    }

    public birthDTO getbirth() {
        return bDTO;
    }
}
