package globesoft.ju_heath.Activity.SearchFragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by system777 on 2017-12-20.
 */

public class SearchFragment extends Fragment{

    View view;
    EditText searchEdt;
    ListView listView;
    FirebaseFirestore db;
    ArrayList<coachDTO> coachDTOArrayList;
    SearchFragmentAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view==null)
        {

            db = FirebaseFirestore.getInstance();
            view = inflater.inflate(R.layout.fragment_search, container, false);
            listView = (ListView)view.findViewById(R.id.searchListView);
            searchEdt = (EditText)view.findViewById(R.id.Edt_search);
            coachDTOArrayList = new ArrayList<>();
            adapter = new SearchFragmentAdapter(getContext(),coachDTOArrayList);
            listView.setAdapter(adapter);

        }


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

        return view;
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
