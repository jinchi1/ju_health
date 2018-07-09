package globesoft.ju_heath.Activity.SearchFragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import globesoft.ju_heath.DTO.coachDTO;
import globesoft.ju_heath.R;

public class SearchActivity extends Activity {

    EditText searchEdt;
    ListView listView;
    FirebaseFirestore db;
    ArrayList<coachDTO> coachDTOArrayList;
    SearchAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        db = FirebaseFirestore.getInstance();
        listView = (ListView)findViewById(R.id.searchListView);
        searchEdt = (EditText)findViewById(R.id.Edt_search);
        coachDTOArrayList = new ArrayList<>();
        adapter = new SearchAdapter(SearchActivity.this,coachDTOArrayList);
        listView.setAdapter(adapter);



        searchEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        if(!searchEdt.getText().toString().equals(""))
                        {
                            coachDTOArrayList.clear();
                            Log.i("aaaaa",searchEdt.getText().toString());
                            call_api_name(searchEdt.getText().toString());
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }



    public void call_api_name(final String name)
    {
        db.collection("trainer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (DocumentSnapshot document : task.getResult()) {
                        coachDTO coachDTO = document.toObject(globesoft.ju_heath.DTO.coachDTO.class);
                        if(coachDTO.name.contains(name)||coachDTO.gym.contains(name))
                        {
                        coachDTOArrayList.add(coachDTO);
                        }
                    }

                    adapter.addItems(coachDTOArrayList);
                    adapter.notifyDataSetChanged();
                }else {
                    Log.i("aaaaaaaaaaaaaa","Error getting documents: "+ task.getException().toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("aaaaaaaaaaaa",e.toString());
            }
        });
    }


}
