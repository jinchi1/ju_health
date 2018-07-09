package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.LessonDTO;
import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-19.
 */

public class ScheduleListDialog extends Dialog {
    ListView listView;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ArrayList<LessonDTO> lessonDTOArrayList;
    ArrayList<usersDTO> userDTOArrayList;
    ScheduleUserListAdapter adapter;
    String uid;
    ArrayList<String> userUid;
    Context adapterContext;
    usersDTO returnDTO;

    public ScheduleListDialog(@NonNull Context context,ArrayList<String> uid,Context contexta) {
        super(context);
        this.userUid = uid;
        this.adapterContext = contexta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.45f;
        getWindow().setAttributes(lpWindow);


        setContentView(R.layout.dialog_scheduleuserlist);
        listView = (ListView)findViewById(R.id.userListView);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid =auth.getCurrentUser().getUid();
        userDTOArrayList = new ArrayList<>();
        adapter = new ScheduleUserListAdapter(getContext(),userDTOArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                ScheduleAdapter.isSign = true;
                returnDTO = userDTOArrayList.get(position);
                dismiss();

            }
        });
        for(int i=0;i<userUid.size();i++) {
            call_api_user(userUid.get(i));
        }

    }


    public void call_api_user(String user_uid)
    {
        db.collection("users").document(user_uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        usersDTO usersDTO = new usersDTO();
                        usersDTO = document.toObject(usersDTO.class);
                        userDTOArrayList.add(usersDTO);
                        adapter.notifyDataSetChanged();
                    } else {

                    }
                } else {
                }
            }
        });
    }

    public usersDTO getUserDTO(){

        return returnDTO;
    }

}
