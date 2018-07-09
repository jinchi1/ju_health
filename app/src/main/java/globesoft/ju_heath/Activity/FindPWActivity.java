package globesoft.ju_heath.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import globesoft.ju_heath.R;

public class FindPWActivity extends AppCompatActivity {

    EditText mailEdt;
    Button sendBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);
        mailEdt=(EditText)findViewById(R.id.Edt_mail);
        sendBtn=(Button)findViewById(R.id.sendPW_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!mailEdt.getText().toString().equals(""))
                {

                    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
                    String emailAddress = mailEdt.getText().toString();
                    mFirebaseAuth.sendPasswordResetEmail(emailAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FindPWActivity.this, "send", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
            }
            }
        });

    }
}
