package globesoft.ju_heath.Activity.SignUpActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

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

import globesoft.ju_heath.Activity.LoginActivity;
import globesoft.ju_heath.Activity.MainActivity;
import globesoft.ju_heath.Activity.SearchFragment.SearchActivity;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

/**
 * Created by system777 on 2017-12-26.
 */

public class SignUpCustomerActivity extends Activity {

    EditText editText_customer_name;
    EditText editText_customer_phone;
    TextView Txt_coach_id;
    ImageView sign_up_btn;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Map<String, Object> users = new HashMap<>();
    String name;
    String phone;
    String coach_id = "coach_ID";
    ProgressDialog progressDialog;
    String SignUpEmail;
    String coach_token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_customer);
        editText_customer_name = (EditText) findViewById(R.id.Edt_customer_sign_up_name);
        editText_customer_phone = (EditText) findViewById(R.id.Edt_customer_sign_up_phone);
        Txt_coach_id = (TextView) findViewById(R.id.Txt_customer_sign_up_coach);
        sign_up_btn = (ImageView) findViewById(R.id.customer_signup_btn);
        SignUpEmail = getIntent().getStringExtra("email");

        progressDialog = new ProgressDialog(SignUpCustomerActivity.this,R.style.MyTheme);
        progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);


        Txt_coach_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpCustomerActivity.this, SearchActivity.class);
                startActivityForResult(intent,100);
            }
        });

        try {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {

                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
                    String tempNumber = telephonyManager.getLine1Number();

                    if (tempNumber.startsWith("+82")) {
                        tempNumber = tempNumber.replace("+82", "0");
                    }

                    editText_customer_phone.setText(tempNumber);
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
        }catch (Exception e)
        {
            Toast.makeText(SignUpCustomerActivity.this,e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }




        sign_up_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                name=editText_customer_name.getText().toString();
                phone=editText_customer_phone.getText().toString();
                coach_id=Txt_coach_id.getText().toString();
                progressDialog.show();

                if(name==null||name.equals(""))
                {

                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(editText_customer_name);
                    editText_customer_name.requestFocus();
                    editText_customer_name.setError("이름을 입력해 주세요.");

                }
                else if(phone==null||phone.equals(""))
                {
                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(editText_customer_phone);
                    editText_customer_phone.requestFocus();
                    editText_customer_phone.setError("전화번호를 입력해 주세요.");

                }
                else if(coach_id==null||coach_id.equals("")||coach_id.equals("coach_ID"))
                {
                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(Txt_coach_id);
                    Txt_coach_id.requestFocus();
                    Txt_coach_id.setError("코치를 입력해 주세요.");
                }
                else{
                    /////////////////////////////////////////////////////////////////

                    auth = FirebaseAuth.getInstance();
                    users.put("uid", auth.getCurrentUser().getUid());
                    users.put("name", editText_customer_name.getText().toString());
                    users.put("phone", editText_customer_phone.getText().toString());
                    users.put("token",((Ju_healthApp)getApplicationContext()).getFCMtoken());
                    users.put("connect",false);
                    if(!Txt_coach_id.getText().toString().equals("coach_ID"))users.put("coach", Txt_coach_id.getText().toString());
                    db = FirebaseFirestore.getInstance();
                    db.collection("users").document(auth.getCurrentUser().getUid())
                            .set(users, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            LogUtil.d("success");
                            progressDialog.cancel();
                            /////////////////////////////////////////////////////////////////////////////

                            if(!Txt_coach_id.getText().toString().equals("coach_ID"))
                            {
                                db.collection("trainer").document(Txt_coach_id.getText().toString()).collection("users").document(auth.getCurrentUser().getUid())
                                        .set(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        saveQuote("A",Txt_coach_id.getText().toString(),auth.getCurrentUser().getUid(),editText_customer_name.getText().toString());
                                        ((Ju_healthApp)getApplicationContext()).setuserValue("user");
                                        Intent intent = new Intent(SignUpCustomerActivity.this, MainActivity.class);
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

                                ((Ju_healthApp)getApplicationContext()).setuserValue("user");
                                Intent intent = new Intent(SignUpCustomerActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                    ///////////////////////////////////////////////////////////////

                }

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1)
        {
            Txt_coach_id.setText(data.getStringExtra("uid"));
            coach_token = data.getStringExtra("coach_token");
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        if(SignUpEmail!=null)
        {
            Intent intent = new Intent(SignUpCustomerActivity.this, SelectCardActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Intent intent = new Intent(SignUpCustomerActivity.this, SelectCardActivity.class);
            intent.putExtra("social","social");
            startActivity(intent);
            finish();
        }
    }

    public void saveQuote(final String type,  final String t_uid, String u_uid, final String name){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        // 출력될 포맷 설정
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now_date = simpleDateFormat.format(date);
        Log.i("now_date>>>>>>>>>", now_date);

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("type", "A");
        dataToSave.put("uid", u_uid);
        dataToSave.put("name", name);
        dataToSave.put("regdate", now_date);

        db.collection("trainer").document(t_uid).collection("noti").document(u_uid).set(dataToSave).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                call_push(coach_token,"P",editText_customer_name.getText().toString());
            }
        });


    }


    public void call_push(final String teacher_token, final String type, final String name)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FMC 메시지 생성 start
                    LogUtil.d("ddddddddddddddddddddddddddddddddddddddddddddddddddddd");
                    JSONObject root = new JSONObject();
                    JSONObject data = new JSONObject();

                    if(type.equals("P"))
                    {
                        data.put("type","P");
                        data.put("message", name+"님이 회원신청을 했습니다.");
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

