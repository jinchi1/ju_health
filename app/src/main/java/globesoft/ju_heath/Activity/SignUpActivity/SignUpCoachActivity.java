package globesoft.ju_heath.Activity.SignUpActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import globesoft.ju_heath.Activity.MainActivity;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2017-12-26.
 */

public class SignUpCoachActivity extends Activity {

    EditText editText_coach_name;
    EditText editText_coach_phone;
    EditText editText_coach_gym;
    ImageView sign_up_btn;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Map<String, Object> trainers = new HashMap<>();
    String name;
    String phone;
    String gym;

    String SignUpEmail;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_coach);
        editText_coach_name = (EditText)findViewById(R.id.Edt_coach_name);
        editText_coach_phone = (EditText)findViewById(R.id.Edt_coach_phone);
        editText_coach_gym = (EditText)findViewById(R.id.Edt_coach_gym);
        sign_up_btn = (ImageView) findViewById(R.id.coach_signup_btn);
        SignUpEmail = getIntent().getStringExtra("email");
        progressDialog = new ProgressDialog(SignUpCoachActivity.this,R.style.MyTheme);
        progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        try {

            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {

                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String tempNumber = telephonyManager.getLine1Number();

                    if (tempNumber.startsWith("+82")) {
                        tempNumber = tempNumber.replace("+82", "0");
                    }

                    editText_coach_phone.setText(tempNumber);
                }

                @Override
                public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                }


            };


            TedPermission.with(this)
                    .setPermissionListener(permissionlistener)
                    .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                    .setPermissions(Manifest.permission.READ_PHONE_STATE)
                    .check();
        } catch (Exception e)
        {
            Toast.makeText(SignUpCoachActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }




        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = editText_coach_name.getText().toString();
                phone = editText_coach_phone.getText().toString();
                gym = editText_coach_gym.getText().toString();
                progressDialog.show();

                if(name.equals(""))
                {

                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(editText_coach_name);
                    editText_coach_name.requestFocus();
                    editText_coach_name.setError("이름을 입력해 주세요.");

                }
                else if(phone.equals(""))
                {
                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(editText_coach_phone);
                    editText_coach_phone.requestFocus();
                    editText_coach_phone.setError("전화번호를 입력해 주세요.");

                }
                else if(gym.equals(""))
                {
                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(editText_coach_gym);
                    editText_coach_gym.requestFocus();
                    editText_coach_gym.setError("소속 헬스장을 입력해 주세요.");
                }

                else
                {
                    auth=FirebaseAuth.getInstance();
                    trainers.put("uid",auth.getCurrentUser().getUid());
                    trainers.put("name",editText_coach_name.getText().toString());
                    trainers.put("phone",editText_coach_phone.getText().toString());
                    trainers.put("gym",editText_coach_gym.getText().toString());
                    trainers.put("token",((Ju_healthApp)getApplicationContext()).getFCMtoken());
                    db = FirebaseFirestore.getInstance();
                    db.collection("trainer").document(auth.getCurrentUser().getUid())
                            .set(trainers, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            LogUtil.d("success");
                            if(!editText_coach_gym.getText().toString().equals(""))
                            {

                                db.collection("gym").document(editText_coach_gym.getText().toString()).collection("trainer").document(auth.getCurrentUser().getUid())
                                        .set(trainers, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        LogUtil.d("success");

                                        ((Ju_healthApp)getApplicationContext()).setuserValue("trainer");
                                        Intent intent = new Intent(SignUpCoachActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        LogUtil.d(e.getMessage());
                                    }
                                });
                            }
                            else
                            {

                                ((Ju_healthApp)getApplicationContext()).setuserValue("trainer");
                                Intent intent = new Intent(SignUpCoachActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            }

                        }
                    });
                }


            }
        });


    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(SignUpEmail!=null)
        {
            Intent intent = new Intent(SignUpCoachActivity.this, SelectCardActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(SignUpCoachActivity.this, SelectCardActivity.class);
            intent.putExtra("social","social");
            startActivity(intent);
            finish();
        }
    }

}
