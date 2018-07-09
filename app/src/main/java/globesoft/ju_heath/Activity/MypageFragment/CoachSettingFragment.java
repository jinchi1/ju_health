package globesoft.ju_heath.Activity.MypageFragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import globesoft.ju_heath.Activity.LoginActivity;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system999 on 2018-01-26.
 */

public class CoachSettingFragment extends Fragment {

    String uid;
    LinearLayout logout;
    LinearLayout back_btn;
    FirebaseAuth auth;
    LinearLayout policy_lay;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_mypage_setting, container, false);


        uid = ((Ju_healthApp)getApplicationContext()).getUserUid();
        auth = FirebaseAuth.getInstance();

        logout = (LinearLayout)view.findViewById(R.id.logout);
        back_btn = (LinearLayout)view.findViewById(R.id.back_btn);
        policy_lay = (LinearLayout)view.findViewById(R.id.policy_lay);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"로그아웃",Toast.LENGTH_SHORT).show();
                auth.signOut();
                LoginManager.getInstance().logOut();
                ((Ju_healthApp)getApplicationContext()).setuserValue("guest");
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment mypageFragment = new CoachMypageFragment();

                Bundle bundle = new Bundle();
                bundle.putString("uid",uid);
                mypageFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.main_base_lay, mypageFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        policy_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),PolicyActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
