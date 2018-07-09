package globesoft.ju_heath.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import globesoft.ju_heath.Activity.MemberFragment.PagerFragment;
import globesoft.ju_heath.Activity.MypageFragment.CoachMypageFragment;
import globesoft.ju_heath.Activity.MypageFragment.CoachpageFragment;
import globesoft.ju_heath.Activity.MypageFragment.UserMypageFragment;
import globesoft.ju_heath.Activity.ScheduleFragment.ModifyScheduleFragment;
import globesoft.ju_heath.Activity.ScheduleFragment.ModifyScheduleFragmenthalf;
import globesoft.ju_heath.Activity.ScheduleFragment.PagerScheduleFragment;
import globesoft.ju_heath.Activity.ScheduleFragment.PagerWriteScheduleFragment;
import globesoft.ju_heath.Activity.SearchFragment.SearchFragment;
import globesoft.ju_heath.Activity.ETCFragment.HowToUseFragment;
import globesoft.ju_heath.Activity.NotificationFragment.NotificationFragment;
import globesoft.ju_heath.DTO.coachDTO;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.FCM.MyReceiver;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout mBaseMenu;
    private LinearLayout mBtnMenu1;
    private LinearLayout mBtnMenu2;
    private LinearLayout mBtnMenu3;
    private LinearLayout mBtnMenu4;
    private ImageView[] mBtnMenu = new ImageView[5];
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private View profileview;
    private Menu drawableMenu;
    private ActionBar actionBar;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private FirebaseAuth auth;

    private ImageView profile_ImageView;
    private TextView name_TextView;

    String fragmentTag;
    String uservalue;
    String uid;
    String coachuid;
    private FragmentManager fragmentManager;
    MyReceiver mReceiver;
    IntentFilter intentfilter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        mBaseMenu = (LinearLayout)findViewById(R.id.base_bottm_menu);
        profile_ImageView = (ImageView)findViewById(R.id.profileImageView);
        name_TextView = (TextView)findViewById(R.id.nameTextView);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();



        LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+((Ju_healthApp)getApplicationContext()).getuserValue());
        uservalue = ((Ju_healthApp)getApplicationContext()).getuserValue();
        String token = FirebaseInstanceId.getInstance().getToken();
        ((Ju_healthApp)getApplicationContext()).setFCMtoken(token);


        if(auth.getCurrentUser()!=null) {
            checkuser(auth.getCurrentUser());
        }

        intentfilter = new IntentFilter();
        intentfilter.addAction("push");

        mReceiver = new MyReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent) {
                fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_base_lay);
                fragmentTag = fragment.getClass().getSimpleName();

                if (fragment instanceof PagerScheduleFragment){


                    fragmentTransaction.detach(fragment);
                    fragmentTransaction.attach(fragment);
                    fragmentTransaction.commitAllowingStateLoss();

                }
                else if (fragment instanceof NotificationFragment){


                    fragmentTransaction.detach(fragment);
                    fragmentTransaction.attach(fragment);
                    fragmentTransaction.commitAllowingStateLoss();

                }
            }
        };


    }

    @Override
    protected void onPause() {
        super.onPause();
        //if(((Ju_healthApp)getApplicationContext()).getuserValue().equals("trainer"))unregisterReceiver(mReceiver);
        if(mReceiver.isRegistered)unregisterReceiver(mReceiver);

    }
/* @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        mBaseMenu = (LinearLayout)findViewById(R.id.base_bottm_menu);
        profile_ImageView = (ImageView)findViewById(R.id.profileImageView);
        name_TextView = (TextView)findViewById(R.id.nameTextView);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();



        LogUtil.d("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+((Ju_healthApp)getApplicationContext()).getuserValue());
        uservalue = ((Ju_healthApp)getApplicationContext()).getuserValue();



        if(auth.getCurrentUser()!=null) {
            checkuser(auth.getCurrentUser());
        }

    }*/

    public void baseInit(int a) {

        mBtnMenu1 = (LinearLayout) findViewById(R.id.btnMenu01);
        mBtnMenu2 = (LinearLayout) findViewById(R.id.btnMenu02);
        mBtnMenu3 = (LinearLayout) findViewById(R.id.btnMenu03);
        mBtnMenu4 = (LinearLayout) findViewById(R.id.btnMenu04);

        ImageView mBtnImg1 = (ImageView) findViewById(R.id.btnImg1);
        ImageView mBtnImg2 = (ImageView) findViewById(R.id.btnImg2);
        ImageView mBtnImg3 = (ImageView) findViewById(R.id.btnImg3);
        ImageView mBtnImg4 = (ImageView) findViewById(R.id.btnImg4);

        if(uservalue.equals("trainer"))
        {
            mBtnImg2.setImageResource(R.drawable.selector_btn_menu_1);
            mBtnImg3.setImageResource(R.drawable.selector_btn_menu_2);

        }
        else
        {
            mBtnImg2.setImageResource(R.drawable.selector_btn_menu_1_user);
            mBtnImg3.setImageResource(R.drawable.selector_btn_menu_2_user);
        }

        mBtnMenu[0] = mBtnImg1;
        mBtnMenu[1] = mBtnImg2;
        mBtnMenu[2] = mBtnImg3;
        mBtnMenu[3] = mBtnImg4;

        mBtnMenu1.setOnClickListener(this);
        mBtnMenu2.setOnClickListener(this);
        mBtnMenu3.setOnClickListener(this);
        mBtnMenu4.setOnClickListener(this);

        if (a == 0) btnImg(0);
        if (a == 1) btnImg(1);
        if (a == 2) btnImg(2);
        if (a == 3) btnImg(3);





    }

    private void btnImg(int click) {
        LogUtil.d("clickBtn is " + click);
        for (int i = 0; i < 4; i++) {
            if (click == i) {
                mBtnMenu[i].setSelected(true);
            } else {
                mBtnMenu[i].setSelected(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_base_lay);
        fragmentTag = fragment.getClass().getSimpleName();

        switch (v.getId()) {

            case R.id.btnMenu01:

                if (fragment instanceof PagerScheduleFragment == false){


                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.main_base_lay, new PagerScheduleFragment());
                    fragmentTransaction.addToBackStack(fragmentTag);
                    fragmentTransaction.commit();
//                    actionBar.setTitle("Schedule");
                }

                btnImg(0);

                break;
            case R.id.btnMenu02:

                if(uservalue.equals("trainer"))
                {
                    if (fragment instanceof PagerFragment == false) {

                        if(fragment instanceof PagerWriteScheduleFragment||fragment instanceof ModifyScheduleFragment || fragment instanceof ModifyScheduleFragmenthalf)
                        {
                            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ab.setView(R.layout.dialog_custom_cancelwrite);
                            }
                            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    btnImg(1);
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    fragmentTransaction.replace(R.id.main_base_lay, new PagerFragment());
                                    fragmentTransaction.addToBackStack(fragmentTag);
                                    fragmentTransaction.commit();

                                }
                            });
                            ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog dialog = ab.show();
                            TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                            Button posButton = (Button)dialog.findViewById(android.R.id.button1);
                            posButton.setTextColor(Color.parseColor("#ee2b47"));
                            Button negButton = (Button)dialog.findViewById(android.R.id.button2);
                            negButton.setTextColor(Color.parseColor("#333333"));

                        }
                        else
                        {
                            btnImg(1);
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.replace(R.id.main_base_lay, new PagerFragment());
                            fragmentTransaction.addToBackStack(fragmentTag);
                            fragmentTransaction.commit();
                        }
                    }
                }
                else
                {
                    if (fragment instanceof CoachpageFragment == false) {

                        btnImg(1);

                        Fragment coachpageFragment = new CoachpageFragment();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        Bundle bundle = new Bundle();
                        bundle.putString("uid",coachuid);
                        coachpageFragment.setArguments(bundle);
                        fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.replace(R.id.main_base_lay, coachpageFragment);
                        fragmentTransaction.addToBackStack(fragmentTag);
                        fragmentTransaction.commit();
                        //    actionBar.setTitle("Search");
                    }
                }




                break;
            case R.id.btnMenu03:

                if(uservalue.equals("trainer"))
                {
                    if (fragment instanceof NotificationFragment == false) {

                        if(fragment instanceof PagerWriteScheduleFragment||fragment instanceof ModifyScheduleFragment || fragment instanceof ModifyScheduleFragmenthalf)
                        {
                            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ab.setView(R.layout.dialog_custom_cancelwrite);
                            }
                            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    btnImg(2);


                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                    fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.main_base_lay, new NotificationFragment());
                                    fragmentTransaction.addToBackStack(fragmentTag);
                                    fragmentTransaction.commit();

                                }
                            });
                            ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog dialog = ab.show();
                            TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                            Button posButton = (Button)dialog.findViewById(android.R.id.button1);
                            posButton.setTextColor(Color.parseColor("#ee2b47"));
                            Button negButton = (Button)dialog.findViewById(android.R.id.button2);
                            negButton.setTextColor(Color.parseColor("#333333"));

                        }
                        else
                        {
                            btnImg(2);


                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.replace(R.id.main_base_lay, new NotificationFragment());
                            fragmentTransaction.addToBackStack(fragmentTag);
                            fragmentTransaction.commit();
                        }


                    }
                }
                else
                {
                    if (fragment instanceof SearchFragment == false) {

                    btnImg(2);

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    fragmentTransaction.replace(R.id.main_base_lay, new SearchFragment());
                    fragmentTransaction.addToBackStack(fragmentTag);
                    fragmentTransaction.commit();
                //    actionBar.setTitle("Search");
                }
                }
                break;
            case R.id.btnMenu04:
                if(uservalue.equals("trainer"))
                {
                    if (fragment instanceof CoachMypageFragment == false) {

                        if(fragment instanceof PagerWriteScheduleFragment||fragment instanceof ModifyScheduleFragment || fragment instanceof ModifyScheduleFragmenthalf)
                        {
                            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ab.setView(R.layout.dialog_custom_cancelwrite);
                            }
                            ab.setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    btnImg(3);


                                    Fragment mypageFragment = new CoachMypageFragment();
                                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("uid",uid);
                                    mypageFragment.setArguments(bundle);
                                    fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.main_base_lay, mypageFragment);
                                    fragmentTransaction.addToBackStack(fragmentTag);
                                    fragmentTransaction.commit();

                                }
                            });
                            ab.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                            AlertDialog dialog = ab.show();
                            TextView msgView = (TextView) dialog.findViewById(android.R.id.message);
                            Button posButton = (Button)dialog.findViewById(android.R.id.button1);
                            posButton.setTextColor(Color.parseColor("#ee2b47"));
                            Button negButton = (Button)dialog.findViewById(android.R.id.button2);
                            negButton.setTextColor(Color.parseColor("#333333"));

                        }
                        else
                        {
                            btnImg(3);


                            Fragment mypageFragment = new CoachMypageFragment();
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            Bundle bundle = new Bundle();
                            bundle.putString("uid",uid);
                            mypageFragment.setArguments(bundle);
                            fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            fragmentTransaction.replace(R.id.main_base_lay, mypageFragment);
                            fragmentTransaction.addToBackStack(fragmentTag);
                            fragmentTransaction.commit();

                        }

                    }
                }
                else
                {
                    if (fragment instanceof UserMypageFragment == false) {

                        btnImg(3);

                        Fragment mypageFragment = new UserMypageFragment();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                        Bundle bundle = new Bundle();
                        fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        fragmentTransaction.replace(R.id.main_base_lay, mypageFragment);
                        fragmentTransaction.addToBackStack(fragmentTag);
                        fragmentTransaction.commit();
                    }
                }


                break;
        }
    }

    private void menuEvent(Menu menu){
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(fragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fragmentTransaction.replace(R.id.main_base_lay, new HowToUseFragment());
                fragmentTransaction.addToBackStack(fragmentTag);
                fragmentTransaction.commit();
              //  actionBar.setTitle("HowToUse");
                drawer.closeDrawers();
                return false;
            }
        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this,"건의사항",Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return false;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this,"버그신고",Toast.LENGTH_SHORT).show();
                drawer.closeDrawers();
                return false;
            }
        });
        menu.getItem(3).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this,"로그아웃",Toast.LENGTH_SHORT).show();
                auth.signOut();
                name_TextView.setText(null);
                LoginManager.getInstance().logOut();
                ((Ju_healthApp)getApplicationContext()).setuserValue("guest");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return false;
            }
        });


    }


    @Override
    public void onBackPressed() {


            super.onBackPressed();
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_base_lay);       //현재 최상위 프래그먼트 가져오기
            if (fragment instanceof PagerScheduleFragment) {
                baseInit(0);
                //actionBar.setTitle("Schedule");
            } else if (fragment instanceof PagerFragment) {
                baseInit(1);
                //actionBar.setTitle("Member");
            } else if (fragment instanceof SearchFragment) {
                baseInit(2);
                //actionBar.setTitle("Search");
            } else if (fragment instanceof NotificationFragment) {
                baseInit(2);
                //actionBar.setTitle("Notification");
            }
            else if (fragment instanceof UserMypageFragment)
            {
                //actionBar.setTitle("Mypage");
                baseInit(3);
            }
            else if (fragment instanceof CoachMypageFragment)
            {
                //actionBar.setTitle("HowToUse");
                baseInit(3);
            }

        }


    public void fragmentInit(){
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_base_lay, new PagerScheduleFragment());
        //fragmentManager.popBackStackImmediate(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
//        actionBar.setTitle("Schedule");
        baseInit(0);
        fragmentTransaction.commitAllowingStateLoss();

    }



    public void checkuser(final FirebaseUser user){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("trainer").document(user.getUid());
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {        DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        final coachDTO coachDTO = document.toObject(globesoft.ju_heath.DTO.coachDTO.class);
                        coachDTO.name = document.get("name").toString();
                        ((Ju_healthApp)getApplicationContext()).setUserName(document.get("name").toString());
                        ((Ju_healthApp)getApplicationContext()).setUserUid(document.get("uid").toString());
                        LogUtil.d("111111111111111111"+coachDTO.name);

                        ((Ju_healthApp)getApplicationContext()).setuserValue("trainer");
                        LogUtil.d("111111111111111111"+       ((Ju_healthApp)getApplicationContext()).getuserValue());
                        Map<String,String> hash = new HashMap<String,String>();
                        hash.put("token", ((Ju_healthApp)getApplicationContext()).getFCMtoken());
                        db.collection("trainer").document(auth.getCurrentUser().getUid())
                                .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                fragmentInit();
                                registerReceiver(mReceiver, intentfilter);

                            }
                        });


                    } else {

                        DocumentReference documentReference1 = db.collection("users").document(user.getUid());
                        documentReference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful())
                                {        DocumentSnapshot document = task.getResult();
                                     if (document.exists()) {

                                        final usersDTO usersDTO = document.toObject(usersDTO.class);
                                        ((Ju_healthApp)getApplicationContext()).setuserValue("user");
                                         ((Ju_healthApp)getApplicationContext()).setUserName(document.get("name").toString());
                                         ((Ju_healthApp)getApplicationContext()).setUserUid(document.get("uid").toString());
                                        if(usersDTO.connect){
                                         ((Ju_healthApp)getApplicationContext()).setuserConnect(true);
                                         }
                                        else {
                                         ((Ju_healthApp)getApplicationContext()).setuserConnect(false);
                                        }
                                        coachuid = usersDTO.coach;

                                       final Map<String,String> hash = new HashMap<String,String>();
                                       hash.put("token", ((Ju_healthApp)getApplicationContext()).getFCMtoken());
                                       db.collection("users").document(auth.getCurrentUser().getUid())
                                                 .set(hash, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener() {
                                           @Override
                                           public void onSuccess(Object o) {
                                               db.collection("trainer").document(coachuid).collection("users").document(auth.getCurrentUser().getUid()).set(hash,SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                   @Override
                                                   public void onSuccess(Void aVoid) {
                                                       fragmentInit();
                                                   }
                                               });
                                           }
                                       });

                                    } else {


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
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                LogUtil.d(e.getMessage().toString());
            }
        });

    }


}
