package globesoft.ju_heath.Activity.MypageFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pl.wheelview.WheelView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import globesoft.ju_heath.DTO.birthDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-31.
 */

public class GenderDialog extends Dialog {

    Context context;

    String gender;


    ImageView okImageView;

    private OnDismissListener _listener ;

    public GenderDialog(@NonNull Context context, String gender) {
        super(context);
        this.gender = gender;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        // 다이얼로그 외부 화면 흐리게 표현
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_gender);

        final ImageView manImageVIew = (ImageView)findViewById(R.id.manImageView);
        final ImageView womanImageView = (ImageView)findViewById(R.id.womanImageVIew);
        ImageView okImageView = (ImageView)findViewById(R.id.okImageView);

        if(gender!=null&&!gender.equals(""))
        {
            if(gender.equals("M"))
            {
                manImageVIew.performClick();
            }
            else if(gender.equals("F"))
            {
                womanImageView.performClick();
            }
        }

        manImageVIew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "M";
                manImageVIew.setImageResource(R.drawable.gender_man_red);
                womanImageView.setImageResource(R.drawable.gender_woman_gray);
            }
        });

        womanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gender = "F";
                womanImageView.setImageResource(R.drawable.gender_woman_red);
                manImageVIew.setImageResource(R.drawable.gender_man_gray);
            }
        });


        okImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( _listener == null ) {} else {
                    _listener.onDismiss( GenderDialog.this ) ;
                }
                dismiss();
            }
        });


    }

    public void setOnDismissListener( OnDismissListener $listener ) {
        _listener = $listener ;
    }

    public String getGender() {
        return gender;
    }
}
