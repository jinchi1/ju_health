package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import globesoft.ju_heath.DTO.LessonDTO;
import globesoft.ju_heath.DTO.makeLessonDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-01-25.
 */

public class NoScheduleFragment extends Fragment implements Dialog.OnDismissListener {

    View view;
    ArrayList<LessonDTO> arrayList;
    ImageView writeButton;
    TextView warnigTextView;
    TextView monthTextView;
    LessonDTO lessonDTO;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String uid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view ==null)
        {
            view = inflater.inflate(R.layout.fragment_noschedule,container,false);
            monthTextView = (TextView)view.findViewById(R.id.monthTextView);
            writeButton = (ImageView)view.findViewById(R.id.writeButton);
            warnigTextView = (TextView)view.findViewById(R.id.waringTextView);

            auth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();
            arrayList = new ArrayList<>();
            lessonDTO = new LessonDTO();
            Date date = new Date();
            SimpleDateFormat month = new SimpleDateFormat("yyyy.MM");
            monthTextView.setText(month.format(date));



        }

        if(((Ju_healthApp)getContext().getApplicationContext()).getuserValue().equals("trainer"))
        {
            uid = auth.getUid();

            warnigTextView.setText("더하기 버튼을 눌러\n스케줄을 추가해주세요.");
            writeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WriteScheduleDialog writeScheduleDialog = new WriteScheduleDialog(getContext(),true,"nono");
                    writeScheduleDialog.setOnDismissListener(NoScheduleFragment.this) ;
                    writeScheduleDialog.show();
                }
            });
        }else
        {
            writeButton.setVisibility(View.INVISIBLE);
        }


        return view;
    }


    @Override
    public void onDismiss(DialogInterface $dialog) {
        // TODO Auto-generated method stub
        WriteScheduleDialog dialog = (WriteScheduleDialog) $dialog ;
        makeLessonDTO makeLessonDTO = dialog.getmakeLessonDTO();
        if(makeLessonDTO.maketrue) {
            if (makeLessonDTO.lessonTime == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("startDay",makeLessonDTO.startDay);
                bundle.putInt("people",makeLessonDTO.people);
                bundle.putInt("week",makeLessonDTO.week);
                bundle.putBoolean("isHalf",false);
                PagerWriteScheduleFragment pagerWriteScheduleFragment = new PagerWriteScheduleFragment();
                pagerWriteScheduleFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_base_lay,pagerWriteScheduleFragment);
                fragmentTransaction.addToBackStack(null);
                // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.commit();
            } else if (makeLessonDTO.lessonTime == 0) {
                Bundle bundle = new Bundle();
                bundle.putString("startDay",makeLessonDTO.startDay);
                bundle.putInt("people",makeLessonDTO.people);
                bundle.putInt("week",makeLessonDTO.week);
                bundle.putBoolean("isHalf",true);
                PagerWriteScheduleFragment pagerWriteScheduleFragment = new PagerWriteScheduleFragment();
                pagerWriteScheduleFragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.main_base_lay,pagerWriteScheduleFragment);
                fragmentTransaction.addToBackStack(null);
                // fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.commit();
            }
        }
        else
        {

        }
    }

}
