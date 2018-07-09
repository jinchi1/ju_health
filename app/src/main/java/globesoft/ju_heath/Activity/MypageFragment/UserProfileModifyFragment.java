package globesoft.ju_heath.Activity.MypageFragment;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import globesoft.ju_heath.DTO.birthDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2018-02-06.
 */

public class UserProfileModifyFragment extends Fragment {

    View view;


    EditText Edtname;
    EditText Edtphone;
    EditText Edtage;
    EditText Edtweight;
    TextView Edtbirth;
    TextView Txtgender;
    LinearLayout modifyLay;

    String name;
    String phone;
    String age;
    String weight;
    String birth;
    String gender;
    String myUid;
    String coachUid;

    FirebaseFirestore db;
    FragmentManager fragmentManager;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view==null){
            view = inflater.from(getContext()).inflate(R.layout.fragment_userprofilemodify,null);
            Edtname = (EditText)view.findViewById(R.id.editTextName);
            Edtphone = (EditText)view.findViewById(R.id.editTextPhone);
            Edtage = (EditText)view.findViewById(R.id.editTextAge);
            Edtweight = (EditText)view.findViewById(R.id.editTextWeight);
            Edtbirth = (TextView) view.findViewById(R.id.editText_birth);
            Txtgender = (TextView)view.findViewById(R.id.TextView_gender);
            modifyLay = (LinearLayout)view.findViewById(R.id.modifyButtonLay);
            context = getContext();

        }

        fragmentManager = getFragmentManager();
        myUid = ((Ju_healthApp)getContext().getApplicationContext()).getUserUid();
        db = FirebaseFirestore.getInstance();
        Bundle bundle = getArguments();
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        age = bundle.getString("age");
        weight = bundle.getString("weight");
        birth = bundle.getString("birth");
        gender = bundle.getString("gender");
        coachUid = bundle.getString("coachUid");

        if(name!=null&&!name.equals("")) Edtname.setText(name);
        if(phone!=null&&!phone.equals("")) Edtphone.setText(phone);
        if(age!=null&&!age.equals("")) Edtage.setText(age);
        if(weight!=null&&!weight.equals("")) Edtweight.setText(weight);
        if(birth!=null&&!birth.equals("")) Edtbirth.setText(birth);
        if(gender!=null&&!gender.equals("")) Txtgender.setText(gender);


        Txtgender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GenderDialog genderDialog = new GenderDialog(getContext(), gender);
                genderDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        GenderDialog dialog = (GenderDialog) dialogInterface;
                        final String genderString = dialog.getGender();
                        Txtgender.setText(genderString);
                    }
                });
                genderDialog.show();
            }
        });

        Edtbirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BirthDialog birthDialog = new BirthDialog(getContext(), birth);
                birthDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        BirthDialog dialog = (BirthDialog) dialogInterface ;
                        birthDTO birthDTO = dialog.getbirth();
                        final String birthString = birthDTO.month+"월 "+birthDTO.day+"일";
                        Edtbirth.setText(birthString);
                    }
                });
                birthDialog.show();
            }
        });



        modifyLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modify();

                // 키보드 감추기
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(Edtphone.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(Edtage.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(Edtweight.getWindowToken(), 0);
            }
        });



        return view;
    }

    public void modify(){

        final Map<String,String> profile = new HashMap<>();

        phone = Edtphone.getText().toString();
        age = Edtage.getText().toString();
        weight = Edtweight.getText().toString();
        birth = Edtbirth.getText().toString();
        gender =Txtgender.getText().toString();

        boolean result = checkData();

        if(result) {

            profile.put("phone",phone);
            profile.put("age",age);
            profile.put("weight",weight);
            profile.put("birth",birth);
            profile.put("gender",gender);

            db.collection("users").document(myUid).set(profile, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    db.collection("trainer").document(coachUid).collection("users").document(myUid).set(profile,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getContext(),"수정이 완료 되었습니다.",Toast.LENGTH_SHORT).show();
                            Fragment mypageFragment = new UserMypageFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("uid",myUid);
                            mypageFragment.setArguments(bundle);
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.main_base_lay, mypageFragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
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

        phone = Edtphone.getText().toString();

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

        return true;

    }

}
