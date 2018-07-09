package globesoft.ju_heath.Activity.MypageFragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-02-06.
 */

public class CoachProfileModifyFragment extends Fragment {

    View view;
    EditText Edtname;
    EditText Edtphone;
    EditText Edtgym;
    EditText Edtaward;
    EditText Edtlicnese;
    EditText Edtcareer;
    LinearLayout modifyLay;

    String name;
    String phone;
    String gym;
    String award;
    String license;
    String career;
    String myUid;

    FirebaseFirestore db;
    FragmentManager fragmentManager;

    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view ==null)
        {

            view = inflater.from(getContext()).inflate(R.layout.fragment_profilemodify,null);
            Edtname = (EditText)view.findViewById(R.id.editTextName);
            Edtphone = (EditText)view.findViewById(R.id.editTextPhone);
            Edtgym = (EditText)view.findViewById(R.id.editTextGym);
            Edtaward = (EditText)view.findViewById(R.id.editText_award);
            Edtlicnese = (EditText)view.findViewById(R.id.editText_license);
            Edtcareer = (EditText)view.findViewById(R.id.editText_career);
            modifyLay = (LinearLayout)view.findViewById(R.id.modifyButtonLay);
            context = getContext();

        }


        myUid = ((Ju_healthApp)getContext().getApplicationContext()).getUserUid();
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        gym = bundle.getString("gym");
        award = bundle.getString("award");
        license = bundle.getString("license");
        career = bundle.getString("career");

        if(name!=null&&!name.equals("")) Edtname.setText(name);
        if(phone!=null&&!phone.equals("")) Edtphone.setText(phone);
        if(gym!=null&&!gym.equals("")) Edtgym.setText(gym);
        if(award!=null&&!award.equals("")) Edtaward.setText(award);
        if(license!=null&&!license.equals("")) Edtlicnese.setText(license);
        if(career!=null&&!career.equals("")) Edtcareer.setText(career);

        db = FirebaseFirestore.getInstance();
        fragmentManager = getFragmentManager();


        modifyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modify();

                // 키보드 감추기
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Edtphone.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(Edtgym.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(Edtaward.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(Edtlicnese.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(Edtcareer.getWindowToken(), 0);
            }
        });



        return view;
    }


    public void modify(){

        Map <String,String> profile = new HashMap<>();

        phone = Edtphone.getText().toString();
        gym = Edtgym.getText().toString();
        award = Edtaward.getText().toString();
        license = Edtlicnese.getText().toString();
        career =Edtcareer.getText().toString();

        boolean result = checkData();

        if(result) {

            profile.put("phone",phone);
            profile.put("gym",gym);
            profile.put("award",award);
            profile.put("license",license);
            profile.put("career",career);

            db.collection("trainer").document(myUid).set(profile, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getContext(),"수정이 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                    Fragment mypageFragment = new CoachMypageFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("uid",myUid);
                    mypageFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_base_lay, mypageFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"fail",Toast.LENGTH_SHORT).show();
                    LogUtil.d(e.getMessage().toString());
                }
            });

        }

    }

    public boolean checkData() {

        String change_data;

        phone = Edtphone.getText().toString();
        gym = Edtgym.getText().toString();

        // 전화번호 입력여부 검사
        if(phone.isEmpty())
        {
            Edtphone.requestFocus();
            Edtphone.setError("전화번호를 입력해주세요.");
//            Toast.makeText(context, "전화번호를 입력해주세요!", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(!Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone))   //핸드폰번호 유효성 검사
        {
            Edtphone.requestFocus();
            Edtphone.setError("올바른 핸드폰 번호가 아닙니다.");
//            Toast.makeText(context, "올바른 핸드폰 번호가 아닙니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // gym 입력여부 검사
        if(gym.length() < 3 || gym.length() > 18)
        {
            Edtgym.requestFocus();
            Edtgym.setError("3~17자 이내로 입력해주세요.");
//            Toast.makeText(context, "체육관은 3~17자 이내로 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

}
