package globesoft.ju_heath.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import globesoft.ju_heath.Activity.SignUpActivity.SelectCardActivity;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    EditText edtText_id;
    EditText edtText_passwd;
    Button login_button;
    TextView sign_up_button;
    TextView findPW_button;
    LoginButton loginButton;
    ProgressDialog progressDialog;

    ImageView fakeFacebookImageView;
    ImageView fakeGoogleImageView;

    private FirebaseAuth mFirebaseAuth;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mGoogleSigininBtn;
    private CallbackManager mCallbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtText_id = (EditText) findViewById(R.id.edtText_id);
        edtText_passwd = (EditText) findViewById(R.id.edtText_password);
        login_button = (Button) findViewById(R.id.login_btn);
        sign_up_button = (TextView)findViewById(R.id.sign_up_btn);
        mGoogleSigininBtn = (SignInButton)findViewById(R.id.sign_in_btn);
        loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
        findPW_button = (TextView)findViewById(R.id.findPW_btn);
        fakeFacebookImageView = (ImageView)findViewById(R.id.fakeFacebookImageView);
        fakeGoogleImageView = (ImageView)findViewById(R.id.fakeGoogleImageView);


        progressDialog = new ProgressDialog(LoginActivity.this,R.style.MyTheme);
        progressDialog.setProgressStyle(R.style.CustomAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        mCallbackManager =CallbackManager.Factory.create();
        loginButton.setReadPermissions("email","public_profile");

        fakeFacebookImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });

        fakeGoogleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(intent, 100);
            }
        });

        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SelectCardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                startActivity(intent);
                finish();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edtText_id.getText().toString()!=null&&!edtText_id.getText().toString().equals("")&&edtText_passwd.getText().toString()!=null&&!edtText_passwd.getText().toString().equals("")) {
                    progressDialog.show();
                    loginUser(edtText_id.getText().toString(), edtText_passwd.getText().toString());
                }

            }
        });

        findPW_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, FindPWActivity.class);
                startActivity(intent);

            }
        });

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mGoogleSigininBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressDialog.show();
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(intent, 100);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        progressDialog.cancel();
                    }

                    @Override
                    public void onError(FacebookException error) {
                        progressDialog.cancel();
                    }
                });
            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null) {

                    progressDialog.show();
                    checkuser(user);
                }
                else
                {

                }
            }
        };

        edtText_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(edtText_id.getText().toString().equals(""))
                {
                    login_button.setBackgroundResource(R.drawable.login_grey);
                }
                else if(!edtText_passwd.getText().toString().equals(""))
                {
                    login_button.setBackgroundResource(R.drawable.login_red);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtText_passwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(edtText_passwd.getText().toString().equals(""))
                {
                    login_button.setBackgroundResource(R.drawable.login_grey);
                }
                else if(!edtText_id.getText().toString().equals(""))
                {
                    login_button.setBackgroundResource(R.drawable.login_red);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




    }

    private void loginUser(final String email, String password) {
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            progressDialog.cancel();
                            YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(edtText_id);
                            YoYo.with(Techniques.Shake).duration(700).repeat(1).playOn(edtText_passwd);
                            LogUtil.d(task.getException().getMessage());
                            if(task.getException().getMessage().equals("The email address is badly formatted."))
                            {
                                edtText_id.requestFocus();
                                edtText_id.setError("이메일 형식이 틀립니다.");
                            }
                            else if(task.getException().getMessage().equals("There is no user record corresponding to this identifier. The user may have been deleted."))
                            {
                                edtText_id.requestFocus();
                                edtText_id.setError("등록되지 않은 이메일 입니다.");
                            }
                            else if(task.getException().getMessage().equals("The password is invalid or the user does not have a password."))
                            {
                                edtText_passwd.requestFocus();
                                edtText_passwd.setError("비밀번호가 틀립니다.");
                            }


                        } else {


                            mFirebaseAuth=FirebaseAuth.getInstance();
                            FirebaseUser user= mFirebaseAuth.getCurrentUser();
                            checkuser(user);

                        }

                        // ...
                    }
                });
    }

    private void firebaseWithGoogle(GoogleSignInAccount account) {
        mFirebaseAuth=FirebaseAuth.getInstance();
        try
        {
            AuthCredential credential
                    = GoogleAuthProvider.getCredential(account.getIdToken(),null);
            Task<AuthResult> authResultTask
                    = mFirebaseAuth.signInWithCredential(credential);
            authResultTask.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    final FirebaseUser firebaseUser =authResult.getUser();
                    Toast.makeText(LoginActivity.this,firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
                    if (!firebaseUser.toString().equals(""))
                    {
                        checkuser(firebaseUser);
                    }
                }

            });
        }
        catch (Exception e)
        {
            progressDialog.cancel();
        }


    }

    private void handleFacebookAccessToken(AccessToken token) {

        mFirebaseAuth=FirebaseAuth.getInstance();
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("bbbbbbb",task.toString());
                        if (!task.isSuccessful()) {

                            Toast.makeText(LoginActivity.this,"fail",Toast.LENGTH_SHORT).show();
                            Log.d("cccccc",task.getException().toString());
                        } else {
                            AuthResult authResult=task.getResult();
                            final FirebaseUser firebaseUser =authResult.getUser();
                         /*   Profile profile = Profile.getCurrentProfile();
                            String profile_url = profile.getProfilePictureUri(200,200).toString();
                            LogUtil.d("pppppppppppppppppppppppppppppppppppp"+profile_url);*/
                            //Toast.makeText(LoginActivity.this,firebaseUser.getEmail(),Toast.LENGTH_SHORT).show();
                            checkuser(firebaseUser);


                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        progressDialog.cancel();
        Toast.makeText(LoginActivity.this,connectionResult.getErrorMessage(),Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==100) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            GoogleSignInAccount account = result.getSignInAccount();
            firebaseWithGoogle(account);
        }
    }

    public void checkuser(final FirebaseUser user){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("trainer").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    progressDialog.cancel();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        LogUtil.d("document exists");
                        ((Ju_healthApp)getApplicationContext()).setuserValue("trainer");
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();

                    } else {

                        DocumentReference documentReference1 = db.collection("users").document(user.getUid());
                        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful())
                                {        DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {

                                        ((Ju_healthApp)getApplicationContext()).setuserValue("user");
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();

                                    } else {

                                        Intent intent = new Intent(LoginActivity.this,SelectCardActivity.class);
                                        intent.putExtra("social","social");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                                        startActivity(intent);
                                        finish();
                                    }

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                                LogUtil.d(e.getMessage().toString());

                            }
                        });
                    }
                }
                else{
                    progressDialog.cancel();
                    Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.cancel();
                LogUtil.d(e.getMessage().toString());
            }
        });

    }

}
