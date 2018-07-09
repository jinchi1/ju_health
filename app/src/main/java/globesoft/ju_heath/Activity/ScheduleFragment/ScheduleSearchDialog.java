package globesoft.ju_heath.Activity.ScheduleFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.usersDTO;
import globesoft.ju_heath.R;

/**
 * Created by system777 on 2018-01-18.
 */

public class ScheduleSearchDialog extends Dialog {

    EditText searchEdt;
    ListView listView;
    FirebaseFirestore db;
    FirebaseAuth auth;
    ArrayList<usersDTO> userDTOArrayList;
    ScheduleSearchAdapter adapter;
    String uid;
    usersDTO usersDTO;


    private OnDismissListener _listener ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_schedulesearch);
        searchEdt = (EditText)findViewById(R.id.Edt_search);
        listView = (ListView)findViewById(R.id.searchListView);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid =auth.getCurrentUser().getUid();
        userDTOArrayList = new ArrayList<>();
        adapter = new ScheduleSearchAdapter(getContext(),userDTOArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                usersDTO = adapter.getItem(i);

                if( _listener == null ) {} else {
                    _listener.onDismiss( ScheduleSearchDialog.this ) ;
                }
                dismiss() ;
            }
        });

        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

                switch (actionId)
                {
                    case EditorInfo.IME_ACTION_SEARCH:
                        if(!searchEdt.getText().toString().equals(""))
                        {
                            userDTOArrayList.clear();
                            Log.i("aaaaa",searchEdt.getText().toString());
                            call_api_user(uid,searchEdt.getText().toString());

                        }
                        break;
                }
                return false;
            }
        });

        call_api_allUser(uid);

    }

    public ScheduleSearchDialog(@NonNull Context context) {
        super(context);
    }

    public void call_api_user(String trainer_uid,String userName)
    {
        db.collection("trainer").document(trainer_uid).collection("users").whereEqualTo("connect",true).whereEqualTo("name",userName).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        usersDTO usersDTO = document.toObject(usersDTO.class);
                        userDTOArrayList.add(usersDTO);
                    }

                    adapter.addItems(userDTOArrayList);
                    adapter.notifyDataSetChanged();


                }else {
                    Log.i("bbbbbbbbbb","Error getting documents: "+ task.getException().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("bbbbbbbbbbb",e.toString());
            }
        });
    }

    public void call_api_allUser(String trainer_uid)
    {
        db.collection("trainer").document(trainer_uid).collection("users").whereEqualTo("connect",true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        usersDTO usersDTO = document.toObject(usersDTO.class);
                        userDTOArrayList.add(usersDTO);
                    }

                    adapter.addItems(userDTOArrayList);
                    adapter.notifyDataSetChanged();


                }else {
                    Log.i("bbbbbbbbbb","Error getting documents: "+ task.getException().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("bbbbbbbbbbb",e.toString());
            }
        });
    }

    public void setOnDismissListener( OnDismissListener $listener ) {
        _listener = $listener ;
    }

    public usersDTO getUser() {
        return usersDTO;
    }

}

