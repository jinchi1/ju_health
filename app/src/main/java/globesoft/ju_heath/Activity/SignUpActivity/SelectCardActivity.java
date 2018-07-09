package globesoft.ju_heath.Activity.SignUpActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import globesoft.ju_heath.Activity.LoginActivity;
import globesoft.ju_heath.FCM.PopupActivity;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;

public class SelectCardActivity extends Activity {

    ImageView customer_card_ImageView;
    ImageView coach_card_ImageView;



    String social;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_card);
        social = getIntent().getStringExtra("social");
        coach_card_ImageView = (ImageView)findViewById(R.id.trainer_card_ImageView);
        customer_card_ImageView = (ImageView)findViewById(R.id.member_card_ImageView);

        coach_card_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(social==null) {
                    Intent intent = new Intent(SelectCardActivity.this, SignUpActivity.class);
                    intent.putExtra("who", "coach");
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Intent intent = new Intent(SelectCardActivity.this, SignUpCoachActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        customer_card_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(social==null)
                {
                Intent intent = new Intent(SelectCardActivity.this, SignUpActivity.class);
                intent.putExtra("who","customer");
                startActivity(intent);
                finish();
                }
                else
                {
                    Intent intent = new Intent(SelectCardActivity.this, SignUpCustomerActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(SelectCardActivity.this, LoginActivity.class);
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        ((Ju_healthApp)getApplicationContext()).setuserValue("guest");
        startActivity(intent);
        finish();
    }
}
