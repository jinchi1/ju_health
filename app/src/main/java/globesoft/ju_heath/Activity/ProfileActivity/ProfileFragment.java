package globesoft.ju_heath.Activity.ProfileActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import globesoft.ju_heath.DTO.coachDTO;
import globesoft.ju_heath.Ju_healthApp;
import globesoft.ju_heath.R;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by system777 on 2018-01-10.
 */

public class ProfileFragment extends Fragment {

    View view;
    Button connnect_btn;
    TextView nameTextView;
    TextView phoneTextView;


    String uid;
    FirebaseFirestore db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view ==null)
        {
            view = inflater.inflate(R.layout.activity_coachprofile,container,false);
            connnect_btn = (Button)view.findViewById(R.id.connect_btn);
            nameTextView = (TextView)view.findViewById(R.id.nameTextView);
            phoneTextView = (TextView)view.findViewById(R.id.phoneTextView);

            uid = getArguments().getString("uid");
            db = FirebaseFirestore.getInstance();

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

                    if(((Ju_healthApp) getApplicationContext()).getuserConnect())
                    {
                        Toast.makeText(getContext(), "트레이너와 연결상태입니다",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"신청"+uid,Toast.LENGTH_SHORT).show();

                    }

                }
            });

            call_api_coachProfile();

        }


        return view;
    }

    public void call_api_coachProfile()
    {

        DocumentReference documentReference = db.collection("trainer").document(uid);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    DocumentSnapshot document = task.getResult();
                    coachDTO coachDTO = document.toObject(globesoft.ju_heath.DTO.coachDTO.class);
                    nameTextView.setText(coachDTO.name);
                    phoneTextView.setText(coachDTO.phone);
                }
            }
        });


    }
}
