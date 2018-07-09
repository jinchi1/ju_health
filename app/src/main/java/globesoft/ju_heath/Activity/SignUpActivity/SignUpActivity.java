package globesoft.ju_heath.Activity.SignUpActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

public class SignUpActivity extends Activity {

    EditText editText_id;
    EditText editText_passwd;
    EditText editText_passwd_confirm;
    Button sign_up_button;
    String who;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editText_id = (EditText)findViewById(R.id.edtText_id);
        editText_passwd = (EditText)findViewById(R.id.edtText_password);
        editText_passwd_confirm = (EditText)findViewById(R.id.edtText_passwordConfirm);
        sign_up_button = (Button) findViewById(R.id.sign_up_btn);
        mFirebaseAuth = FirebaseAuth.getInstance();
        who = getIntent().getStringExtra("who");
        LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaa"+who);

        progressDialog = new ProgressDialog(SignUpActivity.this,R.style.MyTheme);
        progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                if(editText_id.getText().toString().equals(""))
                {
                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_id);
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd);
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd_confirm);
                    editText_id.requestFocus();
                    editText_id.setError("메일주소를 입력해 주세요.");


                }
                else if(editText_passwd.getText().toString().equals(""))
                {
                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_id);
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd);
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd_confirm);
                    editText_passwd.requestFocus();
                    editText_passwd.setError("비밀번호를 입력해 주세요.");

                }
                else if(editText_passwd_confirm.getText().toString().equals(""))
                {
                    progressDialog.cancel();
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_id);
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd);
                    YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd_confirm);
                    editText_passwd_confirm.requestFocus();
                    editText_passwd_confirm.setError("비밀번호 확인을 입력해 주세요.");
                }
                else if(!editText_id.getText().toString().equals("")&&!editText_passwd.getText().toString().equals("")&&!editText_passwd_confirm.getText().toString().equals("")) {
                    if (editText_passwd.getText().toString().equals(editText_passwd_confirm.getText().toString())) {
                        createUser(editText_id.getText().toString(), editText_passwd.getText().toString());
                    } else {
                        progressDialog.cancel();
                        YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_id);
                        YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd);
                        YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd_confirm);
                        editText_passwd_confirm.requestFocus();
                        editText_passwd_confirm.setError("비밀번호 확인이 틀립니다.");

                    }
                }
            }
        });

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {


                }
                else
                {

                }
            }
        };
    }



    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void createUser(final String email, final String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            //loginUser(email,password);

                            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_id);
                            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd);
                            YoYo.with(Techniques.Shake).duration(700).repeat(2).playOn(editText_passwd_confirm);
                            if(task.getException().getMessage().equals("The email address is badly formatted."))
                            {
                                progressDialog.cancel();
                                editText_id.requestFocus();
                                editText_id.setError("이메일 형식이 틀립니다.");
                            }
                            else if(task.getException().getMessage().equals("The email address is already in use by another account."))
                            {
                                progressDialog.cancel();
                                editText_id.requestFocus();
                                editText_id.setError("이미 등록된 이메일 입니다.");

                            }
                            else if(task.getException().getMessage().equals("The given password is invalid. [ Password should be at least 6 characters ]"))
                            {
                                progressDialog.cancel();
                                editText_passwd.requestFocus();
                                editText_passwd.setError("비밀번호가 짧습니다.");
                            }

                        } else {
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this, "인증 성공",
                                    Toast.LENGTH_SHORT).show();
                            if(who.equals("customer")) {
                                Intent intent = new Intent(SignUpActivity.this, SignUpCustomerActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                                intent.putExtra("email","email");
                                startActivity(intent);
                                finish();
                            }
                            else if(who.equals("coach")){
                                Intent intent = new Intent(SignUpActivity.this, SignUpCoachActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                                intent.putExtra("email","email");
                                startActivity(intent);
                                finish();

                            }
                        }

                    }
                });
    }

    private void loginUser(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressDialog.cancel();
                        } else {
                            progressDialog.cancel();
                            Toast.makeText(SignUpActivity.this, "이메일 로그인 완료",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(SignUpActivity.this, SelectCardActivity.class);
        startActivity(intent);
        finish();
    }
}
