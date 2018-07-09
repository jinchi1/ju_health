package globesoft.ju_heath.Activity.ProfileActivity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import globesoft.ju_heath.DTO.coachDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;
import globesoft.ju_heath.Util.LogUtil;

public class ProfileActivity extends AppCompatActivity {

    Button connnect_btn;
    TextView nameTextView;
    TextView phoneTextView;

    String uid;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coachprofile);
        connnect_btn=(Button)findViewById(R.id.connect_btn);

        Intent intent = getIntent();

        uid = intent.getStringExtra("uid");
        nameTextView = (TextView)findViewById(R.id.nameTextView);
        phoneTextView = (TextView)findViewById(R.id.phoneTextView);

        db = FirebaseFirestore.getInstance();
       LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+uid);
        LogUtil.d("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz"+((Ju_healthApp)getApplicationContext()).getuserValue());


        if(((Ju_healthApp)getApplicationContext()).getuserValue().equals("user")||((Ju_healthApp)getApplicationContext()).getuserValue().equals("guest"))
        {
            connnect_btn.setVisibility(View.VISIBLE);
        }
        else
        {
            connnect_btn.setVisibility(View.GONE);
        }

        connnect_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent1 = new Intent();
                intent1.putExtra("uid",uid);
                setResult(1,intent1);
                finish();

            }
        });

        call_api_coachProfile();

    }

    public void call_api_coachProfile() {

        DocumentReference documentReference = db.collection("trainer").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    coachDTO coachDTO = document.toObject(globesoft.ju_heath.DTO.coachDTO.class);
                    nameTextView.setText(coachDTO.name);
                    phoneTextView.setText(coachDTO.phone);
                }
            }
        });


    }
    }
